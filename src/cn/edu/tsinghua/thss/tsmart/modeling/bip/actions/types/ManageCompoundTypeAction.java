package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.CompoundTypeManageDialog;

public class ManageCompoundTypeAction extends OpenDialogFromMenuAction {
    public static final String ID = ManageCompoundTypeAction.class.getCanonicalName();

    public ManageCompoundTypeAction(IWorkbenchWindow window) {
        super(window, ID, "�������������", "�鿴��ɾ�������������", "icons/compound_16.png");
    }

    @Override
    protected Dialog getDialog() {
        return new CompoundTypeManageDialog(getShell());
    }
}
