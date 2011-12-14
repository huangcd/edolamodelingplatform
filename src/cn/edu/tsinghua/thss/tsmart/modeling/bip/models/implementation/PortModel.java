package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class PortModel<P extends IComponentType>
                extends BaseInstanceModel<PortModel, PortTypeModel, P>
                implements
                    IPort<PortModel, PortTypeModel, P, IDataContainer> {

    public final static String PORT   = "port";
    public final static String EXPORT = "export";
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
        firePropertyChange(EXPORT);
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
        List<DataModel<IDataContainer>> datas = getPortArguments();
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
    public List<DataModel<IDataContainer>> getPortArguments() {
        ArrayList<DataModel<IDataContainer>> arguments = new ArrayList<DataModel<IDataContainer>>();
        for (DataTypeModel<PortTypeModel> dataTypeModel : getType().getChildren()) {
            arguments.add(dataTypeModel.getInstance());
        }
        return arguments;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "name"),
                        new ComboBoxPropertyDescriptor(EXPORT, "exportable", trueFalseArray),};
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

    public String getFriendlyString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("port type ").append(getType().getName()).append(" ").append(getName());
        // TODO
        return buffer.toString();
    }
}
