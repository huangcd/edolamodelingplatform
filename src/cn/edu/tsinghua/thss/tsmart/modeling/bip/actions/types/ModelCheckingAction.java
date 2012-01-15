package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking.ModelCheckingDialog;

public class ModelCheckingAction extends OpenDialogAction {

    public static final String ID = ModelCheckingAction.class.getCanonicalName();

    public ModelCheckingAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ModelCheckingAction_0, Messages.ModelCheckingAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        ModelCheckingDialog dialog = new ModelCheckingDialog(getShell());
        dialog.open();
    }
}
