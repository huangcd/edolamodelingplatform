package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;

@SuppressWarnings("rawtypes")
public class AtomicTreeEditPart extends BaseTreeEditPart {

    public AtomicModel getCastedModel() {
        return (AtomicModel) getModel();
    }
    @Override
    protected List<IInstance> getModelChildren() {
        return getCastedModel().getType().getChildren();
    }

    protected String getText() {
        return getCastedModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IModel.CHILDREN)) refresh();
        if (evt.getPropertyName().equals(IModel.NAME)) refreshVisuals();
        else 
            refresh();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(BIPEditor.getImage("icons/atomic_16.png").createImage());
    }
}
