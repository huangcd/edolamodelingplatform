package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:16<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class DataTypeModel<P extends IDataContainer>
                extends BaseTypeModel<DataTypeModel, DataModel, P> {
    private final static HashMap<String, DataTypeModel>                            typeSources;
    private final static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        DataTypeModel boolData = new DataTypeModel("bool");
        DataTypeModel intData = new DataTypeModel("int");
        typeSources = new HashMap<String, DataTypeModel>();
        addTypeSources("bool", boolData);
        addTypeSources("int", intData);
    }

    public static void saveDataTypes() {
        File file = new File(Activator.getPreferenceDirection(), GlobalProperties.DATA_TYPE_FILE);
        PrintWriter writer;
        try {
            writer = new PrintWriter(file);
            for (String string : getTypes()) {
                writer.println(string);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void loadDataTypes() {
        File file = new File(Activator.getPreferenceDirection(), GlobalProperties.DATA_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!getTypes().contains(line)) {
                    addType(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private static boolean removeTypeSources(String type) {
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

    public static DataTypeModel getDataTypeModel(String type) {
        return (DataTypeModel) typeSources.get(type).copy();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> list = new ArrayList<String>(typeSources.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public DataTypeModel(@Attribute(name = "typeName") String typeName) {
        this.setName(typeName);
    }

    public String getName() {
        return name;
    }

    public DataTypeModel<P> setName(String name) {
        this.name = name;
        getInstance().firePropertyChange(DataModel.DATA_TYPE);
        return super.setName(name);
    }

    @Override
    public DataModel createInstance() {
        instance = (DataModel) new DataModel().setType(this);
        // 给常用类型赋初值
        if (getName().equals("bool")) {
            instance.setValue("false");
        } else if (getName().equals("int")) {
            instance.setValue("0");
        }
        return instance;
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
        return "DataType{" + "typeName='" + getName() + '\'' + '}';
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
