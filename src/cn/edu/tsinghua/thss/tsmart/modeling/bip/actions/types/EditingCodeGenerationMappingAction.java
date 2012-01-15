package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.EditRelationDialog;

public class EditingCodeGenerationMappingAction extends OpenDialogAction {

    public static final String ID = EditingCodeGenerationMappingAction.class.getCanonicalName();

    public EditingCodeGenerationMappingAction(IWorkbenchWindow window) {
        super(window, ID, Messages.EditingCodeGenerationMappingAction_0, Messages.EditingCodeGenerationMappingAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            EditRelationDialog dialog = new EditRelationDialog(shell);
            dialog.setBlockOnOpen(true);
            if (Dialog.OK == dialog.open()) {

            }

        }



    }
}
