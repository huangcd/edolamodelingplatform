package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ÏÂÎç3:14<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class ActionModel extends BaseInstanceModel<ActionModel, ActionTypeModel, IContainer> {

    public final static String ACTION = "action";
    @Element(required = false)
    private String             action;

    public String getAction() {
        return action;
    }

    public ActionModel setAction(String action) {
        this.action = action;
        firePropertyChange(CHILDREN);
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return getAction();
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(ACTION, "action")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (id.equals(ACTION)) {
            return action;
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return id.equals(ACTION);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (id.equals(ACTION)) {
            setAction((String) value);
        }
    }
}
