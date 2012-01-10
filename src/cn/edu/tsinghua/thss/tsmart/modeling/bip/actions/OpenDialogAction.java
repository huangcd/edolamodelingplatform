package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public abstract class OpenDialogAction extends Action
                implements
                    ISelectionListener,
                    IWorkbenchAction {

    private final IWorkbenchWindow window;

    public OpenDialogAction(IWorkbenchWindow window, String id, String text, String tooltip,
                    String image) {
        this.window = window;
        if (id != null) {
            setId(id);
        }
        setText(text);
        if (tooltip != null) {
            setToolTipText(tooltip);
        } else {
            setToolTipText(text);
        }
        if (image != null) {
            setImageDescriptor(Activator.getImageDescriptor(image));
        }
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    protected Shell getShell() {
        return window.getShell();
    }

    protected abstract Dialog getDialog();

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    public void run() {
        Dialog dialog = getDialog();
        if (dialog == null) return;
        dialog.setBlockOnOpen(true);
        int returnCode = dialog.open();
        handleFinish(returnCode);
    }

    /**
     * �����������ʵ�ֶԻ���ر�֮��Ľ�һ������
     * 
     * @param returnCode �Ի��򷵻���
     */
    protected void handleFinish(int returnCode) {
        // do nothing
    }
}
