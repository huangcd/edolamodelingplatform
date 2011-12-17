package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.geometry.Rectangle;
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
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "name")};
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

    @Override
    public Model setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect =
                        positionConstraint.getCopy().setSize(IModel.COMPONENT_WIDTH,
                                        IModel.COMPONENT_HEIGHT);
        return super.setPositionConstraint(rect);
    }
}
