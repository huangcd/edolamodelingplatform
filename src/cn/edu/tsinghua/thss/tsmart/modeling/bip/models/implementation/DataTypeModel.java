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

    @ElementList
    private final static HashSet<String> registerTypeNames = new HashSet<String>();
    public final static String           BOUND             = "bound";
    public final static DataTypeModel    boolData          = new DataTypeModel("bool");
    public final static DataTypeModel    intData           = new DataTypeModel("int");

    public static HashSet<String> getRegisterTypeNames() {
        return registerTypeNames;
    }

    public static String[] getRegisterTypeNamesAsArray() {
        return registerTypeNames.toArray(new String[registerTypeNames.size()]);
    }

    @Attribute(name = "typeName")
    private String  typeName;

    @Attribute(name = "bound")
    private boolean bounded = true;

    public DataTypeModel(@Attribute(name = "typeName") String typeName) {
        this.setTypeName(typeName);
    }

    public DataTypeModel(@Attribute(name = "typeName") String typeName,
                    @Attribute(name = "bound") boolean bound) {
        this.setTypeName(typeName);
        this.setBounded(bound);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
        if (!typeName.isEmpty()) {
            registerTypeNames.add(typeName);
        }
        getInstance().firePropertyChange(DataModel.DATA_TYPE);
        firePropertyChange(NAME);
    }

    @Override
    public DataModel createInstance() {
        instance = (DataModel) new DataModel<P>().setType(this);
        // 给常用类型赋初值
        if (typeName.equals("bool")) {
            instance.setValue("false");
        } else if (typeName.equals("int")) {
            instance.setValue("0");
        }
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
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    public boolean isBounded() {
        return bounded;
    }

    public DataTypeModel setBounded(boolean bounded) {
        this.bounded = bounded;
        firePropertyChange(BOUND);
        return this;
    }
}
