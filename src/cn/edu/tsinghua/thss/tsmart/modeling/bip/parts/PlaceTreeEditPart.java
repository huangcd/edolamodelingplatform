package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeletePlaceEditPolicy;


public class PlaceTreeEditPart extends BaseTreeEditPart {

    private PlaceModel getCastedModel() {
        return (PlaceModel) getModel();
    }


    protected String getText() {
        if (getCastedModel().getName() == null) return "";
        return getCastedModel().getName();
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

    @Override
    // °²×°edit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeletePlaceEditPolicy());
    }

}
