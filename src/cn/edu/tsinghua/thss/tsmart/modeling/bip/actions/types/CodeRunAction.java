package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;

public class CodeRunAction extends OpenDialogAction {

    public static final String ID = CodeRunAction.class.getCanonicalName();

    public CodeRunAction(IWorkbenchWindow window) {
        super(window, ID, Messages.CodeRunAction_0, Messages.CodeRunAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
       	CodeGenManager cgm = new CodeGenManager();
       	cgm.runCodeSimulation();
    }
}
