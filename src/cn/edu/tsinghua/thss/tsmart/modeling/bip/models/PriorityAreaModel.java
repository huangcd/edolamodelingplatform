/**
 *
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-7-3 ÉÏÎç10:29:01
 * @project CereusBip
 * @package cereusbip.models
 * @class PriorityAreaModel.java
 */
public class PriorityAreaModel extends ListContainerModel<PriorityModel, AtomicTypeModel> {
    public PriorityAreaModel() {
        setPositionConstraint(new Rectangle(0, 0, 40, 80));
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

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("priorities");
        for (PriorityModel model : getChildren()) {
            element.add(model.toXML());
        }
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
