package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ÏÂÎç3:14<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class ActionModel extends BaseInstanceModel<ActionModel, BaseTypeModel, IContainer> {
    private static final long serialVersionUID = 1851129094881910942L;
    @Element(required = false)
    private String            action           = "";

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
        return getAction().replaceAll("[\r\n]+\t*", "\n\t\t\t");
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor action = new TextPropertyDescriptor(ACTION, "action");
        action.setDescription("01");
        properties.add(action);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "±êÇ©");
        tag.setDescription("02");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (id.equals(ACTION)) {
            return action;
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return ENTITY.equals(id) || id.equals(ACTION);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (id.equals(ACTION)) {
            setAction((String) value);
        }
        if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>)value);
        }
    }
}
