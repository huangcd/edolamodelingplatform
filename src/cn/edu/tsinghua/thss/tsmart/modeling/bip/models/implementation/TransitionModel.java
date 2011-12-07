package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ÏÂÎç3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked"})
@Root
public class TransitionModel
    extends BaseInstanceModel<TransitionModel, TransitionTypeModel, AtomicTypeModel>
    implements IConnection<TransitionModel, AtomicTypeModel, PlaceModel, PlaceModel> {

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

    @Override
    public void attachSource(PlaceModel source) {
        this.source = source;
        firePropertyChange(CHILDREN);
    }

    @Override
    public void attachTarget(PlaceModel target) {
        this.target = target;
        firePropertyChange(CHILDREN);
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
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
        
    }
}
