package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

/**
 * 小圆点，用于表示export port
 * 
 * @author huangcd
 * 
 */
@SuppressWarnings("rawtypes")
@Root
public class BulletModel extends BaseInstanceModel<BulletModel, IType, IContainer> {
    private static final long     serialVersionUID = -3202298194927737177L;
    @Element
    private PortModel             port;
    @Attribute
    private int                   direction;
    @ElementList
    private List<ConnectionModel> targetConnections;

    public BulletModel(PortModel portModel) {
        port = portModel;
        targetConnections = new ArrayList<ConnectionModel>();
    }

    public PortModel getPort() {
        return port;
    }

    public String getPortDescription() {
        if (port.getParent() instanceof ConnectorTypeModel) {
            return port.getType().getName() + " "
                            + ((ConnectorTypeModel) port.getParent()).getInstance().getName();
        } else {
            return port.getType().getName() + " " + port.getName();
        }
    }

    public String getPortTypeName() {
        PortModel port = getPort();
        if (port == null) {
            return "";
        }
        return port.getType().getName();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
    public BulletModel setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = positionConstraint.getCopy().setSize(BULLET_RADIUS * 2, BULLET_RADIUS * 2);
        return super.setPositionConstraint(rect);
    }

    public List<ConnectionModel> getTargetConnections() {
        return targetConnections;
    }

    public BulletModel addTargetConnection(ConnectionModel connectionModel) {
        targetConnections.add(connectionModel);
        firePropertyChange(TARGET);
        return this;
    }

    public BulletModel removeTargetConnection(ConnectionModel connection) {
        targetConnections.remove(connection);
        firePropertyChange(TARGET);
        return this;
    }
}
