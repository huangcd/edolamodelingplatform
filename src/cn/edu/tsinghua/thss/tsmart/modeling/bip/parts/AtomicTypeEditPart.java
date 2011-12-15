package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.AtomicChildrenEditPolicy;


/**
 * @author huangcd (huangcd.thu@gmail.com)
 */
@SuppressWarnings("rawtypes")
public class AtomicTypeEditPart extends PageContainerEditPart {
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new AtomicChildrenEditPolicy());
    }

    public AtomicTypeModel getModel() {
        return (AtomicTypeModel) super.getModel();
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

    @Override
    protected void performDoubleClick() {}
}
