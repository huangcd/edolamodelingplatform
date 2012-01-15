package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;

public class CloseAllAction extends Action implements ISelectionListener, IWorkbenchAction {
    public static final String     ID = CloseAllAction.class.getCanonicalName();
    private final IWorkbenchWindow window;

    public CloseAllAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText(Messages.CloseAllAction_0);
        setToolTipText(Messages.CloseAllAction_1);
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void run() {
        BIPEditor.closeAllEditor();
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}
}
