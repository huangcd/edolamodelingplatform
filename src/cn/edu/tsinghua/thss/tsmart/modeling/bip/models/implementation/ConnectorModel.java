package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.UnboundedException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ����4:44<br/>
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class ConnectorModel
                extends BaseInstanceModel<ConnectorModel, ConnectorTypeModel, CompoundTypeModel> {

    private static final long     serialVersionUID = -8214893138715118215L;
    @Attribute
    private boolean               export;
    @ElementList
    private List<ConnectionModel> sourceConnections;
    @Element
    private RGB                   lineColor        = ColorConstants.black.getRGB();

    protected ConnectorModel() {
        sourceConnections = new ArrayList<ConnectionModel>();
    }

    /**
     * ���������� export port A is B ��ʽ���ı�
     * 
     * @return export port ���ַ���
     */
    public String exportPort() {
        return String.format("export port %s %s is _%s", getType().getPort().getType().getName(),
                        getName(), getName());
    }

    /**
     * ��һ�����������argumentΪnull����ʾȡ����
     * 
     * @param index ����λ��
     * @param argument ����
     * @return ģ������
     * 
     */
    public ConnectorModel bound(int index, PortModel argument) {
        ArgumentEntry entry = getType().getArgumentEntries().get(index);
        PortModel oldArgument = entry.getModel().getInstance();
        if (oldArgument != null) {
            oldArgument.removePropertyChangeListener(this);
        }
        if (argument != null) {
            argument.addPropertyChangeListener(this);
            getType().bound(index, (PortTypeModel) argument.getType());
        } else {
            getType().unbound(index);
        }
        return this;
    }

    public boolean availableToBound(int index, PortModel argument) {
        ArgumentEntry entry = getType().getArgumentEntries().get(index);
        boolean oldIsBounded = entry.isBounded();
        PortTypeModel oldPort = entry.getModel();
        // bound
        entry.bound((PortTypeModel) argument.getType(), getType());
        MessageUtil.clearProblemMessage();

        boolean result = validateOnTheFly();
        // restore
        if (oldIsBounded) {
            entry.bound(oldPort, getType());
        } else {
            entry.unbound();
        }
        return result;
    }

    public PortModel getExportPort() {
        if (export) {
            return getType().getPort();
        }
        return null;
    }

    public String getFriendlyString() {
        return getType().getName() + " " + getName();
    }

    public Color getLineColor() {
        return new Color(null, lineColor);
    }

    public void setLineColor(RGB lineColor) {
        this.lineColor = lineColor;
        firePropertyChange(LINE_COLOR);
        for (ConnectionModel connection : getSourceConnections()) {
            connection.firePropertyChange(LINE_COLOR);
        }
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer =
                        new StringBuilder("connector ").append(getType().getName()).append(" _")
                                        .append(getName()).append('(');
        if (!getType().getArgumentEntries().isEmpty()) {
            for (ArgumentEntry entry : getType().getArgumentEntries()) {
                if (!entry.isBounded())
                    throw new UnboundedException(getType().getName(), getName(), entry.getIndex());
                PortModel port = (PortModel) entry.getModel().getInstance();
                buffer.append(port.getPortStringAsConnectorAgument()).append(", ");
            }
            buffer.setLength(buffer.length() - 2);
        }
        return buffer.append(')').toString();
    }

    /*
     * ����������أ�����Bipģ��
     * 
     * TODO ����������������һ����
     */
    public String exportToBipforCodeGen() {
        // ����������������
        StringBuilder buffer =
                        new StringBuilder("connector ").append(getType().getHardwareCutName())
                                        .append(" _").append(getName()).append('(');
        if (!getType().getArgumentEntries().isEmpty()) {
            for (ArgumentEntry entry : getType().getArgumentEntries()) {
                if (!entry.isBounded())
                    throw new UnboundedException(getType().getName(), getName(), entry.getIndex());
                PortModel port = (PortModel) entry.getModel().getInstance();
                // ���ӵ�Ӳ���˿ڵ�port����
                // ԭ�����
                if (port.getParent() instanceof AtomicTypeModel
                                && ((AtomicTypeModel) port.getParent()).isMarkedHareware()) {
                    continue;
                }
                // port �� parent �� connector��
                // ������connectorֻ�漰��Ӳ���������
                if ((port.getParent() instanceof ConnectorTypeModel)
                                && ((ConnectorTypeModel) port.getParent()).checkOnlyHardwarePorts()) {
                    continue;
                }
                buffer.append(port.getPortStringAsConnectorAgument()).append(", ");
            }
            buffer.setLength(buffer.length() - 2);
        }
        return buffer.append(')').toString();
    }

    @Override
    public ConnectorModel setParent(CompoundTypeModel parent) {
        if (this.parent == parent) {
            return this;
        }
        this.parent = parent;
        addConnections();
        return this;
    }

    /**
     * �ж��ڵ�ǰconnectorģ�����棬���󶨵�port������component�Ƿ��뵱ǰconnector�ж�����ߡ�����ж�����ߣ�����false�����򷵻�true��
     * ��һ��connector������һ��component�ж�����ߣ�
     * 
     * @param port ���󶨵Ķ˿�
     * @param index ���󶨶˿ڵ�����λ��
     * @return �Ƿ�����˿ڰ�
     */
    public boolean portFromSameComponentAlreadyExists(PortModel port, int index) {
        IContainer parent = port.getParent();
        List<ArgumentEntry> list = getType().getArgumentEntries();
        for (int i = 0, size = list.size(); i < size; i++) {
            if (i == index) {
                continue;
            }
            ArgumentEntry entry = list.get(i);
            if (!entry.isBounded()) {
                continue;
            }
            IContainer entryParent = entry.getModel().getInstance().getParent();
            if (parent.equals(entryParent)) {
                return true;
            }
        }
        return false;
    }

    public void reverseConnections(ConnectionModel model) {
        int index = model.getArgumentIndex();
        Point center = getPositionConstraint().getCenter();
        List<ArgumentEntry> arguments = getType().getArgumentEntries();
        int size = arguments.size();
        final int length = 100;
        double degree = 2 * Math.PI / size;
        double alpha = degree * index;
        Point point =
                        new PrecisionPoint(center.x + length * Math.cos(alpha) - BULLET_RADIUS,
                                        center.y + length * Math.sin(alpha) - BULLET_RADIUS);
        ArgumentEntry entry = getType().getArgumentEntries().get(index);
        InvisibleBulletModel bullet = new InvisibleBulletModel();
        bullet.setPositionConstraint(new Rectangle(point, new Dimension(-1, -1)));
        getParent().addChild(bullet);

        model.setSource(this);
        model.setTarget(bullet);
        model.attachSource();
        model.attachTarget();
    }

    /**
     * ��connector��ӵ�CompoundType����ȥ��ͬʱ�����connector������
     */
    private void addConnections() {
        Point center = getPositionConstraint().getCenter();
        List<ArgumentEntry> arguments = getType().getArgumentEntries();
        int size = arguments.size();
        final int length = 100;
        double degree = 2 * Math.PI / size;
        int index = 0;
        double alpha = 0;
        for (ArgumentEntry entry : getType().getArgumentEntries()) {
            Point point =
                            new PrecisionPoint(center.x + length * Math.cos(alpha) - BULLET_RADIUS,
                                            center.y + length * Math.sin(alpha) - BULLET_RADIUS);
            alpha += degree;
            InvisibleBulletModel bullet = new InvisibleBulletModel();
            bullet.setPositionConstraint(new Rectangle(point, new Dimension(-1, -1)));
            getParent().addChild(bullet);
            ConnectionModel model = new ConnectionModel(index);
            index++;
            model.setSource(this);
            model.setTarget(bullet);
            model.attachSource();
            model.attachTarget();
        }
    }

    public boolean isExport() {
        return export;
    }

    public ConnectorModel setExport(boolean export) {
        if (this.export == export) {
            return this;
        }
        // ���portModel��export�ģ���Ҫ��AtomicModel��ӵ�portModel�����Ա仯֪ͨ������ȥ
        if (export) {
            addPropertyChangeListener(getParent().getInstance());
            addPropertyChangeListener(getType().getPort().getBullet());
        }
        this.export = export;
        firePropertyChange(EXPORT_PORT, !export, export);
        if (!export) {
            removePropertyChangeListener(getParent().getInstance());
            removePropertyChangeListener(getType().getPort().getBullet());
        }
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "��������");
        name.setDescription("01");
        properties.add(name);
        ComboBoxPropertyDescriptor export =
                        new ComboBoxPropertyDescriptor(EXPORT_PORT, "�Ƿ񵼳�", TRUE_FALSE_ARRAY);
        export.setDescription("02");
        properties.add(export);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "��ǩ");
        tag.setDescription("02");
        properties.add(tag);
        ColorPropertyDescriptor lineColor = new ColorPropertyDescriptor(LINE_COLOR, "������ɫ");
        lineColor.setDescription("03");
        properties.add(lineColor);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        if (LINE_COLOR.equals(id)) {
            return lineColor;
        }
        if (EXPORT_PORT.equals(id)) {
            return Boolean.toString(export).equals(TRUE_FALSE_ARRAY[0]) ? 0 : 1;
        }
        return null;
    }

    @Override
    public ConnectorModel setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = new Rectangle(positionConstraint.getLocation(), CONNECTOR_SIZE);
        if (rect.equals(getPositionConstraint())) {
            return this;
        }
        return super.setPositionConstraint(rect);
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ENTITY.equals(id) || NAME.equals(id) || LINE_COLOR.equals(id)
                        || EXPORT_PORT.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (EXPORT_PORT.equals(id)) {
            setExport(Boolean.parseBoolean(TRUE_FALSE_ARRAY[(Integer) value]));
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>) value);
        } else if (LINE_COLOR.equals(id)) {
            setLineColor((RGB) value);
        }
    }

    public List<ConnectionModel> getSourceConnections() {
        return sourceConnections;
    }

    public ConnectorModel addSourceConnection(ConnectionModel connection) {
        sourceConnections.add(connection);
        firePropertyChange(SOURCE);
        return this;
    }

    public ConnectorModel removeSourceConnection(ConnectionModel connection) {
        boolean result = sourceConnections.remove(connection);
        if (result) {
            firePropertyChange(SOURCE);
        }
        return this;
    }
}
