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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen.EditPeripheralsDialog;

public class EditingCodeGenerationDevicesAction extends OpenDialogAction {

    public static final String ID = EditingCodeGenerationDevicesAction.class.getCanonicalName();

    public EditingCodeGenerationDevicesAction(IWorkbenchWindow window) {
        super(window, ID, "编辑外设", "编辑外设", null);
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
            CompoundTypeModel model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
            EditPeripheralsDialog dialog = new EditPeripheralsDialog(shell, model);
            dialog.setBlockOnOpen(true);
            if (Dialog.OK == dialog.open()) {

            }

        }
        
        
        
    }
}
