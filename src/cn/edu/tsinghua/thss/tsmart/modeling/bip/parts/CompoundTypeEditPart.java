package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CompoundChildrenEditPolicy;


/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-21 ����03:44:22
 * @project CereusBip
 * @package cereusbip.parts
 * @class ContentsEditPart.java
 */
@SuppressWarnings("rawtypes")
public class CompoundTypeEditPart extends PageContainerEditPart {
    @Override
    // ��װ edit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new CompoundChildrenEditPolicy());
        // installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CustomDirectEditPolicy()); // ֱ�ӱ༭
        // installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteAtomicEditPolicy());
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

    // ˫���༭
    @Override
    protected void performDoubleClick() {
        // AtomicTypeModel child = (AtomicTypeModel) getModel();
        //
        // Shell shell = Display.getDefault().getActiveShell();
        // if (shell != null) {
        // EditAtomicTypeDialog dialog = new EditAtomicTypeDialog(shell, child,
        // child.getParent());
        // dialog.setBlockOnOpen(true);
        // dialog.open();
        // }
    }
}
