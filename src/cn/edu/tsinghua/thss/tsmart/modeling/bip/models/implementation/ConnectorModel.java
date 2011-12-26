package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:44<br/>
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class ConnectorModel
                extends BaseInstanceModel<ConnectorModel, ConnectorTypeModel, CompoundTypeModel> {

    public final static Dimension CONNECTOR_SIZE = new Dimension(30, 30);
    public final static String    LINE_COLOR     = "lineColor";
    private boolean               export;
    private List<ConnectionModel> sourceConnections;
    private RGB                   lineColor      = ColorConstants.black.getRGB();

    protected ConnectorModel() {
        sourceConnections = new ArrayList<ConnectionModel>();
    }

    /**
     * 导出成形如 export port A is B 格式的文本
     * 
     * @return export port 的字符串
     */
    public String exportPort() {
        return String.format("export port %s %s is _%s", getType().getName(), getName(), getName());
    }

    /**
     * 绑定一个参数。如果argument为null，表示取消绑定
     * 
     * @param index 参数位置
     * @param argument 参数
     * @return 模型自身
     */
    public ConnectorModel bound(int index, PortModel argument) {
        ArgumentEntry entry = getType().getArguments().get(index);
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
        // TODO 添加参数
        // if (portEntries.length > 0) {
        // buffer.append(portEntries[0]);
        // for (int i = 1, size = portEntries.length; i < size; i++) {
        // buffer.append(", ").append(portEntries[i]);
        // }
        // }
        return buffer.append(')').toString();
    }

    @Override
    public ConnectorModel setParent(CompoundTypeModel parent) {
        this.parent = parent;
        addConnections();
        return this;
    }

    private void addConnections() {
        Point center = getPositionConstraint().getCenter();
        List<ArgumentEntry> arguments = getType().getArguments();
        int size = arguments.size();
        final int length = 100;
        double degree = 2 * Math.PI / size;
        int index = 0;
        double alpha = 0;
        for (ArgumentEntry entry : getType().getArguments()) {
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
        // 如果portModel是export的，需要把AtomicModel添加到portModel的属性变化通知队列中去
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
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "连接子名");
        name.setDescription("01");
        properties.add(name);
        ComboBoxPropertyDescriptor export =
                        new ComboBoxPropertyDescriptor(EXPORT_PORT, "是否导出", trueFalseArray);
        export.setDescription("02");
        properties.add(export);
        ComboBoxPropertyDescriptor tag = new ComboBoxPropertyDescriptor(TAG, "标签", CONNECTOR_TAGS);
        tag.setDescription("02");
        properties.add(tag);
        ColorPropertyDescriptor lineColor = new ColorPropertyDescriptor(LINE_COLOR, "连线颜色");
        lineColor.setDescription("03");
        properties.add(lineColor);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (TAG.equals(id)) {
            return getTag() == null ? 0 : Arrays.asList(CONNECTOR_TAGS).indexOf(getTag());
        }
        if (LINE_COLOR.equals(id)) {
            return lineColor;
        }
        if (EXPORT_PORT.equals(id)) {
            return Boolean.toString(export).equals(trueFalseArray[0]) ? 0 : 1;
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
        return TAG.equals(id) || NAME.equals(id) || LINE_COLOR.equals(id) || EXPORT_PORT.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (EXPORT_PORT.equals(id)) {
            setExport(Boolean.parseBoolean(trueFalseArray[(Integer) value]));
        } else if (TAG.equals(id)) {
            int index = (Integer) value;
            if (index == 0)
                setTag(null);
            else
                setTag(CONNECTOR_TAGS[index]);
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
