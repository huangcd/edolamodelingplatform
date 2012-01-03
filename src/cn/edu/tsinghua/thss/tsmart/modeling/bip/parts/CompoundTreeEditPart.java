package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundModel;

@SuppressWarnings("rawtypes")
public class CompoundTreeEditPart extends BaseTreeEditPart {

    private CompoundModel getCastedModel() {
        return (CompoundModel) getModel();
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
        if (evt.getPropertyName().equals(IModel.NAME))
            refreshVisuals();
        else if (evt.getPropertyName().equals(IModel.CHILDREN))
            refresh();
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
        setWidgetImage(BIPEditor.getImage("icons/compound_16.png").createImage());
    }

}
