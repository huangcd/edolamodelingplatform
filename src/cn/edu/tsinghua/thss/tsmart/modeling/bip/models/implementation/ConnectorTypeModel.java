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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.UnboundedException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:43<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root(name = "connectorType")
public class ConnectorTypeModel
                extends BaseTypeModel<ConnectorTypeModel, ConnectorModel, ITopModel>
                implements
                    IDataContainer<ConnectorTypeModel, ConnectorModel, ITopModel, IInstance>,
                    IOrderContainer<PortTypeModel> {
    private static final long                                                        serialVersionUID =
                                                                                                                      -7637868341840412974L;
    private final static HashMap<String, ConnectorTypeModel>                         typeSources;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    static {
        typeSources = new HashMap<String, ConnectorTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();

        ConnectorTypeModel singleton = new ConnectorTypeModel().setName("singleton");
        singleton.addArgument(PortTypeModel.getModelByName("ePort"), "p1");
        singleton.parseInteractor("p1");
        singleton.addInteraction(Arrays.asList("p1"), "", "");

        ConnectorTypeModel rendezvous = new ConnectorTypeModel().setName("rendezvous");
        rendezvous.addArgument(PortTypeModel.getModelByName("ePort"), "p1");
        rendezvous.addArgument(PortTypeModel.getModelByName("ePort"), "p2");
        rendezvous.parseInteractor("p1 p2");
        rendezvous.addInteraction(Arrays.asList("p1", "p2"), "", "");

        addTypeSources(singleton.getName(), singleton);
        addTypeSources(rendezvous.getName(), rendezvous);
    }

    public static Collection<ConnectorTypeModel> getAllRegisterTypes() {
        return Collections.unmodifiableCollection(typeSources.values());
    }

    public static void saveTypes() {
        saveTypes(Activator.getPreferenceDirection());
    }

    public static void loadTypes() {
        loadTypes(Activator.getPreferenceDirection());
    }

    public static void clearTypes() {
        Set<String> typeNames = new HashSet<String>(typeSources.keySet());
        for (String type : typeNames) {
            removeType(type);
        }
    }

    public static void saveTypes(File directory) {
        File file = new File(directory, GlobalProperties.CONNECTOR_TYPE_FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (Map.Entry<String, ConnectorTypeModel> entry : getTypeEntries()) {
                ConnectorTypeModel port = entry.getValue();
                ITopModel parent = port.getParent();
                port.setParent(null);
                out.writeObject(entry.getValue());
                out.flush();
                port.setParent(parent);
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadTypes(File directory) {
        File file = new File(directory, GlobalProperties.CONNECTOR_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                ConnectorTypeModel model = (ConnectorTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void addType(ConnectorTypeModel model) {
        String type = model.getName();
        addTypeSources(type, model);
        HashMap<CompoundEditor, CreationToolEntry> map =
                        new HashMap<CompoundEditor, CreationToolEntry>();
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "新建一个" + type + "连接子", new CopyFactory(
                                            model), BIPEditor.getImage("icons/connector_16.png"),
                                            BIPEditor.getImage("icons/connector_32.png"));
            editor.addConnectorCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    private static boolean addTypeSources(String type, ConnectorTypeModel connector) {
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

    protected static void removeType(String type) {
        if (removeTypeSources(type)) {
            HashMap<CompoundEditor, CreationToolEntry> map = toolMap.get(type);
            for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
                editor.removeConnectorCreationToolEntry(map.get(editor));
            }
            toolMap.remove(type);
        }
    }

    public static boolean isRemovable(String type) {
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CompoundTypeModel model = (CompoundTypeModel) editor.getModel();
            Map<String, List<ConnectorModel>> map = model.getConnectorsGroupByType();
            List<ConnectorModel> datas = map.get(type);
            if (datas != null && datas.size() > 0) {
                return false;
            }
        }
        return true;
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

    public static ConnectorTypeModel getModelByName(String type) {
        return typeSources.get(type).copy();
    }

    static class ArgumentEntry implements Serializable {
        private static final long serialVersionUID = 9130376378631319683L;
        @Element
        private int               index;
        @Element
        private boolean           bounded          = false;
        @Element
        private String            name;
        @Element
        private PortTypeModel     model;

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

        protected void bound(PortTypeModel model, ConnectorTypeModel parent) {
            this.model = model;
            this.bounded = true;
        }

        public void unbound() {
            this.bounded = false;
            this.model.setParent(null);
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
        setPort((PortModel) PortTypeModel.getModelByName("ePort").getInstance());
    }

    /*
     * true = 纯硬件 connector
     */
    public boolean checkOnlyHardwarePorts() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return true;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;
        Set<PortModel> set = getAllRelativePorts();
        for (PortModel port : set) {
            if (port.getParent() instanceof AtomicTypeModel) {
                if (port.getParent().getEntityNames().contains(projectModel.getSoftwareEntity()))
                    return false;
            }
        }
        return true;
    }

    /*
     * true = 纯软件 connector
     */
    public boolean checkOnlySoftwarePorts() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return true;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;
        Set<PortModel> set = getAllRelativePorts();
        for (PortModel port : set) {
            if (port.getParent() instanceof AtomicTypeModel) {
                if (port.getParent().getEntityNames().contains(projectModel.getHardwareEntity()))
                    return false;
            }
        }
        return true;
    }

    /*
     * true = 同时有硬软 connector
     */
    public boolean checkSoftwareHardwarePorts() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return true;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;
        boolean soft = false;
        boolean hard = false;

        Set<PortModel> set = getAllRelativePorts();

        for (PortModel port : set) {
            if (port.getParent() instanceof AtomicTypeModel) {
                if (port.getParent().getEntityNames().contains(projectModel.getSoftwareEntity()))
                    soft = true;
                if (port.getParent().getEntityNames().contains(projectModel.getHardwareEntity()))
                    hard = true;
                if (soft && hard) return true;
            }
        }

        return false;
    }

    public Set<PortModel> getAllRelativePorts() {
        HashSet<PortModel> set = new HashSet<PortModel>();
        for (ArgumentEntry entry : arguments) {
            if (entry.isBounded()) {
                PortModel port = entry.getModel().getInstance();
                if (port.getParent() instanceof ConnectorTypeModel) {
                    set.addAll(((ConnectorTypeModel) port.getParent()).getAllRelativePorts());
                } else if (port.getParent() instanceof AtomicTypeModel) {
                    set.add(port);
                } else {
                    throw new EdolaModelingException(
                                    "Parent of port are neither ConnectorTypeModel nor AtomicTypeModel");
                }
            } else {
                throw new UnboundedException("Connector has an unbounded port");
            }
        }
        return set;
    }

    protected List<ArgumentEntry> getArgumentEntries() {
        return arguments;
    }

    public PortModel getPort() {
        return port;
    }

    public ConnectorTypeModel setPort(PortModel port) {
        this.port = port;
        this.port.setName("p");
        this.port.setParent(this);
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

    /**
     * 返回端口参数，如果参数没有被绑定，直接返回null。
     * 
     * @param index
     * @return
     */
    public PortTypeModel getPortArgument(int index) {
        ArgumentEntry entry = arguments.get(index);
        if (entry.isBounded()) {
            return entry.getModel();
        }
        return null;
    }

    /**
     * 返回端口参数的长度
     * 
     * @return
     */
    public int getArgumentSize() {
        return arguments.size();
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

    public String getArgumentType(int index) {
        return arguments.get(index).getTypeName();
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
        buffer.append("\t\tdefine ").append(interactor).append('\n');
        for (DataModel<ConnectorTypeModel> data : datas) {
            buffer.append("\t\t").append(data.exportToBip()).append('\n');
        }
        for (InteractionModel interaction : interactions) {
            buffer.append("\t\t").append(interaction.exportToBip()).append('\n');
        }
        if (port.isExport()) {
            buffer.append("\t\texport port ").append(port.getType().getName()).append(' ')
                            .append(port.getName());
        }

        if (!port.getBoundedArguments().equals("")) {
            buffer.append('(').append(port.getBoundedArguments()).append(")\n");
        }

        buffer.append("\tend");
        return buffer.toString();
    }

    /*
     * 代码生成相关，导出Bip模型
     * 
     * 需要删除链接硬件组件的port
     * 
     * TODO 还没完成 1）删除硬件port 2）如果同步事件只有硬件port的，则删除 3）删除重复例如，p1,p1p2，删除p2后，p1 p1重复了，需要删除一个 4）删除所有up
     * down 动作
     * 
     * 顺序需要保证一致，类型和实例
     * 
     * 确认是否只要保留define块？
     */

    private String hardwareCutName;

    public String getHardwareCutName() {
        return hardwareCutName;
    }

    public void setHardwareCutName(String newName) {
        hardwareCutName = newName;
    }

    public String exportToBipforCodeGen() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return "";
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "";
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        StringBuilder buffer = new StringBuilder();
        String interactors = "";
        // 使用新name
        buffer.append("connector type ").append(getHardwareCutName()).append('(');
        if (arguments != null && !arguments.isEmpty()) {
            for (ArgumentEntry entry : arguments) {
                if (entry.getModel().getInstance() != null)
                    if (!entry.getModel().getInstance().getParent().getEntityNames()
                                    .contains(projectModel.getHardwareEntity())) {// 不是硬件才加上
                        interactors = interactors + entry.getName() + " ";
                        buffer.append(entry.getTypeName()).append(' ').append(entry.getName())
                                        .append(", ");
                    }
            }
            // 因为肯定有软件的port，所以这里-2是安全的
            buffer.replace(buffer.length() - 2, buffer.length(), "");
        }
        buffer.append(")\n");
        // 完成功能2 3 XXX
        // buffer.append("\t\tdefine [").append(interactors).append("]\n");
        buffer.append("\t\tdefine ").append(interactors).append("\n");
        buffer.append("\t\ton ").append(interactors).append("\n\t\t\tup{}\n\t\t\tdown{}\n");


        /*
         * 连接子中不能有 data for (DataModel<ConnectorTypeModel> data : datas) {
         * buffer.append("\t\t").append(data.exportToBip()).append('\n'); }
         */
        // 因为connector必须没有动作，所有action忽略
        /*
         * for (InteractionModel interaction : interactions) {
         * buffer.append("\t\t").append(interaction.exportToBipforCodeGen()).append('\n'); }
         */
        // 顶层模型不用export
        /*
         * buffer.append("\t\texport port ").append(port.getType().getName()).append(' ')
         * .append(port.getName()).append('(').append(port.getBoundedArguments()) .append(")\n");
         */
        buffer.append("\tend");
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
        for (IInstance instance : list) {
            if (instance.getParent() == null || !instance.getParent().equals(this)) {
                instance.setParent(this);
            }
        }
        return list;
    }

    protected ConnectorTypeModel bound(int index, PortTypeModel model) {
        arguments.get(index).bound(model, this);
        getInstance().firePropertyChange(CHILDREN);
        return this;
    }

    protected ConnectorTypeModel unbound(int index) {
        arguments.get(index).unbound();
        getInstance().firePropertyChange(CHILDREN);
        return this;
    }

    @Override
    public ConnectorTypeModel addChild(IInstance child) {
        ensureUniqueName(child);
        if (child instanceof DataModel) {
            addData((DataModel<ConnectorTypeModel>) child);
        }
        if (child instanceof InteractionModel) {
            addInteraction((InteractionModel) child);
        }
        return this;
    }

    /**
     * ConnectorTypeModel不需要parent
     */
    public ConnectorTypeModel setParent(ITopModel parent) {
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

    @Override
    public void ensureUniqueName(IInstance child) {
        HashSet<String> names = new HashSet<String>();
        String name = child.getName();
        for (IInstance model : getChildren()) {
            names.add(model.getName());
        }
        while (names.contains(name)) {
            Matcher mat = NAME_PREFIX.matcher(name);
            if (mat.matches()) {
                String baseName = mat.group(1);
                String number = mat.group(2);
                name = baseName + "" + (Integer.parseInt(number) + 1);
            } else {
                name = name + "1";
            }
            child.setName(name);
        }
    }
}


@SuppressWarnings({"unused", "rawtypes"})
@Root
class Interactor implements Serializable {

    private static final long serialVersionUID = -5340423309780663860L;
    @Element(required = false, name = "content")
    private ArgumentEntry     content;
    @Element(required = false, name = "trigger")
    private Interactor        completePort;
    @ElementArray(required = false, name = "ports")
    private Interactor[]      incompletePorts;

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
        StringBuilder buffer = new StringBuilder("");
        if (completePort != null) {
            buffer.append(completePort).append("\' ");
        }
        if (incompletePorts != null && (incompletePorts.length > 0)) {
            for (Interactor interactor : incompletePorts) {
                buffer.append(interactor).append(' ');
            }
        }
        return buffer.append("").toString();
    }
}
