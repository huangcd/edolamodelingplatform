package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class ViewBaselineAction extends OpenDialogFromMenuAction {

    public static final String ID = ViewBaselineAction.class.getCanonicalName();

    public ViewBaselineAction(IWorkbenchWindow window) {
        super(window, ID, "�鿴��׼��", "�鿴��׼��", null);
    }

    @Override
    protected Dialog getDialog() {
        // TODO ���ز鿴��׼�ߵĶԻ���
        return null;
    }
}
