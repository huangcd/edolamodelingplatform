package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.DataTypeManageDialog;

public class ManageDataTypeAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = ManageDataTypeAction.class.getCanonicalName();

    public ManageDataTypeAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("管理数据类型");
        setToolTipText("增加或删除数据类型");
        // setImageDescriptor(Activator.getImageDescriptor("icons/atomic_16.png"));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
    }
    @Override
    public void run() {
        DataTypeManageDialog dialog = new DataTypeManageDialog(window.getShell());
        dialog.setBlockOnOpen(true);
        dialog.open();
    }
}
