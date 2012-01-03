package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class ViewBaselineAction extends OpenDialogFromMenuAction {

    public static final String ID = ViewBaselineAction.class.getCanonicalName();

    public ViewBaselineAction(IWorkbenchWindow window) {
        super(window, ID, "查看基准线", "查看基准线", null);
    }

    @Override
    protected Dialog getDialog() {
        // TODO 返回查看基准线的对话框
        return null;
    }
}
