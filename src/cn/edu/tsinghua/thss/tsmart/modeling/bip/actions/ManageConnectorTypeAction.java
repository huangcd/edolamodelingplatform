package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ConnectorTypeManageDialog;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class ManageConnectorTypeAction extends Action
                implements
                    ISelectionListener,
                    IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = ManageConnectorTypeAction.class.getCanonicalName();

    public ManageConnectorTypeAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("管理连接子类型");
        setToolTipText("增加或删除连接子类型");
        setImageDescriptor(Activator.getImageDescriptor("icons/connector_16.png"));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    @Override
    public void run() {
        ConnectorTypeManageDialog dialog = new ConnectorTypeManageDialog(window.getShell());
        dialog.setBlockOnOpen(true);
        dialog.open();
    }
}
