package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class ModelCheckingAction extends OpenDialogFromMenuAction {

    public static final String ID = ModelCheckingAction.class.getCanonicalName();

    public ModelCheckingAction(IWorkbenchWindow window) {
        super(window, ID, "模型检测", "模型检测", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 模型检测
    }
}
