package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.util.MessageBoxUtil;

public class EditDataDialog extends Dialog {
    private Text          nameText;
    private Text          valueText;
    private Combo         typeCombo;
    private DataAreaModel _parent;
    private DataModel     owner;

    public EditDataDialog(Shell parentShell, DataAreaModel parent, DataModel owner) {
        super(parentShell);
        this._parent = parent;
        this.owner = owner;
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Edit data properties");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setToolTipText("");
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 4;

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("type");

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setAlignment(SWT.CENTER);
        lblNewLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("name");
        new Label(container, SWT.NONE);

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setAlignment(SWT.CENTER);
        lblNewLabel_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("value");

        typeCombo = new Combo(container, SWT.NONE);
        typeCombo.setItems(new String[] {"int", "boolean"});
        typeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        typeCombo.select(0);

        nameText = new Text(container, SWT.BORDER | SWT.CENTER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblNewLabel_6 = new Label(container, SWT.NONE);
        lblNewLabel_6.setAlignment(SWT.CENTER);
        lblNewLabel_6.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_6.setText("=");

        valueText = new Text(container, SWT.BORDER | SWT.CENTER);
        valueText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        initValues();// 设置初始化值
        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(315, 171);
    }

    private void initValues() {
        this.typeCombo.setText(owner.getType());
        this.nameText.setText(owner.getName());
        this.valueText.setText(owner.getValue());
    }

    private boolean validateUserInput() {
        return !nameExistsInParent(nameText.getText());
    }

    public boolean nameExistsInParent(String name) {
        for (DataModel child : _parent.getChildren()) {
            if (child.equals(owner)) {
                continue;
            }
            if (child.getName().equals(name)) {
                MessageBoxUtil.ShowErrorMessage("Name conflict error",
                                "Data name exists in this atomic");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void okPressed() {
        if (_parent != null) {
            if (!validateUserInput()) {
                return;
            }
        }
        owner.setType(typeCombo.getText().trim());
        owner.setName(nameText.getText().trim());
        owner.setValue(valueText.getText().trim());
        super.okPressed();
    }
}
