package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;

/**
 * 
 * @author huangcd
 *         TODO：做简单类型的检查（如果DataModel是bool类型，则将textValue替换成一个只允许true和false的combo；如果DataModel是int类型
 *         ，则在最后判断输入是否是一个数字）
 */
@SuppressWarnings("rawtypes")
public class DataEditDialog extends AbstractEditDialog {
    private Text           textName;
    private Text           textValue;
    private Combo          comboType;
    private DataModel      instance;
    private IDataContainer container;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public DataEditDialog(Shell parentShell, DataModel data) {
        super(parentShell, "Edit Data Model");
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
        container.setLayout(new GridLayout(4, false));
        new Label(container, SWT.NONE);

        Label labelType = new Label(container, SWT.NONE);
        labelType.setText("type");

        Label labelName = new Label(container, SWT.NONE);
        labelName.setText("name");

        Label labelValue = new Label(container, SWT.NONE);
        labelValue.setText("value");
        new Label(container, SWT.NONE);

        if (!properties.isMultipleDataTypeAvailble()) {
            comboType = new Combo(container, SWT.READ_ONLY | SWT.DROP_DOWN);
        } else {
            comboType = new Combo(container, SWT.NONE);
        }
        GridData gd_comboType = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_comboType.widthHint = 100;
        comboType.setLayoutData(gd_comboType);

        textName = new Text(container, SWT.BORDER);
        textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        textValue = new Text(container, SWT.BORDER);
        textValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

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
        return new Point(340, 153);
    }

    @Override
    protected void initValues() {
        if (!properties.isMultipleDataTypeAvailble()) {
            comboType.setItems(new String[] {"bool"});
            comboType.select(0);
            comboType.setEnabled(true);
        } else {
            comboType.setItems(DataTypeModel.getRegisterTypeNamesAsArray());
            comboType.select(0);
        }
        if (instance.hasName()) {
            textName.setText(instance.getName());
        }
        if (instance.getValue() != null && !instance.getValue().isEmpty()) {
            textValue.setText(instance.getValue());
        }
    }

    @Override
    protected void updateValues() {
        String newType = comboType.getText().toLowerCase().trim();
        String newName = textName.getText().trim();
        String newValue = textValue.getText().trim();
        if (!newType.equals(((DataTypeModel) instance.getType()).getTypeName())) {
            ((DataTypeModel) instance.getType()).setTypeName(newType);
        }
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
            // TODO 弹出错误提示
            return false;
        } else if (comboType.getText().toLowerCase().trim().equals("bool")
                        && !textValue.getText().toLowerCase().trim().equals("true")
                        && !textValue.getText().toLowerCase().trim().equals("false")) {
            // TODO 弹出错误提示
            return false;
        } else if (comboType.getText().toLowerCase().trim().equals("int")) {
            try {
                Integer.parseInt(textValue.getText().toLowerCase().trim());
            } catch (NumberFormatException e) {
                // TODO 弹出错误提示
                return false;
            }
        }
        return true;
    }
}
