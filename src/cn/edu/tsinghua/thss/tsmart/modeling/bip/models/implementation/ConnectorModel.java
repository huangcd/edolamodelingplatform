package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Root;


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
    private boolean               export;
    private List<ConnectionModel> sourceConnections;

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

    public PortModel getExportPort() {
        if (export) {
            return getType().getPort();
        }
        return null;
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

    public boolean isExport() {
        return export;
    }

    public ConnectorModel setExport(boolean export) {
        this.export = export;
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "连接子名")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
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
        return NAME.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
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
