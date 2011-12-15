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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPortType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;


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
    public final static PortTypeModel                                        ePortType;
    public final static PortTypeModel                                        bPortType;
    public final static PortTypeModel                                        iPortType;
    private final static HashMap<String, PortTypeModel>                      typeSources;
    private static HashMap<String, HashMap<AtomicEditor, CreationToolEntry>> toolMap;

    static {
        toolMap = new HashMap<String, HashMap<AtomicEditor, CreationToolEntry>>();
        typeSources = new HashMap<String, PortTypeModel>();
        ePortType = new PortTypeModel().setName("ePort");
        bPortType = new PortTypeModel().setName("boolPort");
        iPortType = new PortTypeModel().setName("intPort");
        bPortType.addChild((DataTypeModel<PortTypeModel>) DataTypeModel.boolData.copy());
        iPortType.addChild((DataTypeModel<PortTypeModel>) DataTypeModel.intData.copy());
        typeSources.put("ePort", ePortType);
        typeSources.put("boolPort", bPortType);
        typeSources.put("intPort", iPortType);
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
                model.addChild(DataTypeModel.getModel(dataType), dataName);
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

    public static boolean addTypeSources(String type, PortTypeModel dataType) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, dataType);
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

    public static boolean removeTypeSources(String type) {
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
        return typeSources.get(type);
    }

    static class ChildEntry implements Serializable {
        boolean       bounded = false;
        String        name;
        DataTypeModel model;

        public ChildEntry(String name, DataTypeModel model) {
            super();
            this.name = name;
            this.model = model;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public DataTypeModel getModel() {
            return model;
        }

        public void setModel(DataTypeModel model) {
            this.model = model;
        }

        public String getTypeName() {
            return model.getTypeName();
        }

        public boolean isBounded() {
            return bounded;
        }

        public void bound(DataTypeModel model) {
            this.model = model;
            this.bounded = true;
        }

        public String toString() {
            return model.getTypeName() + " " + name;
        }
    }

    @ElementList
    private ArrayList<ChildEntry> arguments;

    public PortTypeModel() {
        this.arguments = new ArrayList<ChildEntry>();
    }

    @Override
    public PortModel createInstance() {
        this.instance = (PortModel) new PortModel().setType(this);
        return instance;
    }

    // /**
    // * 深度根据给出的data参数列表复制一个新的PortTypeModel
    // *
    // * @param dataTypes data参数，应该是新的data
    // *
    // * @return 新的portTypeModel
    // */
    // public PortTypeModel copy(List<DataTypeModel<PortTypeModel>> dataTypes) {
    // PortTypeModel copyModel = new PortTypeModel();
    // copyModel.setName("copyOf" + getName());
    // copyModel.arguments.addAll(dataTypes);
    // return copyModel;
    // }
    //
    // @Override
    // public PortTypeModel copy() {
    // try {
    // byte[] bytes = this.exportToBytes();
    // PortTypeModel newModel = importFromBytes(bytes);
    // newModel.resetID();
    // return newModel;
    // // ByteArrayOutputStream out = new ByteArrayOutputStream();
    // // serializer.write(this, out);
    // // return serializer
    // // .read(PortTypeModel.class, new ByteArrayInputStream(out.toByteArray()));
    // } catch (Exception e) {
    // e.printStackTrace();
    // PortTypeModel copyModel = new PortTypeModel();
    // copyModel.setName(getName());
    // for (DataTypeModel<PortTypeModel> child : arguments) {
    // copyModel.addChild(child);
    // }
    // return copyModel;
    // }
    // }

    @Override
    /**
     * 导出的同时设置各个DataTypeModel的name
     */
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("port type ").append(getName()).append('(').append(getArguments())
                        .append(')');
        return buffer.toString();
    }

    public String getArguments() {
        StringBuilder buffer = new StringBuilder();
        if (arguments != null && !arguments.isEmpty()) {
            ChildEntry child = arguments.get(0);
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
            for (ChildEntry entry : arguments) {
                buffer.append(entry.getTypeName()).append(' ');
                if (entry.isBounded())
                    buffer.append(entry.getModel().getInstance().getName());
                else
                    buffer.append("$UNBOUNDED$");
                buffer.append(", ");
            }
        }
        return buffer.substring(0, buffer.length() - 2);
    }

    public PortTypeModel bound(int index, DataTypeModel model) {
        arguments.get(index).bound(model);
        return this;
    }

    public boolean isBounded(int index) {
        return arguments.get(index).isBounded();
    }

    public List<DataTypeModel<PortTypeModel>> getChildren() {
        return getOrderList();
    }

    public DataTypeModel<PortTypeModel> removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        return arguments.remove(index).getModel();
    }

    @Override
    public List<DataTypeModel<PortTypeModel>> getOrderList() {
        ArrayList<DataTypeModel<PortTypeModel>> list =
                        new ArrayList<DataTypeModel<PortTypeModel>>();
        for (ChildEntry entry : arguments) {
            list.add(entry.getModel());
        }
        return list;
    }

    @Override
    public boolean removeChild(DataTypeModel<PortTypeModel> child) {
        firePropertyChange(CHILDREN);
        return arguments.remove(child);
    }

    public PortTypeModel addChild(DataTypeModel child, String name) {
        arguments.add(new ChildEntry(name, child));
        firePropertyChange(CHILDREN);
        return this;
    }

    public PortTypeModel addChild(DataTypeModel<PortTypeModel> child) {
        // FIXME 名字可能会重复，应该确保其不重复
        return addChild(child, "p" + (arguments.size() + 1));
    }

    public void setOrderModelChild(DataTypeModel<PortTypeModel> child, int index) {
        arguments.get(index).setModel(child);
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
        List<DataTypeModel> list = new ArrayList<DataTypeModel>();
        for (ChildEntry entry : arguments) {
            list.add(entry.getModel());
        }
        return list;
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
