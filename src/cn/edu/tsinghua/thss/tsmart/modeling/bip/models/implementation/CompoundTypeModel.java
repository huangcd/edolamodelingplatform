package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;

import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageDialog;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午9:15<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class CompoundTypeModel extends ComponentTypeModel<CompoundTypeModel, CompoundModel> {

    private static final long                                                        serialVersionUID;

    private final static Pattern                                                     atomicNamePattern;
    private final static Pattern                                                     compoundNamePattern;
    private final static HashMap<String, CompoundTypeModel>                          typeSources;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    static {
        serialVersionUID = 4875759382399971421L;
        atomicNamePattern = Pattern.compile("^atomic(\\d*)$");
        compoundNamePattern = Pattern.compile("^compound(\\d*)$");
        typeSources = new HashMap<String, CompoundTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();
    }

    public static void saveTypes() {
        File file = new File(Activator.getPreferenceDirection(), GlobalProperties.ATOMIC_TYPE_FILE);
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

    public static void loadTypes() {
        File file = new File(Activator.getPreferenceDirection(), GlobalProperties.ATOMIC_TYPE_FILE);
        if (!file.exists()) {
            return;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            while (true) {
                CompoundTypeModel model = (CompoundTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model.getName(), model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addType(String type, CompoundTypeModel model) {
        if (!addTypeSources(type, model)) {
            MessageDialog.ShowErrorDialog("已存在同名的组件", "错误");
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

    public static boolean addTypeSources(String type, CompoundTypeModel connector) {
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
    }

    private static boolean removeTypeSources(String type) {
        if (!typeSources.containsKey(type)) {
            return false;
        }
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

    public static Set<Map.Entry<String, CompoundTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static CompoundTypeModel getModelByName(String type) {
        return typeSources.get(type).copy();
    }

    @ElementList
    private List<ComponentModel>                   components;
    @ElementList
    private List<ConnectorModel>                   connectors;
    @ElementList
    private List<PriorityModel<CompoundTypeModel>> priorities;
    private List<InvisibleBulletModel>             bullets;

    public CompoundTypeModel() {
        components = new ArrayList<ComponentModel>();
        connectors = new ArrayList<ConnectorModel>();
        priorities = new ArrayList<PriorityModel<CompoundTypeModel>>();
        bullets = new ArrayList<InvisibleBulletModel>();
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

    public void addComponent(ComponentModel component) {
        components.add(component);
        if (this.getInstance().getParent() != null) {
            addPropertyChangeListener(this.getInstance().getParent());
        }
        firePropertyChange(CHILDREN);
    }

    public void addConnector(ConnectorModel connector) {
        connectors.add(connector);
        firePropertyChange(CHILDREN);
    }

    public void addPriority(PriorityModel<CompoundTypeModel> priorityModel) {
        priorities.add(priorityModel);
        firePropertyChange(CHILDREN);
    }

    protected void addBullet(InvisibleBulletModel child) {
        bullets.add(child);
        firePropertyChange(CHILDREN);
    }

    public List<ComponentModel> getComponents() {
        return components;
    }

    @Override
    public CompoundTypeModel addChild(IInstance child) {
        while (isNewNameAlreadyExistsInParent(child, child.getName())) {
            child.setName(child.getName() + "_");
        }
        if (child instanceof ComponentModel) {
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

    @Override
    public CompoundModel createInstance() {
        if (instance == null) {
            instance = new CompoundModel().setType(this);
        }
        return instance;
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
     * 将以本模型根节点的所有模块导出成BIP代码
     * 
     * 在调用这个函数之前需要先调用validate判断是否可以导出
     * 
     * @return
     */
    public String exportAllToBIP() {
        ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
        ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
        getAllComponent(compounds, atomics);

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

    private final static Pattern NAME_PREFIX = Pattern.compile("^(.*)_(\\d+)$");

    /**
     * 以当前对象为根节点遍历所有的component类型
     * 
     * @param compounds
     * @param atomics
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
                // 避免全局类型重名
                String name = child.getType().getName();
                while (typeNames.contains(name)) {
                    Matcher mat = NAME_PREFIX.matcher(name);
                    if (mat.matches()) {
                        String baseName = mat.group(1);
                        String number = mat.group(2);
                        name = baseName + "_" + (Integer.parseInt(number) + 1);
                    } else {
                        name = name + "_1";
                    }
                    child.getType().setName(name);
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
    public boolean exportable() {
        return true;
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

    @Override
    public CompoundTypeModel setName(String newName) {
        super.setName(newName);
        getInstance().firePropertyChange(TYPE_NAME);
        return this;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "复合构件名");
        name.setDescription("01");
        properties.add(name);
        EntitySelectionPropertyDescriptor tag =
                        new EntitySelectionPropertyDescriptor(ENTITY, "标签");
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
    public boolean isPropertySet(Object id) {
        return ENTITY.equals(id) || NAME.equals(id);
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
    public boolean isNewNameKeyword(String newName) {
        if (GlobalProperties.getKeywords().contains(newName)) return true;
        return false;
    }


    public static String getAppropriateAtomicName(CompoundTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = atomicNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "atomic" + maxNumber;
    }

    public static String getAppropriateCompoundName(CompoundTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = compoundNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "compound" + maxNumber;
    }
}
