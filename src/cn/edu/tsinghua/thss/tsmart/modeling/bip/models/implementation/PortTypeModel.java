package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:26<br/>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Root
public class PortTypeModel extends BaseTypeModel<PortTypeModel, PortModel, ITopModel>
                implements
                    IOrderContainer<DataTypeModel> {
    private static final long                                                      serialVersionUID =
                                                                                                                    3057282699052209648L;
    private final static HashMap<String, PortTypeModel>                            typeSources;
    private final static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        typeSources = new HashMap<String, PortTypeModel>();
        PortTypeModel ePortType = new PortTypeModel().setName("ePort");
        addTypeSources("ePort", ePortType);
    }

    public static Collection<PortTypeModel> getAllRegisterTypes() {
        return Collections.unmodifiableCollection(typeSources.values());
    }

    public static void clearTypes() {
        Set<String> typeNames = new HashSet<String>(typeSources.keySet());
        for (String type : typeNames) {
            removeType(type);
        }
    }

    public static void saveTypes(File directory) {
        File file = new File(directory, GlobalProperties.PORT_TYPE_FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (Map.Entry<String, PortTypeModel> entry : getTypeEntries()) {
                PortTypeModel connector = entry.getValue();
                ITopModel parent = connector.getParent();
                connector.setParent(null);
                out.writeObject(entry.getValue());
                out.flush();
                connector.setParent(parent);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTypes(File directory) {
        File file = new File(directory, GlobalProperties.PORT_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                PortTypeModel model = (PortTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void addType(PortTypeModel model) {
        String type = model.getName();
        addTypeSources(type, model);
        HashMap<AtomicEditor, CreationToolEntry> map =
                        new HashMap<AtomicEditor, CreationToolEntry>();
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "新建一个" + type + "端口",
                                            new CopyFactory(model),
                                            BIPEditor.getImage("icons/port_16.png"),
                                            BIPEditor.getImage("icons/port_32.png"));
            editor.addPortCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    public static PortTypeModel parsePort(String type, String arguments) {
        PortTypeModel model = new PortTypeModel().setName(type);
        if (!arguments.endsWith(",")) {
            Scanner scan = new Scanner(arguments);
            scan.useDelimiter(",");
            while (scan.hasNext()) {
                String argument = scan.next().trim();
                String[] temp = argument.split("\\s+");
                String dataType = temp[0];
                String dataName = temp[1];
                model.addChild(DataTypeModel.getModelByName(dataType), dataName);
            }
        }
        return model;
    }

    private static boolean addTypeSources(String type, PortTypeModel port) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, port);
        return true;
    }

    public static void addToolEntry(String type, AtomicEditor editor, CreationToolEntry entry) {
        if (!toolMap.containsKey(type)) {
            toolMap.put(type, new HashMap<AtomicEditor, CreationToolEntry>());
        }
        toolMap.get(type).put(editor, entry);
    }

    public static void removeType(String type) {
        if (removeTypeSources(type)) {
            HashMap<AtomicEditor, CreationToolEntry> map = toolMap.get(type);
            for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
                editor.removePortCreationToolEntry(map.get(editor));
            }
            toolMap.remove(type);
        }
    }

    public static boolean isRemovable(String type) {
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            AtomicTypeModel model = (AtomicTypeModel) editor.getModel();
            Map<String, List<PortModel>> map = model.getPortsGroupByType();
            List<PortModel> datas = map.get(type);
            if (datas != null && datas.size() > 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean removeTypeSources(String type) {
        if (type.equals("ePort") || !typeSources.containsKey(type)) return false;
        typeSources.remove(type);
        return true;
    }

    public static Set<String> getTypes() {
        return typeSources.keySet();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> array = new ArrayList<String>(typeSources.keySet());
        Collections.sort(array);
        return array.toArray(new String[array.size()]);
    }

    public static Set<Map.Entry<String, PortTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static PortTypeModel getModelByName(String type) {
        return typeSources.get(type).copy();
    }

    static class ArgumentEntry implements Serializable {
        /**
         * 
         */
        private static final long serialVersionUID = -9063660604830297267L;
        private int               index;
        private boolean           bounded          = false;
        private String            name;
        private DataTypeModel     model;

        protected ArgumentEntry(String name, DataTypeModel model, int index) {
            super();
            this.name = name;
            this.model = model;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        protected void setName(String name) {
            this.name = name;
        }

        public DataTypeModel getModel() {
            return model;
        }

        protected void setModel(DataTypeModel model) {
            this.model = model;
        }

        public String getTypeName() {
            return model.getName();
        }

        public boolean isBounded() {
            return bounded;
        }

        protected void bound(DataTypeModel model) {
            this.model = model;
            this.bounded = true;
        }

        public void unbound() {
            this.bounded = false;
        }

        public int getIndex() {
            return index;
        }

        public String toString() {
            return model.getName() + " " + name;
        }
    }

    @ElementList
    private ArrayList<ArgumentEntry> arguments;

    public PortTypeModel() {
        this.arguments = new ArrayList<ArgumentEntry>();
    }

    @Override
    public PortModel createInstance() {
        this.instance = (PortModel) new PortModel().setType(this);
        return instance;
    }

    @Override
    /**
     * 导出的同时设置各个DataTypeModel的name
     */
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("port type ").append(getName());
        if (!getArgumentAsString().equals("")) {
            buffer.append('(').append(getArgumentAsString()).append(')');
        }
        return buffer.toString();
    }

    public String getArgumentAsString() {
        StringBuilder buffer = new StringBuilder();
        if (arguments != null && !arguments.isEmpty()) {
            ArgumentEntry child = arguments.get(0);
            buffer.append(child.getTypeName()).append(' ').append(child.getName());
            for (int i = 1, size = arguments.size(); i < size; i++) {
                child = arguments.get(i);
                buffer.append(", ").append(child.getTypeName()).append(' ').append(child.getName());
            }
        }
        return buffer.toString();
    }

    /**
     * PortTypeModel不需要parent
     */
    public PortTypeModel setParent(ITopModel parent) {
        return this;
    }

    public String getFriendlyArguments() {
        StringBuilder buffer = new StringBuilder();
        if (arguments != null && !arguments.isEmpty()) {
            for (ArgumentEntry entry : arguments) {
                buffer.append(entry.getTypeName()).append(' ');
                if (entry.isBounded())
                    buffer.append(entry.getModel().getInstance().getName());
                else
                    buffer.append("$UNBOUNDED$");
                buffer.append(", ");
            }
            return buffer.substring(0, buffer.length() - 2);
        } else {
            return "";
        }
    }

    /**
     * 
     * @param index 返回第index个参数。如果未被绑定，返回null。
     * @return
     */
    protected DataModel getData(int index) {
        ArgumentEntry entry = arguments.get(index);
        if (entry.isBounded()) return null;
        return (DataModel) entry.getModel().getInstance();
    }

    protected PortTypeModel bound(int index, DataTypeModel model) {
        arguments.get(index).bound(model);
        getInstance().firePropertyChange(CHILDREN);
        return this;
    }

    protected PortTypeModel unbound(int index) {
        arguments.get(index).unbound();
        getInstance().firePropertyChange(CHILDREN);
        return this;
    }

    public boolean isBounded(int index) {
        return arguments.get(index).isBounded();
    }

    public List<DataTypeModel> getChildren() {
        return getOrderList();
    }

    public DataTypeModel removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        return arguments.remove(index).getModel();
    }

    public boolean removeChild(DataTypeModel child) {
        firePropertyChange(CHILDREN);
        return arguments.remove(child);
    }

    public PortTypeModel addChild(DataTypeModel child, String name) {
        arguments.add(new ArgumentEntry(name, child, arguments.size()));
        firePropertyChange(CHILDREN);
        return this;
    }

    public PortTypeModel addChild(DataTypeModel child) {
        return addChild(child, "d" + (arguments.size() + 1));
    }

    public void setOrderModelChild(DataTypeModel child, int index) {
        arguments.get(index).setModel(child);
        firePropertyChange(CHILDREN);
    }

    @Override
    public boolean allSets() {
        for (ArgumentEntry entry : arguments) {
            if (!entry.isBounded()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public List<DataTypeModel> getArguments() {
        List<DataTypeModel> list = new ArrayList<DataTypeModel>();
        for (ArgumentEntry entry : arguments) {
            list.add(entry.getModel());
        }
        return list;
    }

    public List<String> getArgumentNames() {
        List<String> list = new ArrayList<String>();
        for (ArgumentEntry entry : arguments) {
            list.add(entry.getName());
        }
        return list;
    }

    public List<String> getArgumentTypes() {
        List<String> list = new ArrayList<String>();
        for (ArgumentEntry entry : arguments) {
            list.add(entry.getTypeName());
        }
        return list;
    }

    public List<ArgumentEntry> getArgumentEntries() {
        return arguments;
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
    public void setPropertyValue(Object id, Object value) {

    }

    public boolean isNewNameAlreadyExistsInParent(DataTypeModel child, String newName) {
        for (DataTypeModel instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DataTypeModel> getOrderList() {
        return getArguments();
    }
}
