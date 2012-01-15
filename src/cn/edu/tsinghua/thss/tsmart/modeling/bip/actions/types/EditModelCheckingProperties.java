package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking.EditModelCheckingPropertiesDialog;

public class EditModelCheckingProperties extends OpenDialogAction {

    public static final String ID = EditModelCheckingProperties.class.getCanonicalName();

    public EditModelCheckingProperties(IWorkbenchWindow window) {
        super(window, ID, Messages.EditModelCheckingProperties_0, Messages.EditModelCheckingProperties_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        EditModelCheckingPropertiesDialog dialog = new EditModelCheckingPropertiesDialog(getShell());
        dialog.open();
    }
}
