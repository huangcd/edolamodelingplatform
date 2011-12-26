package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;

@SuppressWarnings("rawtypes")
public class AtomicTypeTreeEditPart extends BaseTreeEditPart {

    public AtomicTypeModel getCastedModel() {
        return (AtomicTypeModel) getModel();
    }

    protected List<IInstance> getModelChildren() {
        return getCastedModel().getChildren(); // return a list of activities
    }

    protected String getText() {
        return getCastedModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IModel.CHILDREN)) refresh();
        if (evt.getPropertyName().equals(IModel.NAME)) refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
    }

    public void refreshChildren() {
        List children = getModelChildren();
        if (children != null) for (int i = 0; i < children.size(); i++) {
            if (children.get(i) instanceof AtomicTypeModel || children.get(i).equals(getModel())) {
                getCastedModel().removeChild(getCastedModel().getChildren().get(0));
            }
        }
        super.refreshChildren();
    }

    @Override
    // װedit policy
    protected void createEditPolicies() {
        // installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteAtomicEditPolicy());
    }

}
