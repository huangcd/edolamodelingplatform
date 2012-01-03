package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class CodeGenerationAction extends OpenDialogFromMenuAction {

    public static final String ID = CodeGenerationAction.class.getCanonicalName();

    public CodeGenerationAction(IWorkbenchWindow window) {
        super(window, ID, "代码生成", "代码生成", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 代码生成
    }
}
