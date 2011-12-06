package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import java.util.ArrayList;
import java.util.List;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BipModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.util.MessageBoxUtil;


public class EditCompoundTypeDialog extends EditDialog {
    private Text              nameText;
    private CompoundTypeModel owner;
    private BaseModel         _parent;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     * @param owner
     * @param parent
     */
    public EditCompoundTypeDialog(Shell parentShell, CompoundTypeModel owner, BaseModel parent) {
        super(parentShell);
        this.owner = owner;
        this._parent = parent;
        setTitle("Edit Compound properties");
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

        return container;
    }

    @Override
    protected void initValue() {
        nameText.setText(owner.getName());
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(428, 132);
    }

    @Override
    protected void okPressed() {
        if (_parent != null && !validateUserInput()) {
            return;
        }
        owner.setName(nameText.getText());
        super.okPressed();
    }

    @Override
    protected boolean validateUserInput() {
        if (nameExistsInParent(nameText.getText())) return false;
        return true;
    }

    private boolean nameExistsInParent(String name) {
        List<BaseModel> models = new ArrayList<BaseModel>();
        if (_parent instanceof CompoundTypeModel) {
            models = ((CompoundTypeModel) _parent).getChildren();
        } else if (_parent instanceof BipModel) {
            models = ((BipModel) _parent).getChildren();
        }
        for (BaseModel model : models) {
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
}
