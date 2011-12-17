package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class TransitionModel
                extends BaseInstanceModel<TransitionModel, TransitionTypeModel, AtomicTypeModel>
                implements
                    IConnection<TransitionModel, AtomicTypeModel, PlaceModel, PlaceModel> {

    public static final String         BEND_POINTS             = "bendPoints";
    public static final String         REMOVE_TRANSITION_LABEL = "removeTransitionLabels";
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

    protected TransitionModel() {
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
        port.addPropertyChangeListener(this);
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
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(ActionModel.ACTION, "action"),
                        new TextPropertyDescriptor(GuardModel.GUARD, "guard"),
                        new ComboBoxPropertyDescriptor(IModel.PORT, "port", values)};
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
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ActionModel.ACTION.equals(id) || GuardModel.GUARD.equals(id)
                        || IModel.PORT.equals(id);
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
