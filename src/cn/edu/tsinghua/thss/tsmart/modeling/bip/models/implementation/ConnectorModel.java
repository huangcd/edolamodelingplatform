package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPortType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:44<br/>
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class ConnectorModel
    extends BaseInstanceModel<ConnectorModel, ConnectorTypeModel, CompoundTypeModel>
    implements IPort<ConnectorModel, ConnectorTypeModel, CompoundTypeModel, ConnectorTypeModel> {

    class PortEntry {

        @Element
        IComponentInstance component;
        @Element
        IPort<IPort, IPortType, IComponentType, IDataContainer> port;

        public String toString() {
            return String.format("%s.%s", component.getName(), port.getName());
        }
    }

    @ElementArray
    PortEntry[] portEntries;
    @Element
    boolean export = false;

    protected ConnectorModel() {
    }

    @Override
    public ConnectorModel setType(ConnectorTypeModel connectorTypeModel) {
        super.setType(connectorTypeModel);
        portEntries = new PortEntry[type.getArguments().size()];
        return this;
    }

    public List<IPort> getPorts() {
        List<IPort> ports = new ArrayList<IPort>();
        for (PortEntry entry : portEntries) {
            ports.add(entry.port);
        }
        return ports;
    }

    public List<PortEntry> getPortEntries() {
        return Arrays.asList(portEntries);
    }

    protected void enlargeArguments(int newSize) {
        if (newSize <= portEntries.length) {
            return;
        }
        PortEntry[] newPortEntries = new PortEntry[newSize];
        System.arraycopy(portEntries, 0, newPortEntries, 0, portEntries.length);
        portEntries = newPortEntries;
    }

    protected void removeArgument(int index) {
        int length = portEntries.length;
        if (index >= length) {
            return;
        }
        PortEntry[] newPortEntries = new PortEntry[length - 1];
        for (int i = 0, j = 0; i < length; i++) {
            if (i != index) {
                newPortEntries[j] = portEntries[i];
                j++;
            }
        }
        portEntries = newPortEntries;
    }

    protected void shrinkArguments(int newSize) {
        if (newSize >= portEntries.length) {
            return;
        }
        PortEntry[] newPortEntries = new PortEntry[newSize];
        System.arraycopy(portEntries, 0, newPortEntries, 0, newSize);
        portEntries = newPortEntries;
    }

    /**
     * 设置第index个connector参数
     *
     * @param port
     *     port
     * @param index
     *     索引
     */
    public void setArgument(
        IPort<IPort, IPortType, IComponentType, IDataContainer> port, int index) {
        if (index < 0 || index >= portEntries.length) {
            throw new IndexOutOfBoundsException(Integer.toString(index));
        }
        if (portEntries[index] == null) {
            portEntries[index] = new PortEntry();
        }
        if (!getType().validateArguments(port, index)) {
            return;
        }
        portEntries[index].port = port;
        IInstance component = port.getParent().getInstance();
        if (component instanceof IComponentInstance) {
            portEntries[index].component = (IComponentInstance) port.getParent().getInstance();
        }
    }

    /**
     * 导出成形如 export port A is B 格式的文本
     *
     * @return export port 的字符串
     */
    public String exportPort() {
        return String.format("export port %s %s is _%s", getType().getName(), getName(), getName());
    }

    @Override
    public String exportToBip() {
        StringBuilder
            buffer =
            new StringBuilder("connector ").append(getType().getName()).append(" _")
                .append(getName()).append('(');
        if (portEntries.length > 0) {
            buffer.append(portEntries[0]);
            for (int i = 1, size = portEntries.length; i < size; i++) {
                buffer.append(", ").append(portEntries[i]);
            }
        }
        return buffer.append(')').toString();
    }

    @Override
    public List<DataModel<ConnectorTypeModel>> getPortArguments() {
        return getType().getExportDatas();
    }

    @Override
    public boolean isExport() {
        return export;
    }

    @Override
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
        
    }
}