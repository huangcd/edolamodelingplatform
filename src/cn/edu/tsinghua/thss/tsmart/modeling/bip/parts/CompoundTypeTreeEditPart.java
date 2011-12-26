package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;

@SuppressWarnings("rawtypes")
public class CompoundTypeTreeEditPart extends BaseTreeEditPart {

    public CompoundTypeModel getModel() {
        return (CompoundTypeModel) super.getModel();
    }

    protected List<IInstance> getModelChildren() {
        List<IInstance> modelChildren = new ArrayList<IInstance>();
        for (IInstance instance : getModel().getChildren()) {
            if (instance instanceof InvisibleBulletModel) {
                continue;
            }
            modelChildren.add(instance);
        }
        return modelChildren;
    }

    protected String getText() {
        return getModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IModel.CHILDREN)) refresh();
        if (evt.getPropertyName().equals(IModel.NAME)) refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
    }

}
