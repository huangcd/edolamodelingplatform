package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPortType;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:26<br/>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Root
public class PortTypeModel extends BaseTypeModel<PortTypeModel, PortModel, IContainer>
                implements
                    IDataContainer<PortTypeModel, IContainer, DataTypeModel<PortTypeModel>>,
                    IOrderContainer<DataTypeModel<PortTypeModel>>,
                    IPortType<PortTypeModel, PortModel, IContainer> {

    public final static PortTypeModel               ePort = new PortTypeModel().setName("ePort");
    public final static PortTypeModel               bPort = new PortTypeModel().setName("boolPort");
    public final static PortTypeModel               iPort = new PortTypeModel().setName("intPort");

    static {
        bPort.addChild((DataTypeModel<PortTypeModel>) DataTypeModel.boolData);
        iPort.addChild((DataTypeModel<PortTypeModel>) DataTypeModel.intData);
    }

    @ElementList
    private ArrayList<DataTypeModel<PortTypeModel>> arguments;

    public PortTypeModel() {
        this.arguments = new ArrayList<DataTypeModel<PortTypeModel>>();
    }

    @Override
    public PortModel createInstance() {
        this.instance = new PortModel().setType(this);
        return instance;
    }

    /**
     * 深度根据给出的data参数列表复制一个新的PortTypeModel
     * 
     * @param dataTypes data参数，应该是新的data
     * 
     * @return 新的portTypeModel
     */
    public PortTypeModel copy(List<DataTypeModel<PortTypeModel>> dataTypes) {
        PortTypeModel copyModel = new PortTypeModel();
        copyModel.setName("copyOf" + getName());
        copyModel.arguments.addAll(dataTypes);
        return copyModel;
    }

    @Override
    public PortTypeModel copy() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.write(this, out);
            return serializer
                            .read(PortTypeModel.class, new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            PortTypeModel copyModel = new PortTypeModel();
            copyModel.setName(getName());
            for (DataTypeModel<PortTypeModel> child : arguments) {
                copyModel.addChild(child);
            }
            return copyModel;
        }
    }

    @Override
    /**
     * 导出的同时设置各个DataTypeModel的name
     */
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("port type ").append(getName()).append('(');
        if (arguments != null && !arguments.isEmpty()) {
            DataTypeModel child = arguments.get(0);
            child.setName("p0");
            buffer.append(child.getTypeName()).append(' ').append(child.getName());
            for (int i = 1, size = arguments.size(); i < size; i++) {
                child = arguments.get(i);
                child.setName("p" + i);
                buffer.append(", ").append(child.getTypeName()).append(' ').append(child.getName());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    public List<DataTypeModel<PortTypeModel>> getChildren() {
        return getOrderList();
    }

    public DataTypeModel<PortTypeModel> removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        return arguments.remove(index);
    }

    @Override
    public List<DataTypeModel<PortTypeModel>> getOrderList() {
        return arguments;
    }

    @Override
    public boolean removeChild(DataTypeModel<PortTypeModel> child) {
        firePropertyChange(CHILDREN);
        return arguments.remove(child);
    }

    public PortTypeModel addChild(DataTypeModel<PortTypeModel> child) {
        arguments.add(child);
        firePropertyChange(CHILDREN);
        return this;
    }

    public void setOrderModelChild(DataTypeModel<PortTypeModel> child, int index) {
        arguments.add(index, child);
        firePropertyChange(CHILDREN);
    }

    @Override
    public boolean allSets() {
        for (int i = 0, size = arguments.size(); i < size; i++) {
            if (arguments.get(i) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public List<DataTypeModel> getPortTypeArguments() {
        List<DataTypeModel> dataTypes = new ArrayList<DataTypeModel>();
        dataTypes.addAll(arguments);
        return dataTypes;
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

    @Override
    public List<DataModel<PortTypeModel>> getDatas() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isNewNameAlreadyExistsInParent(DataTypeModel<PortTypeModel> child, String newName) {
        for (DataTypeModel<PortTypeModel> instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }
}
