package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:16<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class DataTypeModel<P extends IDataContainer>
                extends BaseTypeModel<DataTypeModel, DataModel, P> {
    private final static long                                                      serialVersionUID;
    private final static HashMap<String, DataTypeModel>                            typeSources;
    private final static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        serialVersionUID = -5282743296523116195L;
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        DataTypeModel boolData = new DataTypeModel("bool");
        DataTypeModel intData = new DataTypeModel("int");
        typeSources = new HashMap<String, DataTypeModel>();
        addTypeSources("bool", boolData);
        addTypeSources("int", intData);
    }

    public static Collection<DataTypeModel> getAllRegisterTypes() {
        return Collections.unmodifiableCollection(typeSources.values());
    }

    public static void clearTypes() {
        Set<String> typeNames = new HashSet<String>(typeSources.keySet());
        for (String type : typeNames) {
            removeType(type);
        }
    }

    public static void saveTypes(File directory) {
        File file = new File(directory, GlobalProperties.DATA_TYPE_FILE);
        try {
            FileOutputStream out = new FileOutputStream(file);
            for (DataTypeModel data : getAllRegisterTypes()) {
                serializer.write(data, out);
                out.flush();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            PrintWriter writer;
            try {
                writer = new PrintWriter(file);
                for (String string : getTypes()) {
                    writer.println(string);
                }
                writer.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void loadTypes(File directory) {
        File file = new File(directory, GlobalProperties.DATA_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            try {
                FileInputStream in = new FileInputStream(file);
                while (true) {
                    try {
                        DataTypeModel data = serializer.read(DataTypeModel.class, in);
                        addType(data.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (Exception e) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!getTypes().contains(line)) {
                        addType(line);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static DataTypeModel addType(String type) {
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
        return model;
    }

    public static void addToolEntry(String type, AtomicEditor editor, CreationToolEntry entry) {
        toolMap.get(type).put(editor, entry);
    }

    protected static void removeType(String type) {
        if (removeTypeSources(type)) {
            HashMap<AtomicEditor, CreationToolEntry> map = toolMap.get(type);
            for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
                editor.removeDataCreationToolEntry(map.get(editor));
            }
            toolMap.remove(type);
        }
    }

    public static boolean isRemovable(String type) {
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            AtomicTypeModel model = (AtomicTypeModel) editor.getModel();
            Map<String, List<DataModel>> map = model.getDatasGroupByType();
            List<DataModel> datas = map.get(type);
            if (datas != null && datas.size() > 0) {
                return false;
            }
        }
        return true;
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

    public static DataTypeModel getModelByName(String type) {
        return (DataTypeModel) typeSources.get(type).copy();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> list = new ArrayList<String>(typeSources.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public DataTypeModel(@Attribute(name = "name") String name) {
        this.setName(name);
    }

    public DataTypeModel<P> setName(String name) {
        super.setName(name);
        getInstance().firePropertyChange(DataModel.DATA_TYPE);
        return this;
    }

    @Override
    /**
     * DataTypeModel不需要有parent
     */
    public DataTypeModel setParent(P parent) {
        return this;
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
