package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;

@SuppressWarnings("rawtypes")
public class BaselineOptionDialog extends AbstractEditDialog {
    private Button buttonFunctionLayer;
    private Button buttonNoConnection;
    private Button buttonOnlySynchronized;
    private Button buttonHasStartCycle;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public BaselineOptionDialog(Shell parentShell) {
        super(parentShell, "\u8BBE\u7F6E\u57FA\u51C6\u7EBF\u68C0\u67E5\u89C4\u5219");
        setTitle("\u8BBE\u7F6E\u57FA\u51C6\u7EBF\u68C0\u67E5\u89C4\u5219");
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        Group groupModeling = new Group(container, SWT.NONE);
        groupModeling.setText("\u6A21\u578B\u8BBE\u8BA1");
        groupModeling.setBounds(15, 10, 189, 324);

        buttonFunctionLayer = new Button(groupModeling, SWT.CHECK);
        buttonFunctionLayer.setText("\u529F\u80FD\u5C42\u6784\u4EF6\u4E0D\u76F8\u8FDE");
        buttonFunctionLayer.setBounds(10, 29, 113, 17);

        buttonNoConnection = new Button(groupModeling, SWT.CHECK);
        buttonNoConnection
                        .setText("\u4E92\u9501\u5C42\u548C\u4E3B\u63A7\u5C42\u6784\u4EF6\u4E0D\u76F8\u8FDE");
        buttonNoConnection.setBounds(10, 52, 169, 17);

        Group groupModelChecking = new Group(container, SWT.NONE);
        groupModelChecking.setText("\u6A21\u578B\u68C0\u6D4B");
        groupModelChecking.setBounds(219, 10, 189, 324);

        buttonOnlySynchronized = new Button(groupModelChecking, SWT.CHECK);
        buttonOnlySynchronized.setText("\u53EA\u8003\u8651\u5F3A\u540C\u6B65\u8FDE\u7ED3\u5B50");
        buttonOnlySynchronized.setBounds(10, 29, 169, 17);

        Group groupCodeGeneration = new Group(container, SWT.NONE);
        groupCodeGeneration.setText("\u4EE3\u7801\u751F\u6210");
        groupCodeGeneration.setBounds(423, 10, 189, 324);

        buttonHasStartCycle = new Button(groupCodeGeneration, SWT.CHECK);
        buttonHasStartCycle.setText("\u6A21\u5757\u4E2D\u6709start_cycle\u7AEF\u53E3");
        buttonHasStartCycle.setBounds(10, 29, 152, 17);

        initValues();
        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(633, 429);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {}

    @Override
    protected Label getErrorLabel() {
        return null;
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }
}
