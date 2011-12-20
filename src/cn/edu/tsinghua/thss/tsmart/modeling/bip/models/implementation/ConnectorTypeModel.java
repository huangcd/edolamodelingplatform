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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:43<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root(name = "connectorType")
public class ConnectorTypeModel
                extends BaseTypeModel<ConnectorTypeModel, ConnectorModel, IDataContainer>
                implements
                    IDataContainer<ConnectorTypeModel, IDataContainer, IInstance>,
                    IOrderContainer<PortTypeModel> {
    private final static HashMap<String, ConnectorTypeModel>                         typeSources;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    static {
        typeSources = new HashMap<String, ConnectorTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();
        ConnectorTypeModel singleton = new ConnectorTypeModel().setName("singleton");
        singleton.addArgument(PortTypeModel.getPortTypeModel("ePort"), "p1");
        singleton.parseInteractor("p1");

        ConnectorTypeModel rendezvous = new ConnectorTypeModel().setName("rendezvous");
        rendezvous.addArgument(PortTypeModel.getPortTypeModel("ePort"), "p1");
        rendezvous.addArgument(PortTypeModel.getPortTypeModel("ePort"), "p2");
        rendezvous.parseInteractor("p1 p2");
        addTypeSources(singleton.getName(), singleton);
        addTypeSources(rendezvous.getName(), rendezvous);
    }

    public static void saveConnectorTypes() {
        File file =
                        new File(Activator.getPreferenceDirection(),
                                        GlobalProperties.CONNECTOR_TYPE_FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (Map.Entry<String, ConnectorTypeModel> entry : getTypeEntries()) {
                out.writeObject(entry.getValue());
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConnectorTypes() {
        File file =
                        new File(Activator.getPreferenceDirection(),
                                        GlobalProperties.CONNECTOR_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                ConnectorTypeModel model = (ConnectorTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model.getName(), model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addType(String type, ConnectorTypeModel connector) {
        addTypeSources(type, connector);
        HashMap<CompoundEditor, CreationToolEntry> map =
                        new HashMap<CompoundEditor, CreationToolEntry>();
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "新建一个" + type + "连接子", new CopyFactory(
                                            connector),
                                            BIPEditor.getImage("icons/connector_16.png"),
                                            BIPEditor.getImage("icons/connector_32.png"));
            editor.addConnectorCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    public static boolean addTypeSources(String type, ConnectorTypeModel connector) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, connector);
        return true;
    }

    public static void addToolEntry(String type, CompoundEditor editor, CreationToolEntry entry) {
        if (!toolMap.containsKey(type)) {
            toolMap.put(type, new HashMap<CompoundEditor, CreationToolEntry>());
        }
        toolMap.get(type).put(editor, entry);
    }

    public static void removeType(String type) {
        HashMap<CompoundEditor, CreationToolEntry> map = toolMap.get(type);
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            editor.removeConnectorCreationToolEntry(map.get(editor));
        }
        toolMap.remove(type);
        removeTypeSources(type);
        // TODO 删除一种类型的时候检查该类型是否被使用
    }

    private static boolean removeTypeSources(String type) {
        if (type.equals("singleton") || type.equals("rendezvous") || !typeSources.containsKey(type))
            return false;
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

    public static Set<Map.Entry<String, ConnectorTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static ConnectorTypeModel getConnectorTypeModel(String type) {
        return typeSources.get(type).copy();
    }

    static class ArgumentEntry implements Serializable {
        private int           index;
        private boolean       bounded = false;
        private String        name;
        private PortTypeModel model;

        protected ArgumentEntry(String name, PortTypeModel model, int index) {
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

        public PortTypeModel getModel() {
            return model;
        }

        protected void setModel(PortTypeModel model) {
            this.model = model;
        }

        public String getTypeName() {
            return model.getName();
        }

        public boolean isBounded() {
            return bounded;
        }

        protected void bound(PortTypeModel model) {
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
            StringBuilder buffer = new StringBuilder();
            buffer.append(model.getName()).append(' ').append(name).append('(');
            return buffer.append(model.getArgumentAsString()).append(')').toString();
        }
    }

    @ElementList
    private List<DataModel<ConnectorTypeModel>> datas;
    @ElementList
    private List<DataModel<ConnectorTypeModel>> exportDatas;
    @ElementList
    private List<InteractionModel>              interactions;
    @ElementList
    private List<ArgumentEntry>                 arguments;
    @Element
    private PortModel                           port;
    @Element(required = false)
    private Interactor                          interactor;

    public ConnectorTypeModel() {
        datas = new ArrayList<DataModel<ConnectorTypeModel>>();
        exportDatas = new ArrayList<DataModel<ConnectorTypeModel>>();
        interactions = new ArrayList<InteractionModel>();
        arguments = new ArrayList<ArgumentEntry>();
        setPort(PortTypeModel.getPortTypeModel("ePort").getInstance());
    }

    public PortModel getPort() {
        return port;
    }

    public ConnectorTypeModel setPort(PortModel port) {
        this.port = port;
        this.port.setName("p");
        return this;
    }

    protected List<DataModel<ConnectorTypeModel>> getExportDatas() {
        return exportDatas;
    }

    public ArgumentEntry addArgument(PortTypeModel child, String name) {
        ArgumentEntry entry = new ArgumentEntry(name, child, arguments.size());
        arguments.add(entry);
        firePropertyChange(CHILDREN);
        return entry;
    }

    @Override
    public ConnectorModel createInstance() {
        if (instance == null) {
            instance = new ConnectorModel().setType(this);
        }
        return instance;
    }

    /**
     * 返回port type参数的定义形式<br>
     * 
     * 对<br>
     * connector type xxx(int2Port p0, ePort p1)<br>
     * 的p0而言，返回的是类似
     * 
     * <br>
     * int2Port p0(int a, int b)<br>
     * 
     * 形式的字符串，用于在编写Action的时候参考port type参数名
     * 
     * @param index
     * @return
     */
    public String getArgumentAsString(int index) {
        return arguments.get(index).toString();
    }

    public String getArgumentName(int index) {
        return arguments.get(index).getName();
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("connector type ").append(getName()).append('(');
        if (arguments != null && !arguments.isEmpty()) {
            for (ArgumentEntry entry : arguments) {
                buffer.append(entry.getTypeName()).append(' ').append(entry.getName()).append(", ");
            }
            buffer.replace(buffer.length() - 2, buffer.length(), "");
        }
        buffer.append(")\n");
        buffer.append("   define ").append(interactor).append('\n');
        for (DataModel<ConnectorTypeModel> data : datas) {
            buffer.append("    ").append(data.exportToBip()).append('\n');
        }
        for (InteractionModel interaction : interactions) {
            buffer.append("    ").append(interaction.exportToBip()).append('\n');
        }
        // TODO export port
        buffer.append("    export port ").append(port.exportToBip()).append('\n');
        buffer.append("end");
        return buffer.toString();
    }

    @Override
    public List<DataModel<ConnectorTypeModel>> getDatas() {
        return datas;
    }

    @Override
    public List<IInstance> getChildren() {
        List<IInstance> list = new ArrayList<IInstance>(interactions);
        list.addAll(datas);
        return list;
    }

    @Override
    public ConnectorTypeModel addChild(IInstance child) {
        if (child instanceof DataModel) {
            addData((DataModel<ConnectorTypeModel>) child);
        }
        if (child instanceof InteractionModel) {
            addInteraction((InteractionModel) child);
        }
        return this;
    }

    public void addInteraction(InteractionModel interaction) {
        interactions.add(interaction);
        firePropertyChange(CHILDREN);
    }

    public void addData(DataModel child) {
        datas.add(child);
        firePropertyChange(CHILDREN);
    }

    public boolean removeInteraction(InteractionModel interaction) {
        boolean result = interactions.remove(interaction);
        firePropertyChange(CHILDREN);
        return result;
    }

    public boolean removeData(DataModel<ConnectorTypeModel> data) {
        boolean result = datas.remove(data);
        firePropertyChange(CHILDREN);
        return result;
    }

    /**
     * 根据名字删除变量
     * 
     * @param name
     * @return
     */
    public DataModel<ConnectorTypeModel> removeDataByName(String name) {
        Iterator<DataModel<ConnectorTypeModel>> it = datas.iterator();
        while (it.hasNext()) {
            DataModel<ConnectorTypeModel> data = it.next();
            if (data.getName().equals(name)) {
                it.remove();
                return data;
            }
        }
        return null;
    }

    /**
     * 根据名字删除参数
     * 
     * @param name
     * @return
     */
    public boolean removeArgumentByName(String name) {
        Iterator<ArgumentEntry> it = arguments.iterator();
        while (it.hasNext()) {
            ArgumentEntry entry = it.next();
            if (entry.getName().equals(name)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * 根据交互端口字符串删除交互
     * 
     * @param name
     * @return
     */
    public boolean removeInteractionByName(String name) {
        Iterator<InteractionModel> it = interactions.iterator();
        while (it.hasNext()) {
            InteractionModel interaction = it.next();
            if (interaction.getInteractionString().equals(name)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    public InteractionModel getInteractionByName(String name) {
        Iterator<InteractionModel> it = interactions.iterator();
        while (it.hasNext()) {
            InteractionModel interaction = it.next();
            if (interaction.getInteractionString().equals(name)) {
                return interaction;
            }
        }
        return null;
    }

    public DataModel getDataByName(String name) {
        for (DataModel<ConnectorTypeModel> data : datas) {
            if (data.getName().equals(name)) {
                return data;
            }
        }
        return null;
    }

    public boolean moveArgumentForward(int index) {
        if (index <= 0 || index >= arguments.size()) {
            return false;
        }
        arguments.add(index - 1, arguments.remove(index));
        return true;
    }

    public boolean moveArgumentBackward(int index) {
        if (index < 0 || index >= arguments.size() - 1) {
            return false;
        }
        arguments.add(index + 1, arguments.remove(index));
        return true;
    }

    public boolean moveInteractionForward(int index) {
        if (index <= 0 || index >= interactions.size()) {
            return false;
        }
        interactions.add(index - 1, interactions.remove(index));
        return true;
    }

    public boolean moveInteractionBackward(int index) {
        if (index < 0 || index >= interactions.size() - 1) {
            return false;
        }
        interactions.add(index + 1, interactions.remove(index));
        return true;
    }

    public List<String> getArgumentNames() {
        ArrayList<String> list = new ArrayList<String>();
        for (ArgumentEntry entry : arguments) {
            list.add(entry.getName());
        }
        return list;
    }

    public InteractionModel removeInteraction(int index) {
        return interactions.remove(index);
    }

    /**
     * 检查Connector里面是否有重名的参数或者变量
     * 
     * @param name
     * @return
     */
    public boolean nameExistsInConnector(String name) {
        for (DataModel<ConnectorTypeModel> data : datas) {
            if (data.getName().equals(name)) {
                return true;
            }
        }
        for (ArgumentEntry entry : arguments) {
            if (entry.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查Connector里面是否有重复的Interaction
     * 
     * @param name
     * @return
     */
    public boolean interactionNameExistsInConnector(String name) {
        for (InteractionModel interaction : interactions) {
            if (interaction.getInteractionString().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public InteractionModel addInteraction(List<String> portArguments, String upAction,
                    String downAction) {
        List<ArgumentEntry> ports = new ArrayList<ArgumentEntry>();
        for (ArgumentEntry entry : arguments) {
            if (portArguments.contains(entry.getName())) {
                ports.add(entry);
            }
        }
        InteractionModel model =
                        new InteractionModel(new ActionModel().setAction(upAction),
                                        new ActionModel().setAction(downAction), ports);
        addInteraction(model);
        return model;
    }

    public InteractionModel updateInteraction(String oldPorts, List<String> portArguments,
                    String upAction, String downAction) {
        List<ArgumentEntry> ports = new ArrayList<ArgumentEntry>();
        for (ArgumentEntry entry : arguments) {
            if (portArguments.contains(entry.getName())) {
                ports.add(entry);
            }
        }
        Iterator<InteractionModel> it = interactions.iterator();
        while (it.hasNext()) {
            InteractionModel interaction = it.next();
            if (interaction.getInteractionString().equals(oldPorts)) {
                interaction.setInteractionPorts(ports);
                interaction.getUpAction().setAction(upAction);
                interaction.getDownAction().setAction(downAction);
                return interaction;
            }
        }
        return null;
    }

    /**
     * 解析 define [p1' p2]' p3 样式的语句
     * 
     * @param string [p1' p2]' p3 样式的语句
     * 
     * @return 模型本身，用于串接调用
     */
    public ConnectorTypeModel parseInteractor(String string) {
        string = String.format("[%s]", string);
        Stack<Object> stack = new Stack<Object>();
        List<Interactor> list = new LinkedList<Interactor>();
        HashMap<String, ArgumentEntry> portTypeHashMap = new HashMap<String, ArgumentEntry>();
        for (ArgumentEntry entry : this.arguments) {
            portTypeHashMap.put(entry.getName(), entry);
        }
        int i = 0;
        int size = string.length();
        while (i < size) {
            char c = string.charAt(i);
            if (c == '[') {
                stack.push("[");
            } else if (c == ']') {
                Interactor completePort = null;
                while (true) {
                    Object obj = stack.pop();
                    if (obj.equals("[")) {
                        break;
                    }
                    if (obj.equals("'")) {
                        if (stack.peek() instanceof Interactor) {
                            completePort = (Interactor) stack.pop();
                            if (!stack.peek().equals("[")) {
                                throw new IllegalArgumentException(string);
                            }
                        } else {
                            throw new IllegalArgumentException(string);
                        }
                    } else if (obj instanceof Interactor) {
                        list.add(0, (Interactor) obj);
                    }
                }
                Interactor interactor = new Interactor(completePort, list);
                stack.push(interactor);
            } else if (c == '\t' || c == ' ') {
                i++;
                continue;
            } else if (c == '\'') {
                stack.push("'");
            } else if (isIdentifierStart(c)) {
                int j = i + 1;
                while (j < size && isIdentifierPart(string.charAt(j))) {
                    j++;
                }
                String identifier = string.substring(i, j);
                ArgumentEntry portType = portTypeHashMap.get(identifier);
                if (portType == null) {
                    throw new NullPointerException(identifier);
                }
                stack.push(new Interactor(portType));
                i = j - 1;
            }
            i++;
        }
        if (stack.size() != 1 || !(stack.peek() instanceof Interactor)) {
            throw new IllegalArgumentException(string);
        }
        this.interactor = (Interactor) stack.pop();
        return this;
    }

    private boolean isIdentifierStart(char c) {
        return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isIdentifierPart(char c) {
        return isIdentifierStart(c) || (c >= '0' && c <= '9');
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public ConnectorTypeModel addParameter(PortTypeModel child) {
        return setParameter(child, arguments.size());
    }

    public ConnectorTypeModel setParameter(PortTypeModel child, int index) {
        setOrderModelChild(child, index);
        return this;
    }

    @Override
    public boolean removeChild(IInstance iInstance) {
        if (iInstance instanceof DataModel) {
            return removeData((DataModel<ConnectorTypeModel>) iInstance);
        } else if (iInstance instanceof InteractionModel) {
            return removeInteraction((InteractionModel) iInstance);
        }
        return false;
    }

    @Override
    public void setOrderModelChild(PortTypeModel child, int index) {
        arguments.get(index).setModel(child);
        firePropertyChange(CHILDREN);
    }

    @Override
    public boolean allSets() {
        for (ArgumentEntry child : arguments) {
            if (!child.isBounded()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PortTypeModel removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        return arguments.remove(index).getModel();
    }

    @Override
    public List<PortTypeModel> getOrderList() {
        ArrayList<PortTypeModel> list = new ArrayList<PortTypeModel>();
        for (ArgumentEntry entry : arguments) {
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
    public void setPropertyValue(Object id, Object value) {}

    @Override
    public boolean isNewNameAlreadyExistsInParent(IInstance child, String newName) {
        for (IInstance instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, List<DataModel>> getDatasGroupByType() {
        HashMap<String, List<DataModel>> map = new HashMap<String, List<DataModel>>();
        for (DataModel<ConnectorTypeModel> data : datas) {
            String typeName = data.getType().getName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<DataModel>());
            }
            map.get(typeName).add(data);
        }
        return map;
    }
}


@SuppressWarnings({"unused", "rawtypes"})
@Root
class Interactor implements Serializable {

    @Element(required = false, name = "content")
    private ArgumentEntry content;
    @Element(required = false, name = "trigger")
    private Interactor    completePort;
    @ElementArray(required = false, name = "ports")
    private Interactor[]  incompletePorts;

    public Interactor(@Element(name = "content") ArgumentEntry content) {
        this.content = content;
        this.completePort = null;
        this.incompletePorts = null;
    }

    public Interactor(@Element(name = "trigger") Interactor completePort,
                    @ElementArray(name = "ports") Interactor... incompletePorts) {
        this.completePort = completePort;
        this.incompletePorts = incompletePorts;
    }

    public Interactor(Interactor completePort, List<Interactor> incompletePorts) {
        this.completePort = completePort;
        this.incompletePorts = incompletePorts.toArray(new Interactor[incompletePorts.size()]);
    }

    public String toString() {
        if (content != null) {
            return content.getName();
        }
        StringBuilder buffer = new StringBuilder("[");
        if (completePort != null) {
            buffer.append(completePort).append("\' ");
        }
        if (incompletePorts != null && !(incompletePorts.length > 0)) {
            for (Interactor interactor : incompletePorts) {
                buffer.append(interactor).append(' ');
            }
        }
        return buffer.append(']').toString();
    }
}
