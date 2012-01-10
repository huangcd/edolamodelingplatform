package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.PortTypeManageDialog;

public class ManagePortTypeAction extends OpenDialogAction {
    public static final String ID = ManagePortTypeAction.class.getCanonicalName();

    public ManagePortTypeAction(IWorkbenchWindow window) {
        super(window, ID, "����˿�����", "���ӻ�ɾ���˿�����", "icons/port_16.png");
    }

    @Override
    protected Dialog getDialog() {
        return new PortTypeManageDialog(getShell());
    }
}
