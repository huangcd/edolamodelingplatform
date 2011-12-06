package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;


public class EditConnectorPortDialog extends Dialog {
    private Text               nameText;
    private ConnectorPortModel owner;

    public EditConnectorPortDialog(Shell parentShell, ConnectorPortModel owner) {
        super(parentShell);
        this.owner = owner;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Edit Connector Port Properties");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 2;

        Label lblGuard = new Label(container, SWT.NONE);
        lblGuard.setAlignment(SWT.RIGHT);
        lblGuard.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblGuard.setText("name:");

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        container.setTabList(new Control[] {nameText});

        this.nameText.setText(owner.getName());
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
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(283, 142);
    }

    @Override
    protected void okPressed() {
        owner.setName(nameText.getText());
        super.okPressed();
    }
}
