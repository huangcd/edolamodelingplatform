package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public class ComponentModel<Model extends BaseInstanceModel, Type extends IComponentType>
                extends BaseInstanceModel<Model, Type, CompoundTypeModel>
                implements
                    IComponentInstance<Model, Type, CompoundTypeModel> {

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
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();   
        TextPropertyDescriptor name= new TextPropertyDescriptor(NAME, "组件名");   
        name.setDescription("01");
        properties.add(name);
        ComboBoxPropertyDescriptor tag=new ComboBoxPropertyDescriptor(TAG, "标签", COMPONENT_TAGS);
        tag.setDescription("02");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        } else if (TAG.equals(id)) {
            return getTag() == null ? 0 : Arrays.asList(COMPONENT_TAGS).indexOf(getTag());
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || TAG.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (TAG.equals(id)) {
            int index = (Integer) value;
            if (index == 0)
                setTag(null);
            else
                setTag(COMPONENT_TAGS[index]);
        }
    }

    @Override
    public Model setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect =
                        positionConstraint.getCopy().setSize(IModel.COMPONENT_WIDTH,
                                        IModel.COMPONENT_HEIGHT);
        return super.setPositionConstraint(rect);
    }
}
