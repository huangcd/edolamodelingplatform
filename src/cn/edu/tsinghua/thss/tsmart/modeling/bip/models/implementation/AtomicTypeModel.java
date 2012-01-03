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
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageDialog;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.*;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午9:05<br/>
 * AtomicTypeModel：一方面应该是一个类型模型，另一方面应该是一个容器
 * 
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class AtomicTypeModel extends ComponentTypeModel<AtomicTypeModel, AtomicModel>
                implements
                    IDataContainer<AtomicTypeModel, IContainer, IInstance> {

    private static final long                                                        serialVersionUID =
                                                                                                                      6729458252836818786L;
    private final static HashMap<String, AtomicTypeModel>                            typeSources;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    static {
        typeSources = new HashMap<String, AtomicTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();
    }

    public static void saveTypes() {
        File file = new File(Activator.getPreferenceDirection(), GlobalProperties.ATOMIC_TYPE_FILE);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            for (Map.Entry<String, AtomicTypeModel> entry : getTypeEntries()) {
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
                AtomicTypeModel model = (AtomicTypeModel) in.readObject();
                if (!getTypes().contains(model.getName())) {
                    addType(model.getName(), model);
                }
            }
        } catch (EOFException e) {} catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void addType(String type, AtomicTypeModel model) {
        if (!addTypeSources(type, model)) {
            MessageDialog.ShowErrorDialog("已存在同名的组件", "错误");
            return;
        }
        HashMap<CompoundEditor, CreationToolEntry> map =
                        new HashMap<CompoundEditor, CreationToolEntry>();
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, model.getComment(), new CopyFactory(model),
                                            BIPEditor.getImage("icons/atomic_16.png"),
                                            BIPEditor.getImage("icons/atomic_32.png"));
            editor.addAtomicCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    public static boolean addTypeSources(String type, AtomicTypeModel connector) {
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

    public static Set<Map.Entry<String, AtomicTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static AtomicTypeModel getModelByName(String type) {
        return typeSources.get(type).copy();
    }

    @Element(name = "initialPlace")
    private PlaceModel                           initPlace;
    @Element(name = "initialAction", required = false)
    private ActionModel                          initAction;
    @ElementList(entry = "places")
    private List<PlaceModel>                     places;
    @ElementList(entry = "datas")
    private List<DataModel<AtomicTypeModel>>     datas;
    @ElementList(entry = "ports")
    private List<PortModel>                      ports;
    @ElementList(entry = "priorities")
    private List<PriorityModel<AtomicTypeModel>> priorities;

    public AtomicTypeModel() {
        places = new ArrayList<PlaceModel>();
        datas = new ArrayList<DataModel<AtomicTypeModel>>();
        ports = new ArrayList<PortModel>();
        priorities = new ArrayList<PriorityModel<AtomicTypeModel>>();
        initPlace = new PlaceModel();
        initPlace.setName("INIT").setPositionConstraint(new Rectangle(100, 100, 30, 30))
                        .setParent(this);
        initAction = new ActionModel();
        addChild(initPlace);
    }

    @Override
    public List<IInstance> getChildren() {
        ArrayList<IInstance> children = new ArrayList<IInstance>(datas);
        children.addAll(places);
        children.addAll(ports);
        children.addAll(priorities);
        // if (initAction != null) children.add(initAction);
        return children;
    }

    @Override
    public AtomicTypeModel addChild(IInstance child) {
        while (isNewNameAlreadyExistsInParent(child, child.getName())) {
            child.setName(child.getName() + "_");
        }
        if (this.getInstance().getParent() != null) {
            addPropertyChangeListener(this.getInstance().getParent());
            firePropertyChange(CHILDREN);
        }

        if (child instanceof PlaceModel) {
            addPlace((PlaceModel) child);
        } else if (child instanceof DataModel) {
            addData((DataModel<AtomicTypeModel>) child);
        } else if (child instanceof PortModel) {
            addPort((PortModel) child);
        } else if (child instanceof PriorityModel) {
            addPriority((PriorityModel) child);
        } else {
            System.err.println("Invalidated child type to add:\n" + child);
        }
        return this;
    }

    @Override
    public boolean removeChild(IInstance child) {
        if (child instanceof PlaceModel) {
            return removePlace((PlaceModel) child);
        } else if (child instanceof DataModel) {
            return removeData((DataModel<AtomicTypeModel>) child);
        } else if (child instanceof PriorityModel) {
            return removePriority((PriorityModel) child);
        } else if (child instanceof PortModel) {
            return removePort((PortModel) child);
        }
        System.err.println("Invalidated child type to remove:\n" + child);
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

    public boolean removeData(DataModel<AtomicTypeModel> child) {
        if (child == null) {
            return false;
        }
        int index = datas.indexOf(child);
        if (index < 0) {
            return false;
        }
        datas.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removePort(PortModel child) {
        if (child == null) {
            return false;
        }
        int index = ports.indexOf(child);
        if (index < 0) {
            return false;
        }
        ports.remove(index);
        firePropertyChange(CHILDREN);
        if (child.isExport()) {
            getInstance().firePropertyChange(EXPORT_PORT);
        }
        return true;
    }

    public boolean removePlace(PlaceModel child) {
        if (child == null || child.equals(initPlace)) {
            return false;
        }
        int index = places.indexOf(child);
        if (index < 0) {
            return false;
        }
        places.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removePlaceWithTransitions(PlaceModel child) {
        if (child == null || child.equals(initPlace)) {
            return false;
        }
        int index = places.indexOf(child);
        if (index < 0) {
            return false;
        }
        places.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public void addPort(PortModel child) {
        ports.add(child);
        child.setParent(this);
        firePropertyChange(CHILDREN);
    }

    public AtomicTypeModel addPriority(PriorityModel<AtomicTypeModel> child) {
        priorities.add(child);
        firePropertyChange(CHILDREN);
        return this;
    }

    public AtomicTypeModel addData(DataModel<AtomicTypeModel> child) {
        datas.add(child);
        firePropertyChange(CHILDREN);
        return this;
    }

    public AtomicTypeModel addPlace(PlaceModel child) {
        places.add(child);
        firePropertyChange(CHILDREN);
        return this;
    }

    @Override
    public AtomicModel createInstance() {
        if (instance == null) {
            instance = new AtomicModel().setType(this);
        }
        return instance;
    }

    public PlaceModel getInitPlace() {
        return initPlace;
    }

    /**
     * 返回Atomic Type内部的所有数据，按照数据类型做聚类
     * 
     * @return
     */
    public HashMap<String, List<DataModel>> getDatasGroupByType() {
        HashMap<String, List<DataModel>> map = new HashMap<String, List<DataModel>>();
        for (DataModel<AtomicTypeModel> data : datas) {
            String typeName = data.getType().getName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<DataModel>());
            }
            map.get(typeName).add(data);
        }
        return map;
    }

    /**
     * 返回Atomic Type内部的所有端口，按照端口类型做聚类
     * 
     * @return
     */
    public HashMap<String, List<PortModel>> getPortsGroupByType() {
        HashMap<String, List<PortModel>> map = new HashMap<String, List<PortModel>>();
        for (PortModel<AtomicTypeModel> data : ports) {
            String typeName = data.getType().getName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<PortModel>());
            }
            map.get(typeName).add(data);
        }
        return map;
    }

    /**
     * 设置初始状态
     * 
     * @param newInitPlace
     * @return
     */
    public AtomicTypeModel setInitPlace(PlaceModel newInitPlace) {
        if (newInitPlace.equals(this.initPlace)) return this;
        if (!places.contains(newInitPlace)) {
            addPlace(newInitPlace);
        }
        PlaceModel oldInitPlace = this.initPlace;
        initPlace = newInitPlace;
        oldInitPlace.firePropertyChange(ATOMIC_INIT_PLACE);
        newInitPlace.firePropertyChange(ATOMIC_INIT_PLACE);
        return this;
    }

    @Override
    public AtomicTypeModel setName(String newName) {
        super.setName(newName);
        getInstance().firePropertyChange(TYPE_NAME);
        return this;
    }

    public ActionModel getInitAction() {
        return initAction;
    }

    public AtomicTypeModel setInitAction(ActionModel initAction) {
        this.initAction = initAction;
        firePropertyChange(CHILDREN);
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("atomic type ").append(getName()).append('\n');
        for (DataModel<AtomicTypeModel> data : datas) {
            buffer.append("\t\t").append(data.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (PortModel port : ports) {
            buffer.append("\t\t").append(port.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (PlaceModel place : places) {
            buffer.append("\t\t").append(place.exportToBip()).append('\n');
        }
        buffer.append('\n');
        buffer.append("\t\t").append("initial to ").append(initPlace.getName()).append(" do {")
                        .append(initAction.exportToBip()).append("}\n\n");

        for (PlaceModel place : places) {
            for (TransitionModel transition : place.getSourceConnections()) {
                buffer.append("\t\t").append(transition.exportToBip()).append('\n');
            }
        }

        buffer.append('\n');
        for (PriorityModel priority : priorities) {
            buffer.append('\t').append(priority.exportToBip()).append('\n');
        }
        buffer.append("\tend\n");
        return buffer.toString();
    }

    @Override
    public List<DataModel<AtomicTypeModel>> getDatas() {
        return datas;
    }

    public List<PortModel> getPorts() {
        return ports;
    }

    @Override
    public List<PortModel> getExportPorts() {
        List<PortModel> exportPorts = new ArrayList<PortModel>();
        for (PortModel port : ports) {
            if (port.isExport()) {
                exportPorts.add(port);
            }
        }
        return exportPorts;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        int placesCount = places.size();
        String[] placeNames = new String[placesCount];
        for (int i = 0; i < placesCount; i++) {
            PlaceModel place = places.get(i);
            placeNames[i] = place.getName();
        }
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "原子构件名");
        name.setDescription("01");
        properties.add(name);
        ComboBoxPropertyDescriptor init =
                        new ComboBoxPropertyDescriptor(ATOMIC_INIT_PLACE, "初始状态", placeNames);
        init.setDescription("02");
        properties.add(init);
        EntitySelectionPropertyDescriptor tag =
                        new EntitySelectionPropertyDescriptor(ENTITY, "标签");
        tag.setDescription("03");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (ATOMIC_INIT_PLACE.equals(id)) {
            return places.indexOf(initPlace);
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ENTITY.equals(id) || NAME.equals(id) || ATOMIC_INIT_PLACE.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (ATOMIC_INIT_PLACE.equals(id)) {
            int index = (Integer) value;
            setInitPlace(places.get(index));
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>)value);
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

}
