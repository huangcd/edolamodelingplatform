package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.BaselineOptionDialog;

public class ManageBaselineOptionAction extends OpenDialogAction {

    public static final String ID = ManageBaselineOptionAction.class.getCanonicalName();

    public ManageBaselineOptionAction(IWorkbenchWindow window) {
        super(window, ID, "\u8BBE\u7F6E\u57FA\u51C6\u7EBF\u68C0\u67E5\u89C4\u5219",
            "\u8BBE\u7F6E\u57FA\u51C6\u7EBF\u68C0\u67E5\u89C4\u5219", null);
    }

    @Override
    protected Dialog getDialog() {
        return new BaselineOptionDialog(getShell());
    }
}