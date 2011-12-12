package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked"})
@Root
public class TransitionModel
                extends BaseInstanceModel<TransitionModel, TransitionTypeModel, AtomicTypeModel>
                implements
                    IConnection<TransitionModel, AtomicTypeModel, PlaceModel, PlaceModel> {

    public static final String                BEND_POINTS = "bendPoints";
    @Element
    private PlaceModel                        source;
    @Element
    private PlaceModel                        target;
    @Element
    private PortModel                         port;
    @Element
    private ActionModel                       action;
    @Element
    private GuardModel                        guard;
    @ElementList
    private ArrayList<RelativeBendpointModel> bendpoints;

    protected TransitionModel() {
        action = new ActionModel();
        guard = new GuardModel();
        bendpoints = new ArrayList<RelativeBendpointModel>();
    }

    public TransitionModel copy(PlaceModel source, PlaceModel target, PortModel port,
                    ActionModel action, GuardModel guard) {
        TransitionModel model = this.copy();
        model.source = source;
        model.target = target;
        model.port = port;
        model.action = action;
        model.guard = guard;
        return model;
    }

    public PlaceModel getSource() {
        return source;
    }

    public PlaceModel getTarget() {
        return target;
    }

    public PortModel getPort() {
        return port;
    }

    public void setPort(PortModel port) {
        this.port = port;
        firePropertyChange(PortModel.PORT);
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
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(ActionModel.ACTION, "action"),
                        new TextPropertyDescriptor(GuardModel.GUARD, "guard")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (ActionModel.ACTION.equals(id)) {
            return action.getAction();
        }
        if (GuardModel.GUARD.equals(id)) {
            return guard.getGuard();
        }
        if (PortModel.PORT.equals(id)) {
            return port.getName();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ActionModel.ACTION.equals(id) || GuardModel.GUARD.equals(id)
                        || PortModel.PORT.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (ActionModel.ACTION.equals(id)) {
            setActionString((String) value);
        } else if (GuardModel.GUARD.equals(id)) {
            setGuardString((String) value);
        } else if (PortModel.PORT.equals(id)) {
            // TODO set port
        }
    }

    @Override
    public ArrayList<RelativeBendpointModel> getBendpoints() {
        return bendpoints;
    }

    public TransitionModel setBendpoint(int index, RelativeBendpointModel bendpoint) {
        getBendpoints().set(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    public TransitionModel addBendpoint(int index, RelativeBendpointModel bendpoint) {
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
    public RelativeBendpointModel getBendpoint(int index) {
        return getBendpoints().get(index);
    }
}
