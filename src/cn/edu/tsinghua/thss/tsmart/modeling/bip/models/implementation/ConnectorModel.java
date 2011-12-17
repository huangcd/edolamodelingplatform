package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Root;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ����4:44<br/>
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class ConnectorModel
                extends BaseInstanceModel<ConnectorModel, ConnectorTypeModel, CompoundTypeModel> {

    private boolean export;

    protected ConnectorModel() {}

    /**
     * ���������� export port A is B ��ʽ���ı�
     * 
     * @return export port ���ַ���
     */
    public String exportPort() {
        return String.format("export port %s %s is _%s", getType().getName(), getName(), getName());
    }

    public PortModel getExportPort() {
        if (export) {
            return null;
            // TODO export������·���Connector Type�����port�����򷵻�null
        }
        return null;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer =
                        new StringBuilder("connector ").append(getType().getName()).append(" _")
                                        .append(getName()).append('(');
        // TODO ��Ӳ���
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
