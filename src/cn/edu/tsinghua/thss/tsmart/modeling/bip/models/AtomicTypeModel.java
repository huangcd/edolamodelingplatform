package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author : huangcd (huangcd.thu@gmail.com)
 * @time : 2011-6-25 下午10:42:12
 * @project : CereusBip
 * @package : cereusbip.models
 * @class : AtomicModel.java
 */
public class AtomicTypeModel extends ContainerModel {
    private CompoundTypeModel   parent;
    private String              initAction  = "";
    private DataAreaModel       dataAreaModel;
    private PortAreaModel       portAreaModel;
    private PriorityAreaModel   priorityAreaModel;
    private static final String INIT_ACTION = "AtomicInitAction";

    public AtomicTypeModel(String name) {
        setName(name);
        dataAreaModel = new DataAreaModel();
        dataAreaModel.setParent(this);
        dataAreaModel.setPositionConstraint(new Rectangle(25, 35, 40, 80));
        priorityAreaModel = new PriorityAreaModel();
        priorityAreaModel.setPositionConstraint(new Rectangle(165, 35, 60, 80));
        priorityAreaModel.setParent(this);
        portAreaModel = new PortAreaModel();
        portAreaModel.setParent(this);
        portAreaModel.setPositionConstraint(new Rectangle(305, 35, 60, 80));
        PortModel idlePort = new PortModel();
        idlePort.setParent(portAreaModel);
        idlePort.setName("idle");
        idlePort.setOwner(this);
        idlePort.setExport(true);// 此处添加了bulletModel
        portAreaModel.addChild(idlePort);
        PlaceModel initialPlace = new PlaceModel("INIT", this);
        initialPlace.setParent(this);
        initialPlace.setPositionConstraint(new Rectangle(100, 100, -1, -1));
        initialPlace.setInitialPlace(true);

        // clearChildren();
        addChild(initialPlace);
        addChild(dataAreaModel);
        addChild(portAreaModel);
        addChild(priorityAreaModel);
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "atomic name"),
                        new TextPropertyDescriptor(INIT_ACTION, "init action")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return getName();
        }
        if (INIT_ACTION.equals(id)) {
            return initAction;
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || INIT_ACTION.equals(id);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        }
        if (INIT_ACTION.equals(id)) {
            setInitAction((String) value);
        }
    }

    public String getInitAction() {
        return initAction;
    }

    public void setInitAction(String initAction) {
        this.initAction = initAction;
        firePropertyChange(INIT_ACTION, null, null);
    }

    public DataAreaModel getDataAreaModel() {
        return dataAreaModel;
    }

    public PortAreaModel getPortAreaModel() {
        return portAreaModel;
    }

    public PriorityAreaModel getPriorityAreaModel() {
        return priorityAreaModel;
    }

    public CompoundTypeModel getParent() {
        return parent;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("atomicType");
        element.addAttribute("id", getId());
        element.addAttribute("name", getName());
        Rectangle rect = getDataAreaModel().getPositionConstraint();
        element.addAttribute("dataBoxX", Integer.toString(rect.x()));
        element.addAttribute("dataBoxY", Integer.toString(rect.y()));
        element.addAttribute("dataBoxWidth", Integer.toString(rect.width()));
        element.addAttribute("dataBoxHeight", Integer.toString(rect.height()));

        rect = getPortAreaModel().getPositionConstraint();
        element.addAttribute("portBoxX", Integer.toString(rect.x()));
        element.addAttribute("portBoxY", Integer.toString(rect.y()));
        element.addAttribute("portBoxWidth", Integer.toString(rect.width()));
        element.addAttribute("portBoxHeight", Integer.toString(rect.height()));

        rect = getPriorityAreaModel().getPositionConstraint();
        element.addAttribute("priorityBoxX", Integer.toString(rect.x()));
        element.addAttribute("priorityBoxY", Integer.toString(rect.y()));
        element.addAttribute("priorityBoxWidth", Integer.toString(rect.width()));
        element.addAttribute("priorityBoxHeight", Integer.toString(rect.height()));

        element.add(getDataAreaModel().toXML());
        element.add(portAreaModel.toXML());
        Element places = element.addElement("places");
        Element initBlock = element.addElement("initBlock");
        Element transitions = element.addElement("transitions");
        for (BaseModel child : getChildren()) {
            if (child instanceof PlaceModel) {
                PlaceModel place = (PlaceModel) child;
                places.add(place.toXML());
                if (place.isInitialPlace()) {
                    initBlock.addElement("placeRef").addAttribute("placeID", place.getId());
                    initBlock.addElement("action").addText(initAction);
                }
                for (TransitionModel transitionModel : place.getSourceConnections()) {
                    transitions.add(transitionModel.toXML());
                }
            }
        }
        element.add(priorityAreaModel.toXML());
        return element;
    }

    @Override
    public String toBIP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AtomicTypeModel fromXML() {
        // TODO Auto-generated method stub
        return null;
    }
}
