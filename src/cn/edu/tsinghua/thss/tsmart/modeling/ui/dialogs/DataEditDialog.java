package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs;

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
 *         TODO：做简单类型的检查（如果DataModel是bool类型，则将textValue替换成一个只允许true和false的combo；如果DataModel是int类型
 *         ，则在最后判断输入是否是一个数字）
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
        container.setLayout(null);

        Label labelType = new Label(container, SWT.NONE);
        labelType.setBounds(15, 5, 30, 18);
        labelType.setText("type");

        Label labelName = new Label(container, SWT.NONE);
        labelName.setBounds(92, 5, 37, 18);
        labelName.setText("name");

        Label labelValue = new Label(container, SWT.NONE);
        labelValue.setBounds(216, 5, 35, 18);
        labelValue.setText("value");

        labelTypeName = new Label(container, SWT.NONE);
        labelTypeName.setBounds(15, 32, 72, 18);

        textName = new Text(container, SWT.BORDER);
        textName.setBounds(92, 28, 119, 27);

        textValue = new Text(container, SWT.BORDER);
        textValue.setBounds(216, 28, 119, 27);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(17, 63, 297, 18);

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
        return new Point(340, 168);
    }

    @Override
    protected void initValues() {
        labelTypeName.setText(((DataTypeModel) instance.getType()).getTypeName());
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
            labelError.setText("组件中存在相同名字的变量");
            // TODO 弹出错误提示
            return false;
        } else if (labelTypeName.getText().toLowerCase().trim().equals("bool")
                        && !textValue.getText().toLowerCase().trim().equals("true")
                        && !textValue.getText().toLowerCase().trim().equals("false")) {
            labelError.setText("bool类型的值只能是true或者false");
            return false;
        } else if (labelTypeName.getText().toLowerCase().trim().equals("int")) {
            try {
                Integer.parseInt(textValue.getText().toLowerCase().trim());
            } catch (NumberFormatException e) {
                labelError.setText("int类型的值必须是整数");
                return false;
            }
        }
        return true;
    }
}
