package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ModelingSettingDialog;

public class SettingModelingAction extends OpenDialogAction {
    public static final String ID = SettingModelingAction.class.getCanonicalName();

    public SettingModelingAction(IWorkbenchWindow window) {
        super(window, ID, Messages.SettingModelingAction_0, Messages.SettingModelingAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return new ModelingSettingDialog(getShell());
    }
}
