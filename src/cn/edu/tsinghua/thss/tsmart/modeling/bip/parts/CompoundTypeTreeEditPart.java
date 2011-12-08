package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ContainerModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteCompoundEditPolicy;


public class CompoundTypeTreeEditPart extends BaseTreeEditPart {

    private CompoundTypeModel getCastedModel() {
        return (CompoundTypeModel) getModel();
    }

    protected List getModelChildren() {
        return getCastedModel().getChildren(); // return a list of activities
    }


    protected String getText() {
        return getCastedModel().getName();
    }

    @Override
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

    /*
     * public void refreshChildren() { List children=getModelChildren(); if(children!=null) for(int
     * i=0;i<children.size();i++) {
     * 
     * System.out.println(((BaseModel)children.get(i)).toString()); } }
     */

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteCompoundEditPolicy());
    }
}
