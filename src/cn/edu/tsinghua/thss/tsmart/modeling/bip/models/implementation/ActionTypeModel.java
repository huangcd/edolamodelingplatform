package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-5<br/>
 * Time: ÏÂÎç7:27<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class ActionTypeModel extends BaseTypeModel<ActionTypeModel, ActionModel, IContainer> {

    @Override
    public ActionModel createInstance() {
        if (instance == null) {
            instance = new ActionModel().setType(this);
        }
        return instance;
    }

    @Override
    public ActionTypeModel copy() {
        return new ActionTypeModel().setName("CopyOf" + getName());
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        System.err.println("getEditableValue");
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        System.err.println("getPropertyDescriptors");
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        System.err.println("getPropertyValue");
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        System.err.println("isPropertySet");
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub
        System.err.println("resetPropertyValue");
        
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        System.err.println("setPropertyValue");
        // TODO Auto-generated method stub
        
    }
}
