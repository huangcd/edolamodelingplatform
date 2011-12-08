package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.List;

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

    public final static String INITIAL = "initial";
    public final static String SOURCE  = "source";
    public final static String TARGET  = "target";

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
        if (NAME.equals(id)) return getName();
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
        // TODO Auto-generated method stub
        return null;
    }
}
