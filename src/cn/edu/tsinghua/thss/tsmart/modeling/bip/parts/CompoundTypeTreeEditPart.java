package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;

@SuppressWarnings("rawtypes")
public class CompoundTypeTreeEditPart extends BaseTreeEditPart {

    private CompoundTypeModel getCastedModel() {
        return (CompoundTypeModel) getModel();
    }
    protected List<IInstance> getModelChildren() {
        return getCastedModel().getChildren(); // return a list of activities
    }
    protected String getText() {
        return getCastedModel().getName();
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
