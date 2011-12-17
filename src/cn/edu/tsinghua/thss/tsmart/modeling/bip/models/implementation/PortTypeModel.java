package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:26<br/>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
@Root
public class PortTypeModel extends BaseTypeModel<PortTypeModel, PortModel, IDataContainer>
                implements
                    IOrderContainer<DataTypeModel> {
    private final static HashMap<String, PortTypeModel>                            typeSources;
    private final static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        typeSources = new HashMap<String, PortTypeModel>();
        PortTypeModel ePortType = new PortTypeModel().setName("ePort");
        PortTypeModel bPortType = new PortTypeModel().setName("boolPort");
        PortTypeModel iPortType = new PortTypeModel().setName("intPort");
        bPortType.addChild(DataTypeModel.getDataTypeModel("bool"));
        iPortType.addChild(DataTypeModel.getDataTypeModel("int"));
        addTypeSources("ePort", ePortType);
        addTypeSources("boolPort", bPortType);
        addTypeSources("intPort", iPortType);
    }

    public static void addType(String type, String arguments) {
        PortTypeModel model = new PortTypeModel().setName(type);
        if (!arguments.endsWith(",")) {
            Scanner scan = new Scanner(arguments);
            scan.useDelimiter(",");
            while (scan.hasNext()) {
                String argument = scan.next().trim();
                String[] temp = argument.split("\\s+");
                String dataType = temp[0];
                String dataName = temp[1];
                model.addChild(DataTypeModel.getDataTypeModel(dataType), dataName);
            }
        }
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

    public static boolean addTypeSources(String type, PortTypeModel port) {
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
        HashMap<AtomicEditor, CreationToolEntry> map = toolMap.get(type);
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            editor.removePortCreationToolEntry(map.get(editor));
        }
        toolMap.remove(type);
        removeTypeSources(type);
        // TODO 删除一种类型的时候检查该类型是否被使用
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

    public static PortTypeModel getPortTypeModel(String type) {
        return typeSources.get(type).copy();
    }

    static class ArgumentEntry implements Serializable {
        private int           index;
        private boolean       bounded = false;
        private String        name;
        private DataTypeModel model;

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
        buffer.append("port type ").append(getName()).append('(').append(getArgumentAsString())
                        .append(')');
        return buffer.toString();
    }

    public String getArgumentAsString() {
        StringBuilder buffer = new StringBuilder();
        if (arguments != null && !arguments.isEmpty()) {
            ArgumentEntry child = arguments.get(0);
            buffer.append(child.getTypeName()).append(' ').append(child.getName());
            for (int i = 1, size = arguments.size(); i < size; i++) {
                buffer.append(", ").append(child.getTypeName()).append(' ').append(child.getName());
            }
        }
        return buffer.toString();
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
        // FIXME 名字可能会重复，应该确保其不重复
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
