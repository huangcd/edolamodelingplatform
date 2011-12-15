package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:16<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class DataTypeModel<P extends IDataContainer>
                extends BaseTypeModel<DataTypeModel, DataModel, P> {
    public final static String                                               BOUND = "bound";
    public final static DataTypeModel                                        boolData;
    public final static DataTypeModel                                        intData;
    private final static HashMap<String, DataTypeModel>                      typeSources;
    private static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        boolData = new DataTypeModel("bool");
        intData = new DataTypeModel("int");
        typeSources = new HashMap<String, DataTypeModel>();
        typeSources.put("bool", boolData);
        typeSources.put("int", intData);
    }

    public static void addType(String type) {
        DataTypeModel model = new DataTypeModel(type);
        addTypeSources(type, model);
        HashMap<AtomicEditor, CreationToolEntry> map =
                        new HashMap<AtomicEditor, CreationToolEntry>();
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "新建一个" + type + "变量",
                                            new CopyFactory(model),
                                            BIPEditor.getImage("icons/new_data_16.png"),
                                            BIPEditor.getImage("icons/new_data_32.png"));
            editor.addDataCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    public static void addToolEntry(String type, AtomicEditor editor, CreationToolEntry entry) {
        toolMap.get(type).put(editor, entry);
    }

    public static void removeType(String type) {
        HashMap<AtomicEditor, CreationToolEntry> map = toolMap.get(type);
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            editor.removeDataCreationToolEntry(map.get(editor));
        }
        toolMap.remove(type);
        removeTypeSources(type);
        // TODO 删除一种数据类型的时候检查该类型是否被使用
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

    public static Set<String> getTypes() {
        return typeSources.keySet();
    }

    public static Set<Map.Entry<String, DataTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> list = new ArrayList<String>(typeSources.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    @Attribute(name = "typeName")
    private String typeName;


    public DataTypeModel(@Attribute(name = "typeName") String typeName) {
        this.setTypeName(typeName);
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
}
