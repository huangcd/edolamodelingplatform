package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BulletModel;


public class BulletTreeEditPart extends BaseTreeEditPart {

    private BulletModel getCastedModel() {
        return (BulletModel) getModel();
    }


    protected String getText() {
        return getCastedModel().toString();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        // refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
    }

}
