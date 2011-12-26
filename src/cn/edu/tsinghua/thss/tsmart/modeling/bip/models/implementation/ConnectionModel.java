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

    protected ConnectionModel(int index) {
        bendpoints = new ArrayList<Bendpoint>();
        argumentIndex = index;
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
        if (source == null) return "";
        ConnectorTypeModel connectorType = source.getType();
        return connectorType.getArgumentAsString(argumentIndex).replaceAll("\\(.*\\)", "");
    }

    public ConnectorModel attachSource() {
        if (!source.getSourceConnections().contains(this)) {
            source.addSourceConnection(this);
        }
        return source;
    }

    public String getTypeName() {
        return getSource().getType().getArgumentType(argumentIndex);
    }

    public BulletModel attachTarget() {
        if (!target.getTargetConnections().contains(this)) {
            target.addTargetConnection(this);
            // 绑定参数
            if (!(target instanceof InvisibleBulletModel)) {
                source.bound(argumentIndex, target.getPort());
            }
        }
        return target;
    }

    public ConnectorModel detachSource() {
        source.removeSourceConnection(this);
        return source;
    }

    public BulletModel detachTarget() {
        target.removeTargetConnection(this);
        if (target instanceof InvisibleBulletModel) {
            source.getParent().removeBullet((InvisibleBulletModel) target);
        }
        return target;
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
        return new IPropertyDescriptor[0];
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

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
