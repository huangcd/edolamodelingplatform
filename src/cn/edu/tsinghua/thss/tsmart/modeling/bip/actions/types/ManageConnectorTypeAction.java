package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ConnectorTypeManageDialog;

public class ManageConnectorTypeAction extends OpenDialogFromMenuAction {

    public static final String ID = ManageConnectorTypeAction.class.getCanonicalName();

    public ManageConnectorTypeAction(IWorkbenchWindow window) {
        super(window, ID, "��������������", "���ӻ�ɾ������������", "icons/connector_16.png");
    }

    @Override
    protected Dialog getDialog() {
        return new ConnectorTypeManageDialog(getShell());
    }
}
