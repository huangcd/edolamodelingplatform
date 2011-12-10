package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Huangcd Date: 11-9-25 Time: ÏÂÎç6:49
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class PortModel extends BaseInstanceModel<PortModel, PortTypeModel, AtomicTypeModel>
                implements
                    IPort<PortModel, PortTypeModel, AtomicTypeModel, AtomicTypeModel> {

    public final static String PORT = "port";
    @Element
    private boolean            export;

    public PortModel copy(List<DataModel> datas) {
        List<DataTypeModel<PortTypeModel>> dataTypes =
                        new ArrayList<DataTypeModel<PortTypeModel>>();
        for (DataModel<AtomicTypeModel> data : datas) {
            dataTypes.add(data.getType());
        }
        return getType().copy(dataTypes).getInstance();
    }

    public boolean isExport() {
        return export;
    }

    public PortModel setExport(boolean export) {
        this.export = export;
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        if (isExport()) {
            buffer.append("export ");
        }
        buffer.append("port ").append(getName()).append('(');
        List<DataModel<AtomicTypeModel>> datas = getPortArguments();
        if (!datas.isEmpty()) {
            buffer.append(datas.get(0).getName());
            for (int i = 1, size = datas.size(); i < size; i++) {
                buffer.append(", ").append(datas.get(i).getName());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DataModel<AtomicTypeModel>> getPortArguments() {
        ArrayList<DataModel<AtomicTypeModel>> arguments =
                        new ArrayList<DataModel<AtomicTypeModel>>();
        for (DataTypeModel<PortTypeModel> dataTypeModel : getType().getChildren()) {
            arguments.add(dataTypeModel.getInstance());
        }
        return arguments;
    }

    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
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
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub

    }
}
