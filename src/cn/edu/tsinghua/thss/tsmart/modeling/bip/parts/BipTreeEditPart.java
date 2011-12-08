package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BipModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ContainerModel;


public class BipTreeEditPart extends BaseTreeEditPart {
    private BipModel getCastedModel() {
        return (BipModel) getModel();
    }

    protected List getModelChildren() {
        return getCastedModel().getChildren(); // return a list of activities
    }

    protected String getText() {
        return getCastedModel().getName();
    }

    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ContainerModel.CHILDREN)) refresh();
        if (evt.getPropertyName().equals(BaseModel.NAME)) refreshVisuals();
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
