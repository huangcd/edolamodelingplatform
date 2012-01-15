package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class DataEditDialog extends AbstractEditDialog {
    private Text           textName;
    private Text           textValue;
    private DataModel      instance;
    private Label          labelTypeName;
    private Label          labelError;
    private IDataContainer container;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public DataEditDialog(Shell parentShell, DataModel data) {
        super(parentShell, Messages.DataEditDialog_0);
        this.instance = data;
        this.container = (IDataContainer) instance.getParent();
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

        Label labelType = new Label(container, SWT.NONE);
        labelType.setBounds(15, 5, 40, 18);
        labelType.setText(Messages.DataEditDialog_1);

        Label labelName = new Label(container, SWT.NONE);
        labelName.setBounds(80, 5, 40, 18);
        labelName.setText(Messages.DataEditDialog_2);

        Label labelValue = new Label(container, SWT.NONE);
        labelValue.setBounds(216, 5, 40, 18);
        labelValue.setText(Messages.DataEditDialog_3);

        labelTypeName = new Label(container, SWT.NONE);
        labelTypeName.setBounds(15, 32, 40, 18);

        textName = new Text(container, SWT.BORDER);
        textName.setBounds(80, 28, 120, 27);

        textValue = new Text(container, SWT.BORDER);
        textValue.setBounds(215, 28, 120, 27);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(15, 63, 320, 18);

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
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(350, 168);
    }

    @Override
    protected void initValues() {
        labelTypeName.setText(((DataTypeModel) instance.getType()).getName());
        if (instance.hasName()) {
            textName.setText(instance.getName());
        }
        if (instance.getValue() != null && !instance.getValue().isEmpty()) {
            textValue.setText(instance.getValue());
        }
    }

    @Override
    protected void updateValues() {
        String newName = textName.getText().trim();
        String newValue = textValue.getText().trim();
        if (!newName.equals(instance.getName())) {
            instance.setName(newName);
        }
        if (!newValue.equals(instance.getValue())) {
            instance.setValue(newValue);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (container.isNewNameAlreadyExistsInParent(instance, textName.getText().trim())) {
            labelError.setText(Messages.DataEditDialog_4);
            return false;
        } else if (labelTypeName.getText().toLowerCase().trim().equals("bool") //$NON-NLS-1$
                        && !textValue.getText().toLowerCase().trim().equals("true") //$NON-NLS-1$
                        && !textValue.getText().toLowerCase().trim().equals("false")) { //$NON-NLS-1$
            labelError.setText(Messages.DataEditDialog_8);
            return false;
        } else if (labelTypeName.getText().toLowerCase().trim().equals("int")) { //$NON-NLS-1$
            try {
                Integer.parseInt(textValue.getText().toLowerCase().trim());
            } catch (NumberFormatException e) {
                labelError.setText(Messages.DataEditDialog_10);
                return false;
            }
        }
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
