package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class ValidateAction extends OpenDialogFromMenuAction {

    public static final String ID = ValidateAction.class.getCanonicalName();

    public ValidateAction(IWorkbenchWindow window) {
        super(window, ID, "��֤ģ���Ƿ�����Ҫ��", "��֤ģ���Ƿ�����Ҫ��", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO ��֤ģ���Ƿ�����Ҫ��
    }
}
