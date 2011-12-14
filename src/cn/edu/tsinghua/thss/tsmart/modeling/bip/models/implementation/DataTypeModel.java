package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ����3:16<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class DataTypeModel<P extends IDataContainer>
                extends BaseTypeModel<DataTypeModel, DataModel, P> {
    @ElementList
    private final static HashSet<String>               registerTypeNames = new HashSet<String>();
    public final static String                         BOUND             = "bound";
    public final static DataTypeModel                  boolData;
    public final static DataTypeModel                  intData;
    public final static HashMap<String, DataTypeModel> typeSources;

    static {
        boolData = new DataTypeModel("bool");
        intData = new DataTypeModel("int");
        typeSources = new HashMap<String, DataTypeModel>();
        typeSources.put("bool", boolData);
        typeSources.put("int", intData);
    }

    public static boolean addTypeSources(String type, DataTypeModel dataType) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, dataType);
        return true;
    }

    public static boolean removeTypeSources(String type) {
        if (type.equals("int") || type.equals("bool") || !typeSources.containsKey(type))
            return false;
        typeSources.remove(type);
        return true;
    }

    public static String[] getRegisterTypeNamesAsArray() {
        ArrayList<String> list = new ArrayList<String>(typeSources.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
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
        // ���������͸���ֵ
        if (typeName.equals("bool")) {
            instance.setValue("false");
        } else if (typeName.equals("int")) {
            instance.setValue("0");
        }
        return instance;
    }

    /**
     * ����dataTypeModel����ͬʱ����instance�����value�� ��Ϊ�˱���������⣬������instance��name
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
