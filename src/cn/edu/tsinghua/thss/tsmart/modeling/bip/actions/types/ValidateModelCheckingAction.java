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
        super(window, ID, "��֤ģ���Ƿ�����Ҫ��", "��֤ģ���Ƿ�����Ҫ��", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO ��֤ģ���Ƿ�����Ҫ��

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            CompoundTypeModel model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
            // �����ͨ����֤�����ܵ���ģ��

            MessageUtil.ShowErrorDialog("ģ�ͼ��ģ����֤", "Info");
        }
    }
}
