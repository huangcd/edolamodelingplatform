package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteCompoundPriorityEditPolicy;


public class CompoundPriorityTreeEditPart extends BaseTreeEditPart {

    private CompoundPriorityModel getCastedModel() {
        return (CompoundPriorityModel) getModel();
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
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteCompoundPriorityEditPolicy());
    }

}
