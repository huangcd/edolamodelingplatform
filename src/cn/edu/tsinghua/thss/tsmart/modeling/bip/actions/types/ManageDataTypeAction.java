package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.DataTypeManageDialog;

public class ManageDataTypeAction extends OpenDialogAction {

    public static final String ID = ManageDataTypeAction.class.getCanonicalName();

    public ManageDataTypeAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageDataTypeAction_0, Messages.ManageDataTypeAction_1, "icons/new_data_16.png"); //$NON-NLS-3$
    }

    @Override
    protected Dialog getDialog() {
        return new DataTypeManageDialog(getShell());
    }
}
