package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
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
    implements IConnection<TransitionModel, AtomicTypeModel, PlaceModel, PlaceModel> {

    public static final String BEND_POINTS = "bendPoints";
    @Element
    private PlaceModel source;
    @Element
    private PlaceModel target;
    @Element
    private PortModel port;
    @Element
    private ActionModel action;
    @Element
    private GuardModel guard;

    /** action 的标签，用于显示 */
    private LabelModel actionLabel;
    /** guard 的标签，用于显示 */
    private LabelModel guardLabel;
    /** port 的标签，用于显示 */
    private LabelModel portLabel;

    public TransitionModel copy(
        PlaceModel source,
        PlaceModel target,
        PortModel port,
        ActionModel action,
        GuardModel guard) {
        TransitionModel model = this.copy();
        model.source = source;
        model.target = target;
        model.port = port;
        model.action = action;
        model.guard = guard;
        return model;
    }

    protected TransitionModel() {
        action = new ActionModel();
        guard = new GuardModel();
        actionLabel = new LabelModel();
        guardLabel = new LabelModel();
        portLabel = new LabelModel();
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
        firePropertyChange(CHILDREN);
    }

    public void setSource(PlaceModel source) {
        this.source = source;
    }

    public void setTarget(PlaceModel target) {
        this.target = target;
    }

    public ActionModel getAction() {
        return action;
    }

    public void setAction(ActionModel action) {
        this.action = action;
        firePropertyChange(CHILDREN);
    }

    public GuardModel getGuard() {
        return guard;
    }

    public void setGuard(GuardModel guard) {
        this.guard = guard;
        firePropertyChange(CHILDREN);
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

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return String.format("on %s from %s to %s provided %s do {%s}",
                             getPort().getName(),
                             getSource().getName(),
                             getTarget().getName(),
                             getGuard().exportToBip(),
                             getAction().exportToBip());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[]{new TextPropertyDescriptor(ActionModel.ACTION, "action"),
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
        return ActionModel.ACTION.equals(id) || GuardModel.GUARD.equals(id) || PortModel.PORT
            .equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (ActionModel.ACTION.equals(id)) {
            action.setAction((String) value);
            actionLabel.setLabel(action.getAction());
        } else if (GuardModel.GUARD.equals(id)) {
            guard.setGuard((String) value);
            guardLabel.setLabel(guard.getGuard());
        } else if (PortModel.PORT.equals(id)) {
            // TODO set port
        }
    }

    public LabelModel getActionLabel() {
        return actionLabel;
    }

    public LabelModel getGuardLabel() {
        return guardLabel;
    }

    public LabelModel getPortLabel() {
        return portLabel;
    }
}
