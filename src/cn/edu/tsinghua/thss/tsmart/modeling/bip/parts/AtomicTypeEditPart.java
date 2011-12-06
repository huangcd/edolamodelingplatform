package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.AtomicChildEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CustomDirectEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteAtomicEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditAtomicTypeDialog;

/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-21 下午03:44:22
 * @project CereusBip
 * @package cereusbip.parts
 * @class ContentsEditPart.java
 */
public class AtomicTypeEditPart extends ContainerEditPart {
    @Override
    // 安装 edit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new AtomicChildEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CustomDirectEditPolicy()); // 直接编辑
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteAtomicEditPolicy());
    }

    // 双击编辑
    @Override
    protected void performDoubleClick() {
        AtomicTypeModel child = (AtomicTypeModel) getModel();

        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditAtomicTypeDialog dialog = new EditAtomicTypeDialog(shell, child, child.getParent());
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }
}
