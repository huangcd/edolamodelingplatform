package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:07<br/>
 */
@Root
public class PlaceModel extends BaseInstanceModel<PlaceModel, PlaceTypeModel, AtomicTypeModel> {

    public final static String    SOURCE  = "source";
    public final static String    TARGET  = "target";
    public final static Dimension SIZE    = new Dimension(20, 20);
    private LabelModel            label;

    protected PlaceModel() {
        label = new LabelModel();
        label.setTextColor(ColorConstants.blue);
    }

    @Override
    public String exportToBip() {
        return String.format("place %s", getName());
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public boolean isInitialPlace() {
        return getParent().getInitPlace().equals(this);
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "place name")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        }
    }

    public PlaceModel setName(String name) {
        label.setLabel(name);
        firePropertyChange(CHILDREN);
        return super.setName(name);
    }

    public List<TransitionModel> getSourceConnections() {
        // TODO Auto-generated method stub
        return null;
    }

    public List<TransitionModel> getTargetConnections() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 更改对应标签位置
     * 
     * @param rect place的位置
     */
    private void setLabelPositionConstraint(Rectangle rect) {
        Dimension size = label.getLabelSize();
        Point location = new Point(rect.x + rect.width / 2 - size.width / 2, rect.y - size.height);
        label.setPositionConstraint(new Rectangle(location, size));

    }

    @Override
    public PlaceModel setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = new Rectangle(positionConstraint.getLocation(), SIZE);
        if (rect.equals(getPositionConstraint())) {
            return this;
        }
        setLabelPositionConstraint(rect);
        return super.setPositionConstraint(rect);
    }

    public LabelModel getLabel() {
        return label;
    }

    public void setLabel(LabelModel label) {
        this.label = label;
    }
}
