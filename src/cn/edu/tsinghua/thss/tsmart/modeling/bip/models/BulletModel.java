package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class BulletModel extends BaseModel {
    public final static String       OWNER             = "BulletOwner";
    public final static String       PARENT            = "BulletParent";
    public final static String       TARGET_CONNECTION = "BulletModelTargetConnection";
    private BaseModel                shapeOwner;
    private BaseModel                parent;
    private List<ConnectorPortModel> targetConnections = new ArrayList<ConnectorPortModel>();

    public BulletModel(BaseModel parent, BaseModel shapeOwner) {
        this.parent = parent;
        this.shapeOwner = shapeOwner;
    }

    public PortModel getPortModel() {
        if (shapeOwner instanceof PortModel) {
            return (PortModel) shapeOwner;
        } else if (shapeOwner instanceof ConnectorTypeModel) {
            return ((ConnectorTypeModel) shapeOwner).getExportPort();
        }
        return null;
    }

    public void addTargetConnection(ConnectorPortModel target) {
        targetConnections.add(target);
        firePropertyChange(TARGET_CONNECTION, null, null);
    }

    public BaseModel getOwner() {
        return shapeOwner;
    }

    public BaseModel getParent() {
        return parent;
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    public List<ConnectorPortModel> getTargetConnections() {
        return targetConnections;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    public void removeTargetConnection(ConnectorPortModel target) {
        targetConnections.remove(target);
        firePropertyChange(TARGET_CONNECTION, null, null);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    public void setOwner(BaseModel owner) {
        this.shapeOwner = owner;
        firePropertyChange(OWNER, null, null);
    }

    public void setParent(BaseModel parent) {
        this.parent = parent;
        firePropertyChange(PARENT, null, null);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    // TODO ÏÔÊ¾ownerµÄtext
    @Override
    public String toString() {
        return shapeOwner.toString();
    }

    @Override
    public Element toXML() {
        return null;
    }

    @Override
    public String toBIP() {
        return null;
    }

    @Override
    public BulletModel fromXML() {
        return null;
    }
}
