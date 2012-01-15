package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class MultilineTextEditDialog extends AbstractEditDialog {
    private StyledText textAction;
    private String     action;

    public MultilineTextEditDialog(Shell shell, String action) {
        super(shell, Messages.MultilineTextEditDialog_0);
        setShellStyle(SWT.BORDER | SWT.MAX | SWT.RESIZE);
        this.action = action;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));
        textAction = new StyledText(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        initValues();
        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(668, 616);
    }

    @Override
    protected void initValues() {
        textAction.setText(action);
    }

    @Override
    protected void updateValues() {
        action = textAction.getText();
    }

    public String getAction() {
        return action;
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
