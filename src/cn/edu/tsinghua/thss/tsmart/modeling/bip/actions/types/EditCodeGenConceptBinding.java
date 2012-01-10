package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.EditCodeGenConceptBindingDialog;

public class EditCodeGenConceptBinding extends OpenDialogAction {

    public static final String ID = EditCodeGenConceptBinding.class.getCanonicalName();

    public EditCodeGenConceptBinding(IWorkbenchWindow window) {
        super(window, ID, "代码生成概念和实体绑定", "代码生成概念和实体绑定", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        EditCodeGenConceptBindingDialog dialog = new EditCodeGenConceptBindingDialog(shell);
        dialog.setBlockOnOpen(true);
        if (Dialog.OK == dialog.open()) {
            
            

        }

    }
}
