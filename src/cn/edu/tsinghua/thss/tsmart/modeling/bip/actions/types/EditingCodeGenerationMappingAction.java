package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

public class EditingCodeGenerationMappingAction extends OpenDialogFromMenuAction {

    public static final String ID = EditingCodeGenerationMappingAction.class.getCanonicalName();

    public EditingCodeGenerationMappingAction(IWorkbenchWindow window) {
        super(window, ID, "�༭ӳ���ϵ", "�༭ӳ���ϵ", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO �༭ӳ���ϵ
    }
}
