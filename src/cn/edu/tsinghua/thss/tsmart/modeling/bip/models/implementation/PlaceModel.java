package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Root;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ÏÂÎç7:07<br/>
 */
@Root
public class PlaceModel extends BaseInstanceModel<PlaceModel, PlaceTypeModel, AtomicTypeModel> {

    public final static String    SOURCE = "source";
    public final static String    TARGET = "target";
    public final static Dimension SIZE   = new Dimension(20, 20);
    private List<TransitionModel> sourceConnections;
    private List<TransitionModel> targetConnections;

    public PlaceModel() {
        sourceConnections = new ArrayList<TransitionModel>();
        targetConnections = new ArrayList<TransitionModel>();
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
        return getParent() != null && getParent().getInitPlace().equals(this);
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "×´Ì¬Ãû")};
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

    public List<TransitionModel> getSourceConnections() {
        return sourceConnections;
    }

    public List<TransitionModel> getTargetConnections() {
        return targetConnections;
    }

    public PlaceModel addSourceConnection(TransitionModel connection) {
        sourceConnections.add(connection);
        firePropertyChange(SOURCE);
        return this;
    }

    public PlaceModel removeSourceConnection(TransitionModel connection) {
        boolean result = sourceConnections.remove(connection);
        if (result) {
            firePropertyChange(SOURCE);
        }
        return this;
    }

    public PlaceModel removeTargetConnection(TransitionModel connection) {
        boolean result = targetConnections.remove(connection);
        if (result) {
            firePropertyChange(TARGET);
        }
        return this;
    }

    public PlaceModel addTargetConnection(TransitionModel connection) {
        targetConnections.add(connection);
        firePropertyChange(TARGET);
        return this;
    }

    @Override
    public PlaceModel setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = new Rectangle(positionConstraint.getLocation(), SIZE);
        if (rect.equals(getPositionConstraint())) {
            return this;
        }
        return super.setPositionConstraint(rect);
    }
}
