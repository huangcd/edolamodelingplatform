package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.draw2d.geometry.Rectangle;

public class BipModel extends BaseModel {
    private static List<BipModel> models   = new ArrayList<BipModel>();
    private List<BaseModel>       children = new ArrayList<BaseModel>();
    public final static String    CHILDREN = "BipChildren";

    public BipModel() {
        models.add(this);
    }

    // TODO 临时解决导出的办法
    public static BipModel getModel() {
        return models.get(0);
    }

    public boolean addChild(BaseModel model) {
        children.add(model);
        firePropertyChange(CHILDREN, null, null);
        return true;
    }

    public List<BaseModel> getChildren() {
        return children;
    }

    public boolean removeChild(BaseModel model) {
        children.remove(model);
        firePropertyChange(CHILDREN, null, null);
        return true;
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
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {}

    private List<BaseModel> getAllChildren() {
        List<BaseModel> children = new ArrayList<BaseModel>();
        Stack<BaseModel> stack = new Stack<BaseModel>();
        assert (getChildren().size() == 1);
        stack.push(getChildren().get(0));
        children.add(getChildren().get(0));
        while (!stack.isEmpty()) {
            BaseModel model = stack.pop();
            if (model instanceof AtomicTypeModel) {
                for (BaseModel child : ((AtomicTypeModel) model).getChildren()) {
                    if (child instanceof AtomicTypeModel || child instanceof CompoundTypeModel) {
                        children.add(child);
                        stack.push(child);
                    }
                }
            }
            if (model instanceof CompoundTypeModel) {
                for (BaseModel child : ((CompoundTypeModel) model).getChildren()) {
                    if (child instanceof AtomicTypeModel || child instanceof CompoundTypeModel) {
                        children.add(child);
                        stack.push(child);
                    } else if (child instanceof ConnectorTypeModel) {
                        children.add(child);
                    }
                }
            }
        }
        return children;
    }

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("model");
        Element portTypes = element.addElement("portTypes");
        Element connectorTypes = element.addElement("connectorTypes");
        Element atomicTypes = element.addElement("atomicTypes");
        Element compoundTypes = element.addElement("compoundTypes");
        for (BaseModel model : getAllChildren()) {
            if (model instanceof AtomicTypeModel) {
                atomicTypes.add(model.toXML());
            } else if (model instanceof ConnectorTypeModel) {
                connectorTypes.add(model.toXML());
            } else if (model instanceof CompoundTypeModel) {
                compoundTypes.add(model.toXML());
            }
        }
        PortTypeModel.reset();
        // port type
        // TODO 考虑用类似 Atomic、Compound、Connector 之类的方法
        for (PortTypeModel portType : PortTypeModel.getAllPortTypeModels()) {
            portTypes.add(portType.toXML());
        }

        Element compound = element.addElement("compound");
        CompoundTypeModel model = (CompoundTypeModel) getChildren().get(0);
        compound.addAttribute("componentTypeID", model.getId());
        compound.addAttribute("name", "__ALL__");
        Rectangle rect = model.getPositionConstraint();
        compound.addAttribute("x", Integer.toString(rect.x()));
        compound.addAttribute("y", Integer.toString(rect.y()));
        compound.addAttribute("width", Integer.toString(rect.width()));
        compound.addAttribute("height", Integer.toString(rect.height()));
        return element;
    }

    @Override
    public String toBIP() {
        // TODO
        return null;
    }

    @Override
    public BipModel fromXML() {
        // TODO
        return null;
    }

}
