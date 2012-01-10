package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

public class ValidateModelCheckingAction extends OpenDialogAction {

    public static final String ID = ValidateModelCheckingAction.class.getCanonicalName();

    public ValidateModelCheckingAction(IWorkbenchWindow window) {
        super(window, ID, "验证模型是否满足要求", "验证模型是否满足要求", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 验证模型是否满足要求

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            CompoundTypeModel model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
            // 如果不通过验证，不能导出模型

            MessageUtil.ShowErrorDialog("模型检测模型验证", "Info");
        }
    }
}
