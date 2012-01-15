package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.VariableSelectionDialog;

@SuppressWarnings("rawtypes")
public class VariableSelectionAction extends SelectionAction {
    public final static String id = VariableSelectionAction.class.getCanonicalName();

    public VariableSelectionAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText(Messages.VariableSelectionAction_0);
        setToolTipText(Messages.VariableSelectionAction_1);
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        IWorkbenchPart part = getWorkbenchPart();
        if (!(part instanceof BIPEditor)) {
            return;
        }
        VariableSelectionDialog dialog = new VariableSelectionDialog(shell, false);
        dialog.setBlockOnOpen(true);
        if (Dialog.OK == dialog.open()) {
            MessageBox box =
                            new MessageBox(Display.getCurrent().getActiveShell(),
                                            SWT.ICON_INFORMATION | SWT.OK);
            box.setMessage(dialog.getData());
            box.setText(Messages.VariableSelectionAction_2);
            box.open();
        }
    }
}
