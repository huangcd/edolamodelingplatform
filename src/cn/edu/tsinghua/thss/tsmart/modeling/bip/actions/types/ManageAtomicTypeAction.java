package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AtomicTypeManageDialog;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class ManageAtomicTypeAction extends OpenDialogAction {
    public static final String ID = ManageAtomicTypeAction.class.getCanonicalName();

    public ManageAtomicTypeAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageAtomicTypeAction_0, Messages.ManageAtomicTypeAction_1, "icons/atomic_16.png"); //$NON-NLS-3$ //$NON-NLS-1$
    }

    @Override
    protected Dialog getDialog() {
        GlobalProperties.getInstance().getTopModel().reloadComponent();
        return new AtomicTypeManageDialog(getShell());
    }
}
