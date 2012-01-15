package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.CompoundTypeManageDialog;

public class ManageCompoundTypeAction extends OpenDialogAction {
    public static final String ID = ManageCompoundTypeAction.class.getCanonicalName();

    public ManageCompoundTypeAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageCompoundTypeAction_0, Messages.ManageCompoundTypeAction_1, "icons/compound_16.png"); //$NON-NLS-3$
    }

    @Override
    protected Dialog getDialog() {
        return new CompoundTypeManageDialog(getShell());
    }
}
