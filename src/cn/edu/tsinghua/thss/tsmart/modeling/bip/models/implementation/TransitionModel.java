package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.UnboundedException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.MultilineTextPropertyDescriptor;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class TransitionModel
                extends BaseInstanceModel<TransitionModel, BaseTypeModel, AtomicTypeModel>
                implements
                    IConnection<TransitionModel, AtomicTypeModel, PlaceModel, PlaceModel> {

    private static final long          serialVersionUID = 3752284127183635224L;
    @Element
    private PlaceModel                 source;
    @Element
    private PlaceModel                 target;
    @Element
    private PortModel<AtomicTypeModel> port;
    @Element
    private ActionModel                action;
    @Element
    private GuardModel                 guard;
    @ElementList
    private ArrayList<Bendpoint>       bendpoints;

    public TransitionModel() {
        action = new ActionModel();
        guard = new GuardModel();
        bendpoints = new ArrayList<Bendpoint>();
    }

    public PlaceModel getSource() {
        return source;
    }

    public PlaceModel getTarget() {
        return target;
    }

    public PortModel<AtomicTypeModel> getPort() {
        return port;
    }

    /**
     * 设置Transition的端口标签，并把Transition加入到port的属性变化通知队列中去
     * 
     * @param newPort
     */
    public void setPort(PortModel<AtomicTypeModel> newPort) {
        if (port != null) {
            port.removePropertyChangeListener(this);
        }
        port = newPort;
        if (port != null) {
            port.addPropertyChangeListener(this);
        }
        firePropertyChange(IModel.PORT);
    }

    public void setSource(PlaceModel source) {
        this.source = source;
    }

    public void setTarget(PlaceModel target) {
        this.target = target;
    }

    public String getActionString() {
        return action == null ? "" : action.getAction();
    }

    public String getOneLineActionString() {
        if (action == null) {
            return "";
        }
        String str = action.getAction();
        if (str == null) {
            return "";
        }
        str = str.replaceAll("\r", "\n");
        int index = str.indexOf('\n');
        if (index == -1) {
            return str;
        }
        return str.substring(0, index) + "...";
    }

    public ActionModel getAction() {
        return action;
    }

    public void setActionString(String action) {
        this.action.setAction(action);
        firePropertyChange(ActionModel.ACTION);
    }

    public String getGuardString() {
        return guard == null ? "" : guard.getGuard();
    }

    public GuardModel getGuard() {
        return guard;
    }

    public void setGuardString(String guard) {
        this.guard.setGuard(guard);
        firePropertyChange(GuardModel.GUARD);
    }

    public String getPortString() {
        return port == null ? "" : port.getName();
    }

    /**
     * 友好的信息显示方式
     * 
     * @return
     */
    public String getFriendlyString() {
        return String.format("{%s} %s {%s}", getGuardString(), getPortString(), getActionString());
    }

    public PlaceModel attachSource() {
        if (!source.getSourceConnections().contains(this)) {
            source.addSourceConnection(this);
        }
        return source;
    }

    public PlaceModel attachTarget() {
        if (!target.getTargetConnections().contains(this)) {
            target.addTargetConnection(this);
        }
        return target;
    }

    public PlaceModel detachSource() {
        source.removeSourceConnection(this);
        return source;
    }

    public PlaceModel detachTarget() {
        target.removeTargetConnection(this);
        return target;
    }

    public boolean isLoop() {
        return source != null && source.equals(target);
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        if (getPort() == null) {
            throw new UnboundedException(String.format(
                            "变迁 [%s -> %s， 原子组件 %s] 没有端口关联。", getSource().getName(),
                            getTarget().getName(), getSource().getParent().getName()));
        }
        return String.format("on %s from %s to %s provided %s do {%s}", getPort().getName(),
                        getSource().getName(), getTarget().getName(), getGuard().exportToBip(),
                        getAction().exportToBip());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        List<PortModel> ports = getParent().getPorts();
        String[] values = new String[ports.size() + 1];
        for (int i = 0, size = ports.size(); i < size; i++) {
            values[i] = ports.get(i).getName();
        }
        values[ports.size()] = "$UNBOUNDED$";

        ArrayList<PropertyDescriptor> properties = new ArrayList<PropertyDescriptor>();
        PropertyDescriptor action =
                        new MultilineTextPropertyDescriptor(ActionModel.ACTION, "动作");
        PropertyDescriptor guard =
                        new MultilineTextPropertyDescriptor(GuardModel.GUARD, "约束");
        guard.setDescription("01");
        properties.add(guard);
        ComboBoxPropertyDescriptor port = new ComboBoxPropertyDescriptor(IModel.PORT, "端口", values);
        port.setDescription("02");
        properties.add(port);
        action.setDescription("03");
        properties.add(action);
        /*
         * EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY,
         * "标签"); tag.setDescription("04"); properties.add(tag);
         */
        return properties.toArray(new PropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (ActionModel.ACTION.equals(id)) {
            return action.getAction();
        }
        if (GuardModel.GUARD.equals(id)) {
            return guard.getGuard();
        }
        if (IModel.PORT.equals(id)) {
            List<PortModel> ports = getParent().getPorts();
            if (port == null) {
                return ports.size();
            }
            return ports.indexOf(port);
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ActionModel.ACTION.equals(id) || GuardModel.GUARD.equals(id)
                        || IModel.PORT.equals(id) || ENTITY.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (ActionModel.ACTION.equals(id)) {
            setActionString((String) value);
        } else if (GuardModel.GUARD.equals(id)) {
            setGuardString((String) value);
        } else if (IModel.PORT.equals(id)) {
            List<PortModel> ports = getParent().getPorts();
            int index = (Integer) value;
            if (index == ports.size()) {
                setPort(null);
            }
            if (index >= 0 && index < ports.size()) {
                setPort(ports.get(index));
            }
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>) value);
        }
    }

    @Override
    public ArrayList<Bendpoint> getBendpoints() {
        return bendpoints;
    }

    public TransitionModel setBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().set(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    public TransitionModel addBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().add(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public TransitionModel removeBendpoint(int index) {
        getBendpoints().remove(index);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public Bendpoint getBendpoint(int index) {
        return getBendpoints().get(index);
    }
}
