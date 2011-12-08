package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.util.MessageBoxUtil;


public class EditAtomicTypeDialog extends AbstractEditDialog {
    private Text              nameText;
    private Text              initActionText;
    private AtomicTypeModel   owner;
    private CompoundTypeModel _parent;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     * @param owner
     * @param parent
     */
    public EditAtomicTypeDialog(Shell parentShell, AtomicTypeModel owner, CompoundTypeModel parent) {
        super(parentShell, "Edit Atomic properties");
        this.owner = owner;
        this._parent = parent;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("name:");

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("init action:");

        initActionText = new Text(container, SWT.BORDER | SWT.MULTI);
        GridData gd_initActionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_initActionText.heightHint = 81;
        initActionText.setLayoutData(gd_initActionText);

        initValues();
        return container;
    }

    @Override
    protected void initValues() {
        nameText.setText(owner.getName());
        initActionText.setText(owner.getInitAction());
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(428, 214);
    }

    @Override
    protected void okPressed() {
        if (_parent != null && !validateUserInput()) {
            return;
        }
        owner.setName(nameText.getText());
        owner.setInitAction(initActionText.getText());
        super.okPressed();
    }

    @Override
    protected boolean validateUserInput() {
        if (nameExistsInParent(nameText.getText())) return false;
        return true;
    }

    private boolean nameExistsInParent(String name) {
        for (BaseModel model : _parent.getChildren()) {
            if (model instanceof AtomicTypeModel) {
                AtomicTypeModel child = (AtomicTypeModel) model;
                if (child.equals(this.owner)) {
                    continue;
                }
                if (child.getName().equals(name)) {
                    MessageBoxUtil.ShowErrorMessage(this.getShell(), "Name conflict error",
                                    "Atomic name exists in this compound");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void updateValues() {
        // TODO Auto-generated method stub
        
    }
}
