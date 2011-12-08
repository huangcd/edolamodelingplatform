package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortAreaModel;


public class CompoundPriorityAreaTreeEditPart extends BaseTreeEditPart {

    private CompoundPriorityAreaModel getCastedModel() {
        return (CompoundPriorityAreaModel) getModel();
    }

    protected List getModelChildren() {
        return getCastedModel().getChildren(); // return a list of activities
    }

    protected String getText() {
        return getCastedModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(PortAreaModel.CHILDREN))
            refresh();
        else
            refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText("PRIORITIES");
    }

}
