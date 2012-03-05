package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Entity;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.IncompleteModelException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午9:15<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class CompoundTypeModel extends ComponentTypeModel<CompoundTypeModel, CompoundModel> {

    private final static Pattern                                                     atomicNamePattern;
    private final static Pattern                                                     compoundNamePattern;
    private static final long                                                        serialVersionUID =
                                                                                                                      4875759382399971421L;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    private final static HashMap<String, CompoundTypeModel>                          typeSources;

    static {
        atomicNamePattern = Pattern.compile("^atomic(\\d*)$");
        compoundNamePattern = Pattern.compile("^compound(\\d*)$");
        typeSources = new HashMap<String, CompoundTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();
    }

    public static void addToolEntry(String type, CompoundEditor editor, CreationToolEntry entry) {
        if (!toolMap.containsKey(type)) {
            toolMap.put(type, new HashMap<CompoundEditor, CreationToolEntry>());
        }
        toolMap.get(type).put(editor, entry);
    }

    public static void clearTypes() {
        Set<String> typeNames = new HashSet<String>(typeSources.keySet());
        for (String type : typeNames) {
            removeType(type);
        }
    }

    public static Collection<CompoundTypeModel> getAllRegisterTypes() {
        return Collections.unmodifiableCollection(typeSources.values());
    }

    public static CompoundTypeModel getModelByName(String type) {
        return typeSources.get(type).copy();
    }

    public static Set<Map.Entry<String, CompoundTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> array = new ArrayList<String>(typeSources.keySet());
        Collections.sort(array);
        return array.toArray(new String[array.size()]);
    }

    public static Set<String> getTypes() {
        return typeSources.keySet();
    }

    public static boolean isRemovable(String selection) {
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CompoundTypeModel model = (CompoundTypeModel) editor.getModel();
            /*
             * if(model.getName().equals(selection)) return false;
             */
            ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
            ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
            model.getAllComponent(compounds, atomics);// compounds包括了model本身
            compounds.remove(compounds.size() - 1);// 刪除model本身
            for (CompoundTypeModel compound : compounds) {
                if (compound.getName().equals(selection)) return false;
            }
        }
        return true;
    }

    protected static void addType(CompoundTypeModel model) {
        String type = model.getName();
        if (!addTypeSources(type, model)) {
            MessageUtil.addProblemWarningMessage("已存在同名组件" + type);
            return;
        }
        HashMap<CompoundEditor, CreationToolEntry> map =
                        new HashMap<CompoundEditor, CreationToolEntry>();
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, model.getComment(), new CopyFactory(model),
                                            BIPEditor.getImage("icons/compound_16.png"),
                                            BIPEditor.getImage("icons/compound_32.png"));
            editor.addCompoundCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    protected static void removeType(String type) {
        if (removeTypeSources(type)) {
            HashMap<CompoundEditor, CreationToolEntry> map = toolMap.get(type);
            for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
                editor.removeCompoundCreationToolEntry(map.get(editor));
            }
            toolMap.remove(type);
        }
    }

    private static boolean addTypeSources(String type, CompoundTypeModel connector) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, connector);
        return true;
    }

    private static boolean removeTypeSources(String type) {
        if (!typeSources.containsKey(type)) {
            return false;
        }
        typeSources.remove(type);
        return true;
    }

    private List<InvisibleBulletModel>             bullets;

    private List<DiamondModel>                     diamonds;

    @ElementList
    private List<ComponentModel>                   components;

    @ElementList
    private List<ConnectorModel>                   connectors;

    @ElementList
    private List<PriorityModel<CompoundTypeModel>> priorities;

    public List<PriorityModel<CompoundTypeModel>> getPriorities() {
        return priorities;
    }

    public CompoundTypeModel() {
        components = new ArrayList<ComponentModel>();
        connectors = new ArrayList<ConnectorModel>();
        priorities = new ArrayList<PriorityModel<CompoundTypeModel>>();
        bullets = new ArrayList<InvisibleBulletModel>();
        diamonds = new ArrayList<DiamondModel>();
    }

    @Override
    public CompoundTypeModel addChild(IInstance child) {
        ensureUniqueName(child);
        child.setParent(this);
        if (child instanceof ComponentModel) {
            addComponent((ComponentModel) child);
        } else if (child instanceof ConnectorModel) {
            addConnector((ConnectorModel) child);
        } else if (child instanceof PriorityModel) {
            addPriority((PriorityModel) child);
        } else if (child instanceof InvisibleBulletModel) {
            addBullet((InvisibleBulletModel) child);
        } else if (child instanceof DiamondModel) {
            addDiamond((DiamondModel) child);
        } else {
            System.err.println("Invalidated child type to add:\n" + child);
        }
        return this;
    }

    private void addDiamond(DiamondModel child) {
        if (diamonds == null) {
            diamonds = new ArrayList<DiamondModel>();
        }
        diamonds.add(child);
        firePropertyChange(CHILDREN);
    }

    public void deleteRelatedPrioritiesWhenDeleteConnector(ConnectorModel con) {
        Iterator<PriorityModel<CompoundTypeModel>> iter = priorities.iterator();
        while (iter.hasNext()) {
            PriorityModel<CompoundTypeModel> model = iter.next();
            if (model.getLeftPort().equals(con) || model.getRightPort().equals(con)) iter.remove();
        }
    }

    /*
     * 基准线合法性相关
     */
    public boolean checkValidateBaseline() {
        List<Entity> entities = GlobalProperties.getInstance().getEntities();

        for (Object s : getEntityNames()) {
            boolean contain = false;
            for (Entity en : entities) {
                if (en.getName().equals(s)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) return false;
        }

        for (IInstance child : getChildren()) {
            for (Object s : child.getEntityNames()) {
                boolean contain = false;
                for (Entity en : entities) {
                    if (en.getName().equals(s)) {
                        contain = true;
                        break;
                    }
                }
                if (!contain) return false;
            }

            if (child instanceof CompoundModel) {
                if (!((CompoundModel) child).getType().checkValidateBaseline()) return false;
            }
            if (child instanceof AtomicModel) {
                if (!((AtomicModel) child).getType().checkValidateBaseline()) return false;
            }
        }

        return true;
    }

    public void cleanEntityNames() {
        List<Entity> entities = GlobalProperties.getInstance().getEntities();

        ArrayList<String> entitiesNames = new ArrayList<String>(getEntityNames());

        for (Object s : entitiesNames) {
            boolean contain = false;
            for (Entity en : entities) {
                if (en.getName().equals(s)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                getEntityNames().remove(s);
            }
        }

        for (IInstance child : getChildren()) {
            entitiesNames = new ArrayList<String>(child.getEntityNames());

            for (Object s : entitiesNames) {
                boolean contain = false;
                for (Entity en : entities) {
                    if (en.getName().equals(s)) {
                        contain = true;
                        break;
                    }
                }
                if (!contain) {
                    child.getEntityNames().remove(s);
                }
            }
            if (child instanceof CompoundModel) {
                ((CompoundModel) child).getType().checkValidateBaseline();
            }
            if (child instanceof AtomicModel) {
                ((AtomicModel) child).getType().checkValidateBaseline();
            }
        }
    }

    /*
     * 给需要重命名的connector type新名字
     * 
     * TODO 需要测试
     */
    public void buildNewNamesforConnectors() {
        ArrayList<ConnectorModel> consNeedNewName = new ArrayList<ConnectorModel>();
        HashSet<String> typeNames = new HashSet<String>();

        for (ConnectorModel con : connectors) {
            if (con.getType().checkSoftwareHardwarePorts()) {// 软硬都有，需要重命名
                consNeedNewName.add(con);
                con.getType().setHardwareCutName(con.getType().getName());// 初始化新name为老name
            }
        }

        // 添加所有connector type的name
        for (Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
            typeNames.add(entry.getValue().getName());
        }

        Pattern NAME_PREFIX = Pattern.compile("^(.*)_(\\d+)$");

        for (ConnectorModel con : consNeedNewName) {
            String name = con.getType().getHardwareCutName();
            while (typeNames.contains(name)) {
                Matcher mat = NAME_PREFIX.matcher(name);
                if (mat.matches()) {
                    String baseName = mat.group(1);
                    String number = mat.group(2);
                    name = baseName + "_" + (Integer.parseInt(number) + 1);
                } else {
                    name = name + "_1";
                }
            }
            con.getType().setHardwareCutName(name);
            typeNames.add(name);
        }
    }

    public boolean checkAllTickSync() {
        boolean result = true;
        int tickConnectorNum = 0;
        Set<PortModel> ports = new HashSet<PortModel>();
        Set<PortModel> allPorts = new HashSet<PortModel>();

        for (IInstance child : getChildren()) {
            if (child instanceof ConnectorModel) {
                boolean contain = false;

                ports = (((ConnectorModel) child).getType().getAllRelativePorts());
                for (PortModel port : ports) {
                    if (port.isMarkedTick()) {
                        contain = true;
                        break;
                    }
                }
                if (contain) {
                    tickConnectorNum++;
                    allPorts = ports;
                }
            }
        }

        if (tickConnectorNum > 1) {

            String errMessage = String.format("所有标注“tick”的端口必须全同步。");
            MessageUtil.addProblemWarningMessage(errMessage);

            result = false;
        } else {
            result = checkAllTickSyncSub(allPorts) && result;
        }

        return result;
    }

    public boolean checkAllTickSyncSub(Set<PortModel> tickPorts) {
        boolean result = true;

        for (IInstance child : getChildren()) {
            if (child instanceof CompoundModel) {
                if (!((CompoundModel) child).getType().checkAllTickSyncSub(tickPorts)) {
                    result = false;
                }
            }
            if (child instanceof AtomicModel) {
                if (!((AtomicModel) child).getType().checkAllTickSyncSub(tickPorts)) {
                    result = false;
                }
            }
        }

        return result;
    }

    public boolean checkCodeGenValid() {
        MessageUtil.clearProblemMessage();
        if (!checkIOPortNames()) return false;
        if (!checkEntityMappings()) return false;
        if (!checkEntityTagHWorSW()) return false;
        if (!checkOnlyContainsHWorSW()) return false;
        if (!checkOnlyContainTick()) return false;
        if (!checkAllTickSync()) return false;
        if (!checkOnlyContainIO()) return false;
        if (!checkPartitionBreakIOandTickValid()) return false;
        return true;
    }

    /*
     * 针对代码生成，如果模型打标正确，则返回true，否则为false 打标正确的含义如下： 0)所有4个entity均有映射：硬件，软件，io，tick
     * 1）所有的原子组件都必须有硬件或者软件的标，不能同时具有 2）如果复合组件不是顶层组件，则该组件不能同时包括打上硬件标的原子组件和软件标的原子组件
     * 3）包含tick的连接子，只能包含tick 4）所有的tick都必须全同步 5）io端口只能和io端口同步 6)软硬件划分不能破坏 IO 和 tick
     * 之外的port的连接关系（因为条件2很强，所以这个检查可以简化：只需要检查顶层复合组件的连接子即可）7）IO port的的名称只能为io
     */
    public boolean checkEntityMappings() {
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
        if (projectModel.getHardwareEntity().equals("")
                        || projectModel.getSoftwareEntity().equals("")
                        || projectModel.getIoEntity().equals("")
                        || projectModel.getTickEntity().equals("")) {

            String errMessage = String.format("软件、硬件、IO、tick必须均绑定一个实体。");
            MessageUtil.addProblemWarningMessage(errMessage);
            return false;
        }
        return true;
    }

    public boolean checkEntityTagHWorSW() {
        boolean result = true;
        for (IInstance child : getChildren()) {
            if (child instanceof AtomicModel) {
                if (((AtomicModel) child).getType().getHardwareSoftwareType().equals("")) {
                    result = false;

                    String errMessage =
                                    String.format("原子组件类型 %s 同时被标注上“软件”和“硬件”实体或者没有标注。", child
                                                    .getType().getName());
                    MessageUtil.addProblemWarningMessage(errMessage);
                }
            }
            if (child instanceof CompoundModel) {
                result = ((CompoundModel) child).getType().checkEntityTagHWorSW() && result;
            }
        }
        return result;
    }

    /*
     * 检查是否存在名字为name的子组件（名称示例为：model.model.speed）
     */
    public boolean checkExistenceByName(String name) {
        if (name.equals("")) return false;

        String[] names = name.split("\\.", 2);

        if (names.length != 2) return false;

        for (IInstance instance : getChildren()) {
            if (instance instanceof CompoundModel && instance.getName().equals(names[0])) {
                return ((CompoundModel) instance).getType().checkExistenceByName(names[1]);
            }
            if (instance instanceof AtomicModel && instance.getName().equals(names[0])) {
                return ((AtomicModel) instance).getType().checkExistenceByName(names[1]);
            }
        }
        return false;
    }

    public boolean checkIOPortNames() {
        boolean result = true;

        for (IInstance child : getChildren()) {
            if (child instanceof AtomicModel) {
                result = ((AtomicModel) child).getType().checkIOPortNames() && result;
            }
            if (child instanceof CompoundModel) {
                result = ((CompoundModel) child).getType().checkIOPortNames() && result;
            }
        }
        return result;
    }

    public boolean checkOnlyContainIO() {
        boolean result = true;

        for (IInstance child : getChildren()) {
            if (child instanceof ConnectorModel) {
                boolean contain = false;

                Set<PortModel> ports = (((ConnectorModel) child).getType().getAllRelativePorts());
                for (PortModel port : ports) {
                    if (port.isMarkedIO()) {
                        contain = true;
                        break;
                    }
                }
                if (contain)
                    for (PortModel port : ports) {
                        if (!port.isMarkedIO()) {

                            String errMessage =
                                            String.format("连接子 %s 中已有端口被标注上“IO”实体，但是其下端口 %s 没有标注“IO”实体。",
                                                            child.getName(), port.getName());
                            MessageUtil.addProblemWarningMessage(errMessage);
                            result = false;
                        }
                    }
            }
            if (child instanceof CompoundModel) {
                result = (((CompoundModel) child).getType().checkOnlyContainIO()) && result;
            }
        }

        return result;
    }

    public boolean checkOnlyContainsHWorSW() {
        // 只检查子复合组件
        boolean result = true;
        for (IInstance child : getChildren()) {
            if (child instanceof CompoundModel) {
                if (((CompoundModel) child).getType().getHardwareSoftwareType().equals("")) {
                    String errMessage =
                                    String.format("复合组件类型 %s 中有类型冲突，或者其下不包含任何原子组件。", child
                                                    .getType().getName());
                    MessageUtil.addProblemWarningMessage(errMessage);
                    result = false;
                }
            }
        }
        return result;
    }

    public boolean checkOnlyContainTick() {
        boolean result = true;

        for (IInstance child : getChildren()) {
            if (child instanceof ConnectorModel) {
                boolean contain = false;

                Set<PortModel> ports = (((ConnectorModel) child).getType().getAllRelativePorts());
                for (PortModel port : ports) {
                    if (port.isMarkedTick()) {
                        contain = true;
                        break;
                    }
                }
                if (contain)
                    for (PortModel port : ports) {
                        if (!port.isMarkedTick()) {
                            String errMessage =
                                            String.format("连接子 %s 中已有端口被标注上“tick”实体，但是其下端口 %s 没有标注“tick”实体。",
                                                            child.getName(), port.getName());
                            MessageUtil.addProblemWarningMessage(errMessage);
                            result = false;
                        }
                    }
            }
            if (child instanceof CompoundModel) {
                result = (((CompoundModel) child).getType().checkOnlyContainTick()) && result;
            }
        }

        return result;
    }

    public boolean checkPartitionBreakIOandTickValid() {
        // 这个检查必须在 checkOnlyContainsHWorSW() 为真时进行
        if (!checkOnlyContainsHWorSW()) return false;

        boolean result = true;

        for (IInstance child : getChildren()) {// 因为条件2的约束，只要检查一层就可以了
            if (child instanceof ConnectorModel) {
                boolean hardware = false;
                boolean software = false;

                Set<PortModel> ports = ((ConnectorModel) child).getType().getAllRelativePorts();

                for (PortModel port : ports) {
                    if (!port.isMarkedIO() && !port.isMarkedTick()) {// 不是io和tick才考虑
                        if (((AtomicTypeModel) port.getParent()).isMarkedHareware()) {// port所在的组件是硬件
                            hardware = true;
                        }
                        if (((AtomicTypeModel) port.getParent()).isMarkedSoftware()) {// port所在的组件是软件
                            software = true;
                        }
                    }
                }
                if (hardware && software) {// 一个连接子，不是io和tick，但是同时有硬件和软件，被划分开了：错误

                    String errMessage =
                                    String.format("连接子 %s 所涉及端口被软硬件分割。只有“tick”和“IO”连接子才能被软硬件分割。",
                                                    child.getName());
                    MessageUtil.addProblemWarningMessage(errMessage);


                    result = false;
                }
            }
        }

        return result;
    }

    @Override
    public CompoundModel createInstance() {
        if (instance == null) {
            instance = new CompoundModel().setType(this);
        }
        return instance;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    void getAllUsedPortAndConnectorType(HashMap<String, PortTypeModel> ports,
                    HashMap<String, ConnectorTypeModel> connectors) {
        Stack<CompoundTypeModel> stack = new Stack<CompoundTypeModel>();
        stack.push(this);
        while (!stack.isEmpty()) {
            CompoundTypeModel compound = stack.pop();
            for (IInstance child : compound.getChildren()) {
                // 原子组件
                if (child instanceof AtomicModel) {
                    AtomicTypeModel atomic = (AtomicTypeModel) child.getType();
                    for (PortModel port : atomic.getPorts()) {
                        String typeName = port.getType().getName();
                        if (ports.containsKey(typeName)) {
                            continue;
                        } else {
                            ports.put(typeName, (PortTypeModel) port.getType());
                        }
                    }
                }
                // 连接子
                else if (child instanceof ConnectorModel) {
                    ConnectorTypeModel connector = (ConnectorTypeModel) child.getType();
                    PortModel exportPort = connector.getPort();
                    String typeName = exportPort.getType().getName();
                    if (!ports.containsKey(typeName)) {
                        ports.put(typeName, (PortTypeModel) exportPort.getType());
                    }
                    for (ArgumentEntry entry : connector.getArgumentEntries()) {
                        if (entry.isBounded()) {
                            PortTypeModel port = entry.getModel();
                            typeName = port.getName();
                            if (!ports.containsKey(typeName)) {
                                ports.put(typeName, port);
                            }
                        }
                    }
                    typeName = connector.getName();
                    if (!connectors.containsKey(typeName)) {
                        connectors.put(typeName, connector);
                    }
                } else if (child instanceof CompoundModel) {
                    stack.push((CompoundTypeModel) child.getType());
                }
            }
        }
    }

    /**
     * 将以本模型根节点的所有模块导出成BIP代码
     * 
     * 在调用这个函数之前需要先调用validate判断是否可以导出
     * 
     * @return
     */
    public String exportAllToBIP() {
        ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
        ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
        try {
            getAllComponent(compounds, atomics);
        } catch (IncompleteModelException ex) {
            MessageUtil.addProblemWarningMessage("模型不完整，不能导出：" + ex.getMessage());
            return "";
        }

        StringBuilder buffer = new StringBuilder("model BIP_MODEL\n\n");

        try {
            HashMap<String, PortTypeModel> ports = new HashMap<String, PortTypeModel>();
            HashMap<String, ConnectorTypeModel> connectors =
                            new HashMap<String, ConnectorTypeModel>();
            getAllUsedPortAndConnectorType(ports, connectors);
            for (Entry<String, PortTypeModel> entry : ports.entrySet()) {
                buffer.append('\t').append(entry.getValue().exportToBip()).append('\n');
            }
            buffer.append('\n');

            for (Entry<String, ConnectorTypeModel> entry : connectors.entrySet()) {
                buffer.append('\t').append(entry.getValue().exportToBip()).append("\n\n");
            }
            buffer.append('\n');

            for (AtomicTypeModel atomic : atomics) {
                buffer.append('\t').append(atomic.exportToBip()).append('\n');
            }
            buffer.append('\n');

            for (CompoundTypeModel compound : compounds) {
                buffer.append('\t').append(compound.exportToBip()).append('\n');
            }
            buffer.append("\tcomponent ").append(getName()).append(" m\nend");
        } catch (EdolaModelingException ex) {
            MessageUtil.addProblemWarningMessage(ex.getMessage());
            return "";
        }

        return buffer.toString();
    }

    /**
     * 将以本模型根节点的所有模块导出成为 “代码生成” 的BIP代码
     * 
     * 在调用这个函数之前需要先调用 checkCodeGenValid 判断是否可以导出
     * 
     * @return TODO 需要测试
     */
    public String exportAllToBIPforCodeGen() {
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

        ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
        ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
        try {
            getAllComponent(compounds, atomics);
        } catch (IncompleteModelException ex) {
            MessageUtil.addProblemWarningMessage("模型不完整，不能导出：" + ex.getMessage());
            return "";
        }

        StringBuilder buffer = new StringBuilder("model BIP_MODEL\n\n");

        try {
            for (Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
                buffer.append('\t').append(entry.getValue().exportToBip()).append('\n');
            }
            buffer.append('\n');

            // 这里只是导出连接子类型，不需要修改,确定类型与实例的关系，因为无法确定连接子是否连接纯硬件port，所以这里就把所有类型都写上了
            for (Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
                buffer.append('\t').append(entry.getValue().exportToBip()).append("\n\n");
            }
            buffer.append('\n');

            // 给需要分割的连接子重命名
            buildNewNamesforConnectors();

            // 如果顶层组件中的连接子，如果有涉及到软硬划分，则需要新建一个连接子类型
            for (ConnectorModel connector : connectors) {
                if (!connector.getType().checkSoftwareHardwarePorts()) // 只有同时包含软硬件的连接子才导出（同时包含软硬件的连接子才会被分割）
                    continue;
                buffer.append("\t").append(connector.getType().exportToBipforCodeGen())
                                .append('\n');
            }
            buffer.append('\n');


            for (AtomicTypeModel atomic : atomics) {
                if (atomic.isMarkedSoftware()) // 只有软件模块才导出
                    buffer.append('\t').append(atomic.exportToBip()).append('\n');
            }
            buffer.append('\n');

            for (CompoundTypeModel compound : compounds) {
                if (!compound.equals(this)
                                && compound.getHardwareSoftwareType().equals(
                                                projectModel.getSoftwareEntity())) // 只有软件模块才能导出,非本模块
                    buffer.append('\t').append(compound.exportToBip()).append('\n');// 由于条件2，非顶层模块，没有分割问题，直接导出即可，不用特殊处理
                if (compound.equals(this)) // 本模块是顶层模块，可能涉及到软硬分割，需要调用特殊函数导出
                    buffer.append('\t').append(compound.exportToBipforCodeGen()).append('\n');

            }
            buffer.append("\tcomponent ").append(getName()).append(" m\nend");
        } catch (EdolaModelingException ex) {
            MessageUtil.addProblemWarningMessage(ex.getMessage());
            return "";
        }

        return buffer.toString();
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("compound type ").append(getName()).append('\n');
        for (ComponentModel component : components) {
            buffer.append("\t\t").append(component.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (ConnectorModel connector : connectors) {
            buffer.append("\t\t").append(connector.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (ConnectorModel connector : connectors) {
            if (connector.isExport()) {
                buffer.append("\t\t").append(connector.exportPort()).append('\n');
            }
        }
        buffer.append('\n');
        for (PriorityModel<CompoundTypeModel> priority : priorities) {
            buffer.append("\t\t").append(priority.exportToBip()).append('\n');
        }
        buffer.append("\tend\n");
        return buffer.toString();
    }

    /**
     * 导出成为 “代码生成” 的BIP代码
     * 
     * 顶层模型里同时有 硬件 和 软件，硬件 不考虑，连接到硬件的connector需要修改，只连接到硬件的connector不能导出
     * 
     * @return TODO 需要测试
     */
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

        buffer.append("compound type ").append(getName()).append('\n');
        for (ComponentModel component : components) {
            if (component instanceof ComponentModel
                            && ((ComponentTypeModel) component.getType()).getHardwareSoftwareType()
                                            .equals(projectModel.getSoftwareEntity())) // 只有软件模块才导出
                buffer.append("\t\t").append(component.exportToBip()).append('\n');
        }
        buffer.append('\n');


        for (ConnectorModel connector : connectors) {
            if (connector.getType().checkOnlySoftwarePorts()) // 纯软件connector实例按照原有方法导出
                buffer.append("\t\t").append(connector.exportToBip()).append('\n');

            if (connector.getType().checkSoftwareHardwarePorts()) // 软硬件connector实例按照新方法导出
                buffer.append("\t\t").append(connector.exportToBipforCodeGen()).append('\n');
        }
        buffer.append('\n');


        // 顶层模型不用export端口
        if (false) for (ConnectorModel connector : connectors) {
            if (connector.isExport()) {
                buffer.append("\t\t").append(connector.exportPort()).append('\n');
            }
        }
        buffer.append('\n');

        for (PriorityModel<CompoundTypeModel> priority : priorities) {
            buffer.append("\t\t").append(priority.exportToBip()).append('\n');
        }
        buffer.append("\tend\n");
        return buffer.toString();
    }

    public String getAllTickConnectorName() {
        Set<PortModel> ports = new HashSet<PortModel>();

        for (IInstance child : getChildren()) {
            if (child instanceof ConnectorModel) {
                boolean contain = false;

                ports = (((ConnectorModel) child).getType().getAllRelativePorts());
                for (PortModel port : ports) {
                    if (port.isMarkedTick()) {
                        contain = true;
                        break;
                    }
                }
                if (contain) {
                    return child.getName();
                }
            }
        }

        return "";
    }

    public void getAllComponentWithoutChecking(ArrayList<CompoundTypeModel> compounds,
                    ArrayList<AtomicTypeModel> atomics) {
        Stack<CompoundTypeModel> stack = new Stack<CompoundTypeModel>();
        HashSet<String> typeNames = new HashSet<String>();
        stack.push(this);
        typeNames.add(getName());
        while (!stack.isEmpty()) {
            CompoundTypeModel item = stack.pop();
            HashSet<String> names = new HashSet<String>();
            for (IInstance child : item.getChildren()) {
                if (child instanceof AtomicModel) {
                    atomics.add((AtomicTypeModel) child.getType());
                } else if (child instanceof CompoundModel) {
                    stack.push((CompoundTypeModel) child.getType());
                }
            }
            compounds.add(0, item);
        }
    }

    /**
     * 以当前对象为根节点遍历所有的component类型
     * 
     * @param compounds compound列表，传进来的时候为空，将遍历到的compound保存进去
     * @param atomics atomic列表，传进来的时候为空，将遍历到的atomic保存进去
     */
    public void getAllComponent(ArrayList<CompoundTypeModel> compounds,
                    ArrayList<AtomicTypeModel> atomics) {
        Stack<CompoundTypeModel> stack = new Stack<CompoundTypeModel>();
        HashSet<String> typeNames = new HashSet<String>();
        stack.push(this);
        typeNames.add(getName());
        while (!stack.isEmpty()) {
            CompoundTypeModel item = stack.pop();
            HashSet<String> names = new HashSet<String>();
            for (IInstance child : item.getChildren()) {
                if (child instanceof InvisibleBulletModel) {
                    String name = "NULL";
                    BaseInstanceModel source =
                                    ((InvisibleBulletModel) child).getTargetConnections().get(0)
                                                    .getSource();
                    if (source instanceof ConnectorModel) {
                        name = source.getName();
                    } else {
                        // TODO 通过InternalBulletModel得到Connector的名字
                    }
                    throw new IncompleteModelException("连接子 " + name + " 不完整");
                }
                // 避免全局类型重名
                if (child.getType() == null) continue;
                String name = child.getType().getName();
                while (typeNames.contains(name)) {
                    Matcher mat = NAME_PREFIX.matcher(name);
                    if (mat.matches()) {
                        String baseName = mat.group(1);
                        String number = mat.group(2);
                        name = baseName + "" + (Integer.parseInt(number) + 1);
                    } else {
                        name = name + "1";
                    }
                    if (child instanceof AtomicModel || child instanceof CompoundModel) {
                        child.getType().setName(name);
                    }
                }
                typeNames.add(name);
                // 避免compound内部构件重名
                name = child.getName();
                while (names.contains(name)) {
                    Matcher mat = NAME_PREFIX.matcher(name);
                    if (mat.matches()) {
                        String baseName = mat.group(1);
                        String number = mat.group(2);
                        name = baseName + "_" + (Integer.parseInt(number) + 1);
                    } else {
                        name = name + "_1";
                    }
                    child.setName(name);
                }
                names.add(name);
                if (child instanceof AtomicModel) {
                    atomics.add((AtomicTypeModel) child.getType());
                } else if (child instanceof CompoundModel) {
                    stack.push((CompoundTypeModel) child.getType());
                }
            }
            compounds.add(0, item);
        }
    }

    @Override
    public List<IInstance> getChildren() {
        ArrayList<IInstance> children = new ArrayList<IInstance>();
        children.addAll(components);
        children.addAll(connectors);
        children.addAll(priorities);
        children.addAll(bullets);
        children.addAll(diamonds);
        for (IInstance instance : children) {
            if (instance.getParent() == null || !instance.getParent().equals(this)) {
                instance.setParent(this);
            }
        }
        return children;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    /**
     * 返回Compound Type内部的所有连接子，按照连接子类型做聚类
     * 
     * @return
     */
    public HashMap<String, List<ConnectorModel>> getConnectorsGroupByType() {
        HashMap<String, List<ConnectorModel>> map = new HashMap<String, List<ConnectorModel>>();
        for (ConnectorModel data : connectors) {
            String typeName = data.getType().getName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<ConnectorModel>());
            }
            map.get(typeName).add(data);
        }
        return map;
    }


    @Override
    public List<PortModel> getExportPorts() {
        List<PortModel> exportPorts = new ArrayList<PortModel>();
        for (ConnectorModel connector : connectors) {
            if (connector.isExport()) {
                exportPorts.add(connector.getExportPort());
            }
        }
        return exportPorts;
    }

    public String getHardwareSoftwareType() {
        if (!checkEntityTagHWorSW()) return "";

        String childType = "";

        for (IInstance child : getChildren()) {
            if (child instanceof AtomicModel) {
                String type = ((AtomicModel) child).getType().getHardwareSoftwareType();

                if (childType.equals("")) // 第一次
                    childType = type;
                if (type.equals("") || !childType.equals(type)) {// 类型冲突

                    String errMessage = String.format("复合组件类型 %s 同时包含“软件”和“硬件”组件。", getName());
                    MessageUtil.addProblemWarningMessage(errMessage);

                    return "";
                }
            }

            if (child instanceof CompoundModel) {
                String type = ((CompoundModel) child).getType().getHardwareSoftwareType();

                if (childType.equals("")) // 第一次
                    childType = type;
                if (!type.equals("") && !childType.equals(type)) {// 类型冲突

                    String errMessage = String.format("复合组件类型 %s 同时包含“软件”和“硬件”组件。", getName());
                    MessageUtil.addProblemWarningMessage(errMessage);
                    return "";
                }
            }
        }

        return childType;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "复合构件名");
        name.setDescription("01");
        properties.add(name);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "标签");
        tag.setDescription("02");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

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
    public boolean isPropertySet(Object id) {
        return ENTITY.equals(id) || NAME.equals(id);
    }

    public boolean removeBullet(InvisibleBulletModel child) {
        if (child == null) {
            return false;
        }
        int index = bullets.indexOf(child);
        if (index < 0) {
            return false;
        }
        bullets.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    @Override
    public boolean removeChild(IInstance iInstance) {
        if (iInstance instanceof ComponentModel) {
            removeComponent((ComponentModel) iInstance);
        } else if (iInstance instanceof ConnectorModel) {
            removeConnector((ConnectorModel) iInstance);
        } else if (iInstance instanceof PriorityModel) {
            removePriority((PriorityModel) iInstance);
        } else if (iInstance instanceof InvisibleBulletModel) {
            removeBullet((InvisibleBulletModel) iInstance);
        } else if (iInstance instanceof DiamondModel) {
            removeDiamond((DiamondModel) iInstance);
        }
        return false;
    }

    private boolean removeDiamond(DiamondModel child) {
        if (child == null) {
            return false;
        }
        int index = diamonds.indexOf(child);
        if (index < 0) {
            return false;
        }
        diamonds.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removeComponent(ComponentModel child) {
        if (child == null) {
            return false;
        }
        int index = components.indexOf(child);
        if (index < 0) {
            return false;
        }
        components.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removeConnector(ConnectorModel child) {
        if (child == null) {
            return false;
        }
        int index = connectors.indexOf(child);
        if (index < 0) {
            return false;
        }
        connectors.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removePriority(PriorityModel child) {
        if (child == null) {
            return false;
        }
        int index = priorities.indexOf(child);
        if (index < 0) {
            return false;
        }
        priorities.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    @Override
    public CompoundTypeModel setName(String newName) {
        super.setName(newName);
        getInstance().firePropertyChange(TYPE_NAME);
        return this;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        }
        if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>) value);
        }
    }


    private void addBullet(InvisibleBulletModel child) {
        bullets.add(child);
        firePropertyChange(CHILDREN);
    }

    private void addComponent(ComponentModel component) {
        components.add(component);
        if (this.getInstance().getParent() != null) {
            addPropertyChangeListener(this.getInstance().getParent());
        }
        firePropertyChange(CHILDREN);
    }

    private void addConnector(ConnectorModel connector) {
        connectors.add(connector);
        firePropertyChange(CHILDREN);
    }

    private void addPriority(PriorityModel<CompoundTypeModel> priorityModel) {
        priorities.add(priorityModel);
        firePropertyChange(CHILDREN);
    }

}
