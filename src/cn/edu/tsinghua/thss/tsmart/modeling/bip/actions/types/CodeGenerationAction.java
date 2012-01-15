package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.CodeGenerateDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.CodeGeneratorRunningDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;

public class CodeGenerationAction extends OpenDialogAction {

    public static final String ID = CodeGenerationAction.class.getCanonicalName();

    public CodeGenerationAction(IWorkbenchWindow window) {
        super(window, ID, Messages.CodeGenerationAction_0, Messages.CodeGenerationAction_1, null);
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
        	final CodeGeneratorRunningDialog cgrd = new CodeGeneratorRunningDialog(shell);
        	cgrd.notifyStarted();
        	new Thread() {
        		public void run() {
                	CodeGenManager cgm = new CodeGenManager();
                	// invoke the code generator
                	try {
                		cgm.runCodeGen();
                	} finally {
                    	// notify the cgrd to close
                    	cgrd.notifyFinished();
                	}
        		}
        	}.start();
    		// if the task ends very quickly, the cgrd may be already notified FINISHED.
    		// In that case, don't open the window
        	if (!cgrd.isFinished()) {
            	// block the UI until code generation is finished.
            	cgrd.open();
        	}
        }
    }
}
