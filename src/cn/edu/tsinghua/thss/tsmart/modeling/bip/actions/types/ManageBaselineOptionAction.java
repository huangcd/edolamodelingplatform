package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.BaselineOptionDialog;

public class ManageBaselineOptionAction extends OpenDialogAction {

    public static final String ID = ManageBaselineOptionAction.class.getCanonicalName();

    public ManageBaselineOptionAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageBaselineOptionAction_0,
            Messages.ManageBaselineOptionAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return new BaselineOptionDialog(getShell());
    }
}