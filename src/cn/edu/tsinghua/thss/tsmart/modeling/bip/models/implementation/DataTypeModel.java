package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.HashSet;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:16<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class DataTypeModel<P extends IDataContainer>
                extends BaseTypeModel<DataTypeModel, DataModel, P> {

    public static HashSet<String> getRegisterTypeNames() {
        return registerTypeNames;
    }

    public static String[] getRegisterTypeNamesAsArray() {
        return registerTypeNames.toArray(new String[registerTypeNames.size()]);
    }

    @ElementList
    private final static HashSet<String> registerTypeNames = new HashSet<String>();

    @Attribute(name = "typeName")
    private String                       typeName;

    public DataTypeModel(@Attribute(name = "typeName") String typeName) {
        this.setTypeName(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        registerTypeNames.add(typeName);
        getInstance().firePropertyChange(DataModel.DATA_TYPE);
        firePropertyChange(NAME);
    }

    @Override
    public DataModel createInstance() {
        instance = (DataModel) new DataModel<P>().setType(this);
        return instance;
    }

    /**
     * 复制dataTypeModel对象，同时复制instance对象的value。 但为了避免出现问题，不复制instance的name
     */
    public DataTypeModel copy() {
        DataTypeModel copyModel = new DataTypeModel(this.typeName);
        DataModel instance = copyModel.createInstance();
        instance.setValue(this.getInstance().getValue());
        return copyModel;
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public String toString() {
        return "DataType{" + "typeName='" + getTypeName() + '\'' + '}';
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
