package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ConnectorPortModel extends BaseConnectionModel {
    private CompoundTypeModel  parent;
    private ConnectorTypeModel source;
    private BulletModel        target;
    private String             name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        firePropertyChange(REFRESH, null, null);
    }

    public CompoundTypeModel getParent() {
        return parent;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public ConnectorTypeModel getSource() {
        return source;
    }

    public void setSource(ConnectorTypeModel source) {
        this.source = source;
    }

    public BulletModel getTarget() {
        return target;
    }

    public void setTarget(BulletModel target) {
        this.target = target;
    }

    public void attachSource() {
        if (!source.getSourceConnections().contains(this)) source.addSourceConnection(this);
    }

    public void detachSource() {
        source.removeSourceConnection(this);
    }

    public void attachTarget() {
        if (!target.getTargetConnections().contains(this)) target.addTargetConnection(this);
    }

    public void detachTarget() {
        target.removeTargetConnection(this);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) return name;
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor[] descriptors =
                        new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "name"),};
        return descriptors;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("portArg");
        element.addAttribute("name", getName());
        element.addAttribute("portTypeID", getTarget().getPortModel().getPortTypeID());
        return element;
    }

    @Override
    public String toBIP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseModel fromXML() {
        // TODO Auto-generated method stub
        return null;
    }
}
