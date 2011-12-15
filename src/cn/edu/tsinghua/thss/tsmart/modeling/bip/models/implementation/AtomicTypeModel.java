package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午9:05<br/>
 * AtomicTypeModel：一方面应该是一个类型模型，另一方面应该是一个容器
 * 
 * 
 * FIXME 关闭一个Atomic编辑页面的时候应该把所有Transition去掉（detach），打开的时候再attach上去
 */
@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class AtomicTypeModel extends BaseTypeModel<AtomicTypeModel, AtomicModel, IContainer>
                implements
                    IDataContainer<AtomicTypeModel, IContainer, IInstance>,
                    IComponentType<AtomicTypeModel, AtomicModel, IContainer, IInstance> {

    public final static String                              INIT_PLACE  = "initPlace";
    public final static String                              INIT_ACTION = "initAction";
    @Element(name = "initialPlace")
    private PlaceModel                                      initPlace;
    @Element(name = "initialAction", required = false)
    private ActionModel                                     initAction;
    @ElementList(entry = "place")
    private List<PlaceModel>                                places;
    @ElementList(entry = "data")
    private List<DataModel<AtomicTypeModel>>                datas;
    @ElementList(entry = "port")
    private List<PortModel>                                 ports;
    @ElementList(entry = "priority")
    private List<PriorityModel<AtomicTypeModel, PortModel>> priorities;

    public AtomicTypeModel() {
        places = new ArrayList<PlaceModel>();
        datas = new ArrayList<DataModel<AtomicTypeModel>>();
        ports = new ArrayList<PortModel>();
        priorities = new ArrayList<PriorityModel<AtomicTypeModel, PortModel>>();
        initPlace = new PlaceTypeModel().getInstance();
        initPlace.setName("init").setPositionConstraint(new Rectangle(100, 100, 30, 30))
                        .setParent(this);
        addChild(initPlace);
    }

    @Override
    public List<IInstance> getChildren() {
        ArrayList<IInstance> children = new ArrayList<IInstance>(datas);
        children.addAll(places);
        children.addAll(ports);
        children.addAll(priorities);
        if (initAction != null) children.add(initAction);
        return children;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AtomicTypeModel addChild(IInstance child) {
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
    @SuppressWarnings("unchecked")
    /**
     * 初始Place不可删除
     * 如果Place有连着Transition
     */
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
        firePropertyChange(CHILDREN);
    }

    public AtomicTypeModel addPriority(PriorityModel<AtomicTypeModel, PortModel> child) {
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
            String typeName = data.getType().getTypeName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<DataModel>());
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
        oldInitPlace.firePropertyChange(INIT_PLACE);
        newInitPlace.firePropertyChange(INIT_PLACE);
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
    public AtomicTypeModel copy() {
        AtomicTypeModel copyModel = new AtomicTypeModel();
        copyModel.setName("CopyOf" + getName());
        HashMap<DataModel<AtomicTypeModel>, DataModel<AtomicTypeModel>> dataMap = copyList(datas);
        HashMap<PlaceModel, PlaceModel> placeMap = copyList(places);
        HashMap<PortModel, PortModel> portMap = new HashMap<PortModel, PortModel>();

        // 复制 datas
        copyModel.datas = new ArrayList<DataModel<AtomicTypeModel>>();
        for (DataModel<AtomicTypeModel> data : datas) {
            copyModel.datas.add(dataMap.get(data));
        }

        // 复制 ports
        copyModel.ports = new ArrayList<PortModel>();
        for (PortModel port : ports) {
            PortTypeModel oldPortType = (PortTypeModel) port.getType();
            // TODO a mash
            PortTypeModel copyPortType = oldPortType;
            // .copy(oldPortType.getPortTypeArguments());
            portMap.put(port, copyPortType.getInstance());
            copyModel.ports.add(copyPortType.getInstance());
        }

        // 复制 places
        copyModel.places = new ArrayList<PlaceModel>();
        for (PlaceModel place : places) {
            copyModel.places.add(placeMap.get(place));
        }

        // 复制 transitions

        copyModel.priorities =
                        new ArrayList<PriorityModel<AtomicTypeModel, PortModel>>(this.priorities);
        for (PriorityModel<AtomicTypeModel, PortModel> priority : priorities) {
            // copyModel.priorities.add(priorityMap.get(priority));
        }
        copyModel.initAction = this.initAction;
        copyModel.initPlace = this.initPlace;
        return copyModel;
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
            buffer.append('\t').append(data.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (PortModel port : ports) {
            buffer.append('\t').append(port.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (PlaceModel place : places) {
            buffer.append('\t').append(place.exportToBip()).append('\n');
        }
        buffer.append('\n');
        buffer.append('\t').append("initial to ").append(initPlace.getName()).append("do {")
                        .append(initAction.exportToBip()).append("}\n\n");
        // TODO transition
        // for (TransitionModel transition : transitions) {
        // buffer.append('\t').append(transition.exportToBip()).append('\n');
        // }
        buffer.append('\n');
        for (PriorityModel priority : priorities) {
            buffer.append('\t').append(priority.exportToBip()).append('\n');
        }
        buffer.append("end\n");
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
    public List<IPort> getExportPorts() {
        List<IPort> exportPorts = new ArrayList<IPort>();
        for (IPort port : ports) {
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
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "component name"),
                        new ComboBoxPropertyDescriptor(INIT_PLACE, "init place", placeNames)};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (INIT_PLACE.equals(id)) {
            return places.indexOf(initPlace);
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || INIT_PLACE.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (INIT_PLACE.equals(id)) {
            int index = (Integer) value;
            setInitPlace(places.get(index));
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
}
