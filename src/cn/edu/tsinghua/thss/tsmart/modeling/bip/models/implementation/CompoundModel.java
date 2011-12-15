package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ÏÂÎç9:15<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class CompoundModel extends BaseInstanceModel<CompoundModel, CompoundTypeModel, IContainer>
                implements
                    IComponentInstance<CompoundModel, CompoundTypeModel, IContainer> {

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return String.format("component %s %s", getType().getName(), getName());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
    }
}
