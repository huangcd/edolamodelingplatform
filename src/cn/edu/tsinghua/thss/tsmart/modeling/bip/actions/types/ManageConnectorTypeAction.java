package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ConnectorTypeManageDialog;

public class ManageConnectorTypeAction extends OpenDialogAction {

    public static final String ID = ManageConnectorTypeAction.class.getCanonicalName();

    public ManageConnectorTypeAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageConnectorTypeAction_0, Messages.ManageConnectorTypeAction_1, "icons/connector_16.png"); //$NON-NLS-3$
    }

    @Override
    protected Dialog getDialog() {
        return new ConnectorTypeManageDialog(getShell());
    }
}
