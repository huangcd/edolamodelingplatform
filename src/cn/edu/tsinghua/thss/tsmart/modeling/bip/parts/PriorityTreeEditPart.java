package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeletePriorityEditPolicy;


public class PriorityTreeEditPart extends BaseTreeEditPart {

    private PriorityModel getCastedModel() {
        return (PriorityModel) getModel();
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

    @Override
    // °²×°edit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeletePriorityEditPolicy());
    }

}
