package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CompoundChildrenEditPolicy;

@SuppressWarnings("rawtypes")
public class CompoundTypeEditPart extends PageContainerEditPart {
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new CompoundChildrenEditPolicy());
    }

    public CompoundTypeModel getModel() {
        return (CompoundTypeModel) super.getModel();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        return getModel().getChildren();
    }

    @Override
    public void refresh() {
        refreshChildren();
        super.refresh();
    }

    // Ë«»÷±à¼­
    @Override
    protected void performDoubleClick() {}
}
