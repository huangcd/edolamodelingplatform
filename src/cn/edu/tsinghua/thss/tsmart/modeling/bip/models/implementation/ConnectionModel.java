package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class ConnectionModel
                extends BaseInstanceModel<ConnectionModel, ConnectionTypeModel, CompoundTypeModel>
                implements
                    IConnection<ConnectionModel, CompoundTypeModel, ConnectorModel, BulletModel> {

    public static final String   BEND_POINTS             = "bendPoints";
    public static final String   REMOVE_TRANSITION_LABEL = "removeTransitionLabels";
    @Element
    private ConnectorModel       source;
    @Element
    private BulletModel          target;
    @ElementList
    private ArrayList<Bendpoint> bendpoints;
    @Element
    private int                  argumentIndex;

    protected ConnectionModel() {
        bendpoints = new ArrayList<Bendpoint>();
    }

    public ConnectorModel getSource() {
        return source;
    }

    public BulletModel getTarget() {
        return target;
    }

    public void setSource(ConnectorModel source) {
        this.source = source;
    }

    public void setTarget(BulletModel target) {
        this.target = target;
    }

    /**
     * 友好的信息显示方式
     * 
     * @return
     */
    public String getFriendlyString() {
        // TODO
        return "";
    }

    public ConnectorModel attachSource() {
        if (!source.getSourceConnections().contains(this)) {
            source.addSourceConnection(this);
        }
        return source;
    }

    public BulletModel attachTarget() {
        if (!target.getTargetConnections().contains(this)) {
            target.addTargetConnection(this);
        }
        return target;
    }

    public ConnectorModel detachSource() {
        source.removeSourceConnection(this);
        return source;
    }

    public BulletModel detachTarget() {
        target.removeTargetConnection(this);
        return target;
    }

    public boolean isLoop() {
        return source != null && source.equals(target);
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO
        return new IPropertyDescriptor[0];
        // List<PortModel> ports = getParent().getPorts();
        // String[] values = new String[ports.size() + 1];
        // for (int i = 0, size = ports.size(); i < size; i++) {
        // values[i] = ports.get(i).getName();
        // }
        // values[ports.size()] = "$UNBOUNDED$";
        // return new IPropertyDescriptor[] {new TextPropertyDescriptor(ActionModel.ACTION, "动作"),
        // new TextPropertyDescriptor(GuardModel.GUARD, "门卫函数"),
        // new ComboBoxPropertyDescriptor(IModel.PORT, "端口", values)};
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO
    }

    @Override
    public ArrayList<Bendpoint> getBendpoints() {
        return bendpoints;
    }

    public ConnectionModel setBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().set(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    public ConnectionModel addBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().add(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public ConnectionModel removeBendpoint(int index) {
        getBendpoints().remove(index);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public Bendpoint getBendpoint(int index) {
        return getBendpoints().get(index);
    }
}
