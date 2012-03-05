package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class OpenViewAction extends Action implements ISelectionListener, IWorkbenchAction {
    private final IWorkbenchWindow window;
    public static final String     ID      = OpenViewAction.class.getCanonicalName();
    public final String            outline = "org.eclipse.ui.views.ContentOutline";  //$NON-NLS-1$

    public OpenViewAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText(Messages.OpenViewAction_1);
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

    }
}
