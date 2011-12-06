package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ContainerModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteAtomicEditPolicy;


public class AtomicTypeTreeEditPart extends BaseTreeEditPart {

    private AtomicTypeModel getCastedModel() {
        return (AtomicTypeModel) getModel();
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

    // TODO bug
    public void refreshChildren() {
        List children = getModelChildren();
        if (children != null)
            for (int i = 0; i < children.size(); i++) {
                System.out.println(i + " " + ((BaseModel) children.get(i)).toString());
                if (children.get(i) instanceof AtomicTypeModel
                                || children.get(i).equals(getCastedModel())) {
                    System.out.println(i + " " + ((BaseModel) children.get(i)).toString());
                    getCastedModel().removeChild(getCastedModel().getChildren().get(0));
                }
            }
        //

        super.refreshChildren();
    }

    @Override
    // װedit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteAtomicEditPolicy());
    }

}
