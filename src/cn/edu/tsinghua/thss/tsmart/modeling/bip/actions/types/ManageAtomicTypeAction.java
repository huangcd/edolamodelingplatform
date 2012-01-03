package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AtomicTypeManageDialog;

public class ManageAtomicTypeAction extends OpenDialogFromMenuAction {
    public static final String ID = ManageAtomicTypeAction.class.getCanonicalName();

    public ManageAtomicTypeAction(IWorkbenchWindow window) {
        super(window, ID, "管理原子组件类型", "查看或删除原子组件类型", "icons/atomic_16.png");
    }

    @Override
    protected Dialog getDialog() {
        return new AtomicTypeManageDialog(getShell());
    }
}
