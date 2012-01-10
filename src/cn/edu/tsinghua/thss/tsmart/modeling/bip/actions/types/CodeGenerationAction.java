package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.CodeGenerateDialog;

public class CodeGenerationAction extends OpenDialogAction {

    public static final String ID = CodeGenerationAction.class.getCanonicalName();

    public CodeGenerationAction(IWorkbenchWindow window) {
        super(window, ID, "代码生成", "代码生成", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        CodeGenerateDialog dialog = new CodeGenerateDialog(shell);
        dialog.setBlockOnOpen(true);
        if (Dialog.OK == dialog.open()) {

        }

    }
}
