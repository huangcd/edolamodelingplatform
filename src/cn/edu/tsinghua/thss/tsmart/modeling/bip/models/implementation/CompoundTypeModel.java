package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.IncompleteModelException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����9:15<br/>
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
        return typeSources.values();
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
        // TODO lynn �� �ж�CompoundType�Ƿ����ɾ��
        return true;
    }

    public static void loadTypes() {
        loadTypes(Activator.getPreferenceDirection());
    }

    public static void loadTypes(File directory) {
        File file = new File(directory, GlobalProperties.COMPOUND_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                CompoundTypeModel model = (CompoundTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveTypes() {
        saveTypes(Activator.getPreferenceDirection());
    }

    public static void saveTypes(File directory) {
        File file = new File(directory, GlobalProperties.COMPOUND_TYPE_FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (Map.Entry<String, CompoundTypeModel> entry : getTypeEntries()) {
                out.writeObject(entry.getValue());
                out.flush();
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void addType(CompoundTypeModel model) {
        String type = model.getName();
        if (!addTypeSources(type, model)) {
            MessageUtil.ShowErrorDialog("�Ѵ���ͬ�������", "����");
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

    @ElementList
    private List<ComponentModel>                   components;

    @ElementList
    private List<ConnectorModel>                   connectors;

    @ElementList
    private List<PriorityModel<CompoundTypeModel>> priorities;

    public CompoundTypeModel() {
        components = new ArrayList<ComponentModel>();
        connectors = new ArrayList<ConnectorModel>();
        priorities = new ArrayList<PriorityModel<CompoundTypeModel>>();
        bullets = new ArrayList<InvisibleBulletModel>();
    }

    @Override
    public CompoundTypeModel addChild(IInstance child) {
        ensureUniqueName(child);
        if (child instanceof ComponentModel) {
            child.getType().setName(child.getName() + "type");
            addComponent((ComponentModel) child);
        } else if (child instanceof ConnectorModel) {
            addConnector((ConnectorModel) child);
        } else if (child instanceof PriorityModel) {
            addPriority((PriorityModel) child);
        } else if (child instanceof InvisibleBulletModel) {
            addBullet((InvisibleBulletModel) child);
        } else {
            System.err.println("Invalidated child type to add:\n" + child);
        }
        return this;
    }

    /*
     * ����Ҫ��������connector type������
     * 
     * TODO ��Ҫ����
     */
    public void buildNewNamesforConnectors() {
        ArrayList<ConnectorModel> consNeedNewName = new ArrayList<ConnectorModel>();
        HashSet<String> typeNames = new HashSet<String>();

        for (ConnectorModel con : connectors) {
            if (con.getType().checkSoftwareHardwarePorts()) {// ��Ӳ���У���Ҫ������
                consNeedNewName.add(con);
                con.getType().setHardwareCutName(con.getType().getName());// ��ʼ����nameΪ��name
            }
        }

        // �������connector type��name
        for (Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
            typeNames.add(entry.getValue().getName());
        }

        Pattern NAME_PREFIX = Pattern.compile("^(.*)_(\\d+)$"); // TODO

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
                }
            }
        }

        if (tickConnectorNum > 1) {

            String errMessage = String.format("���б�ע��tick���Ķ˿ڱ���ȫͬ����");
            try {
                MessageUtil.addProblemWarningMessage(errMessage);
            } catch (CoreException e) {
                e.printStackTrace();
            }

            result = false;
        } else {
            result = checkAllTickSyncSub(ports) && result;
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

        try {
            MessageUtil.clearProblemMessage();
        } catch (CoreException e) {
            e.printStackTrace();
        }

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
     * ��Դ������ɣ����ģ�ʹ����ȷ���򷵻�true������Ϊfalse �����ȷ�ĺ������£� 0)����4��entity����ӳ�䣺Ӳ���������io��tick
     * 1�����е�ԭ�������������Ӳ����������ı꣬����ͬʱ���� 2���������������Ƕ������������������ͬʱ��������Ӳ�����ԭ�������������ԭ�����
     * 3������tick�������ӣ�ֻ�ܰ���tick 4�����е�tick������ȫͬ�� 5��io�˿�ֻ�ܺ�io�˿�ͬ�� 6)��Ӳ�����ֲ����ƻ� IO �� tick
     * ֮���port�����ӹ�ϵ����Ϊ����2��ǿ��������������Լ򻯣�ֻ��Ҫ��鶥�㸴������������Ӽ��ɣ�7��IO port�ĵ�����ֻ��Ϊio
     */
    public boolean checkEntityMappings() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // ������ģʽ�²������
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

            String errMessage = String.format("�����Ӳ����IO��tick�������һ��ʵ�塣");
            try {
                MessageUtil.addProblemWarningMessage(errMessage);
            } catch (CoreException e) {
                e.printStackTrace();
            }

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
                                    String.format("ԭ��������� %s ͬʱ����ע�ϡ�������͡�Ӳ����ʵ�����û�б�ע��", child
                                                    .getType().getName());
                    try {
                        MessageUtil.addProblemWarningMessage(errMessage);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (child instanceof CompoundModel) {
                result = ((CompoundModel) child).getType().checkEntityTagHWorSW() && result;
            }
        }
        return result;
    }

    /*
     * ����Ƿ��������Ϊname�������������ʾ��Ϊ��model.model.speed��
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
                                            String.format("������ %s �����ж˿ڱ���ע�ϡ�IO��ʵ�壬�������¶˿� %s û�б�ע��IO��ʵ�塣",
                                                            child.getName(), port.getName());
                            try {
                                MessageUtil.addProblemWarningMessage(errMessage);
                            } catch (CoreException e) {
                                e.printStackTrace();
                            }

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
        // ֻ����Ӹ������
        boolean result = true;
        for (IInstance child : getChildren()) {
            if (child instanceof CompoundModel) {
                if (((CompoundModel) child).getType().getHardwareSoftwareType().equals("")) {
                    String errMessage =
                                    String.format("����������� %s �������ͳ�ͻ���������²������κ�ԭ�������", child
                                                    .getType().getName());
                    try {
                        MessageUtil.addProblemWarningMessage(errMessage);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }

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
                                            String.format("������ %s �����ж˿ڱ���ע�ϡ�tick��ʵ�壬�������¶˿� %s û�б�ע��tick��ʵ�塣",
                                                            child.getName(), port.getName());
                            try {
                                MessageUtil.addProblemWarningMessage(errMessage);
                            } catch (CoreException e) {
                                e.printStackTrace();
                            }

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
        // ����������� checkOnlyContainsHWorSW() Ϊ��ʱ����
        if (!checkOnlyContainsHWorSW()) return false;

        boolean result = true;

        for (IInstance child : getChildren()) {// ��Ϊ����2��Լ����ֻҪ���һ��Ϳ�����
            if (child instanceof ConnectorModel) {
                boolean hardware = false;
                boolean software = false;

                Set<PortModel> ports = ((ConnectorModel) child).getType().getAllRelativePorts();

                for (PortModel port : ports) {
                    if (!port.isMarkedIO() && !port.isMarkedTick()) {// ����io��tick�ſ���
                        if (((AtomicModel) port.getParent()).isMarkedHareware()) {// port���ڵ������Ӳ��
                            hardware = true;
                        }
                        if (((AtomicModel) port.getParent()).isMarkedSoftware()) {// port���ڵ���������
                            software = true;
                        }
                    }
                }
                if (hardware && software) {// һ�������ӣ�����io��tick������ͬʱ��Ӳ��������������ֿ��ˣ�����

                    String errMessage =
                                    String.format("������ %s ���漰�˿ڱ���Ӳ���ָֻ�С�tick���͡�IO�������Ӳ��ܱ���Ӳ���ָ",
                                                    child.getName());
                    try {
                        MessageUtil.addProblemWarningMessage(errMessage);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }


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

    /**
     * ���Ա�ģ�͸��ڵ������ģ�鵼����BIP����
     * 
     * �ڵ����������֮ǰ��Ҫ�ȵ���validate�ж��Ƿ���Ե���
     * 
     * @return
     */
    public String exportAllToBIP() {
        ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
        ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
        try {
            getAllComponent(compounds, atomics);
        } catch (IncompleteModelException ex) {
            MessageUtil.ShowErrorDialog(ex.getMessage(), "ģ�Ͳ����������ܵ���");
        }

        StringBuilder buffer = new StringBuilder("model BIP_MODEL\n\n");
        for (Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
            buffer.append('\t').append(entry.getValue().exportToBip()).append('\n');
        }
        buffer.append('\n');

        for (Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
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
        return buffer.toString();
    }

    /**
     * ���Ա�ģ�͸��ڵ������ģ�鵼����Ϊ ���������ɡ� ��BIP����
     * 
     * �ڵ����������֮ǰ��Ҫ�ȵ��� checkCodeGenValid �ж��Ƿ���Ե���
     * 
     * @return TODO ��Ҫ����
     */
    public String exportAllToBIPforCodeGen() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // ������ģʽ�²������
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
            MessageUtil.ShowErrorDialog(ex.getMessage(), "ģ�Ͳ����������ܵ���");
        }

        StringBuilder buffer = new StringBuilder("model BIP_MODEL\n\n");

        for (Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
            buffer.append('\t').append(entry.getValue().exportToBip()).append('\n');
        }
        buffer.append('\n');

        // ����ֻ�ǵ������������ͣ�����Ҫ�޸�,ȷ��������ʵ���Ĺ�ϵ����Ϊ�޷�ȷ���������Ƿ����Ӵ�Ӳ��port����������Ͱ��������Ͷ�д����
        for (Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
            buffer.append('\t').append(entry.getValue().exportToBip()).append("\n\n");
        }
        buffer.append('\n');

        // ����Ҫ�ָ��������������
        buildNewNamesforConnectors();

        // �����������е������ӣ�������漰����Ӳ���֣�����Ҫ�½�һ������������
        for (ConnectorModel connector : connectors) {
            if (!connector.getType().checkSoftwareHardwarePorts()) // ֻ��ͬʱ������Ӳ���������Ӳŵ�����ͬʱ������Ӳ���������ӲŻᱻ�ָ
                continue;
            buffer.append("\t").append(connector.getType().exportToBipforCodeGen()).append('\n');
        }
        buffer.append('\n');


        for (AtomicTypeModel atomic : atomics) {
            if (atomic.isMarkedSoftware()) // ֻ�����ģ��ŵ���
                buffer.append('\t').append(atomic.exportToBip()).append('\n');
        }
        buffer.append('\n');

        for (CompoundTypeModel compound : compounds) {
            if (!compound.equals(this)
                            && compound.getHardwareSoftwareType().equals(
                                            projectModel.getSoftwareEntity())) // ֻ�����ģ����ܵ���,�Ǳ�ģ��
                buffer.append('\t').append(compound.exportToBip()).append('\n');// ��������2���Ƕ���ģ�飬û�зָ����⣬ֱ�ӵ������ɣ��������⴦��
            if (compound.equals(this)) // ��ģ���Ƕ���ģ�飬�����漰����Ӳ�ָ��Ҫ�������⺯������
                buffer.append('\t').append(compound.exportToBipforCodeGen()).append('\n');

        }
        buffer.append("\tcomponent ").append(getName()).append(" m\nend");
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
     * ������Ϊ ���������ɡ� ��BIP����
     * 
     * ����ģ����ͬʱ�� Ӳ�� �� �����Ӳ�� �����ǣ����ӵ�Ӳ����connector��Ҫ�޸ģ�ֻ���ӵ�Ӳ����connector���ܵ���
     * 
     * @return TODO ��Ҫ����
     */
    public String exportToBipforCodeGen() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // ������ģʽ�²������
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
                                            .equals(projectModel.getSoftwareEntity())) // ֻ�����ģ��ŵ���
                buffer.append("\t\t").append(component.exportToBip()).append('\n');
        }
        buffer.append('\n');


        for (ConnectorModel connector : connectors) {
            if (connector.getType().checkOnlySoftwarePorts()) // �����connectorʵ������ԭ�з�������
                buffer.append("\t\t").append(connector.exportToBip()).append('\n');

            if (connector.getType().checkSoftwareHardwarePorts()) // ��Ӳ��connectorʵ�������·�������
                buffer.append("\t\t").append(connector.exportToBipforCodeGen()).append('\n');
        }
        buffer.append('\n');


        // ����ģ�Ͳ���export�˿�
        if (false) for (ConnectorModel connector : connectors) {
            if (connector.isExport()) {
                buffer.append("\t\t").append(connector.exportPort()).append('\n');
            }
        }
        buffer.append('\n');

        for (PriorityModel<CompoundTypeModel> priority : priorities) {
            if (false) // ������Ӳ��connector��priority������ TODO need check
                       // ����ֻ��port��port֮������ȼ���Ӧ��connector��connector֮������ȼ�
                buffer.append("\t\t").append(priority.exportToBip()).append('\n');
        }
        buffer.append("\tend\n");
        return buffer.toString();
    }

    /**
     * �Ե�ǰ����Ϊ���ڵ�������е�component����
     * 
     * @param compounds compound�б���������ʱ��Ϊ�գ�����������compound�����ȥ
     * @param atomics atomic�б���������ʱ��Ϊ�գ�����������atomic�����ȥ
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
                    ConnectorModel connector =
                                    ((InvisibleBulletModel) child).getTargetConnections().get(0)
                                                    .getSource();
                    throw new IncompleteModelException("������ " + connector.getName() + " ������");
                }
                // ����ȫ����������
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
                    child.getType().setName(name);
                }
                typeNames.add(name);
                // ����compound�ڲ���������
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
        return children;
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    /**
     * ����Compound Type�ڲ������������ӣ���������������������
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

                if (childType.equals("")) // ��һ��
                    childType = type;
                if (type.equals("") || !childType.equals(type)) {// ���ͳ�ͻ

                    String errMessage = String.format("����������� %s ͬʱ������������͡�Ӳ���������", getName());
                    try {
                        MessageUtil.addProblemWarningMessage(errMessage);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }

                    return "";
                }
            }

            if (child instanceof CompoundModel) {
                String type = ((CompoundModel) child).getType().getHardwareSoftwareType();

                if (childType.equals("")) // ��һ��
                    childType = type;
                if (!type.equals("") && !childType.equals(type)) {// ���ͳ�ͻ

                    String errMessage = String.format("����������� %s ͬʱ������������͡�Ӳ���������", getName());
                    try {
                        MessageUtil.addProblemWarningMessage(errMessage);
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }

                    return "";
                }
            }
        }

        return childType;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "���Ϲ�����");
        name.setDescription("01");
        properties.add(name);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "��ǩ");
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
        }
        return false;
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
