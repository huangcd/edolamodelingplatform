package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.VariableSelectionDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Binding;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditMappingDialog extends AbstractEditDialog {
    private Binding             instance;
    private ArrayList<String>   variables = new ArrayList<String>();

    private Text                var;
    private Combo               type;
    private Label               errorLabel;
    private Spinner             addr;
    private Spinner             length;
    private Spinner             bit;



    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditMappingDialog(Shell parentShell, Binding data, ArrayList<String> vars) {
        super(parentShell, Messages.EditMappingDialog_0);
        setTitle(Messages.EditMappingDialog_1);
        this.instance = data;
        this.variables = vars;
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

        Label label = new Label(container, SWT.NONE);
        label.setBounds(39, 22, 36, 17);
        label.setText(Messages.EditMappingDialog_2);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(22, 58, 53, 17);
        lblNewLabel.setText(Messages.EditMappingDialog_3);

        Label label_1 = new Label(container, SWT.NONE);
        label_1.setBounds(51, 97, 24, 17);
        label_1.setText(Messages.EditMappingDialog_4);

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setBounds(51, 134, 24, 17);
        lblNewLabel_1.setText(Messages.EditMappingDialog_5);

        Label label_2 = new Label(container, SWT.NONE);
        label_2.setBounds(63, 173, 12, 17);
        label_2.setText(Messages.EditMappingDialog_6);

        var = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        var.setBounds(82, 19, 183, 23);

        Button button = new Button(container, SWT.NONE);
        button.setBounds(271, 17, 60, 27);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                VariableSelectionDialog dialog = new VariableSelectionDialog(Display.getCurrent().getActiveShell(), true);
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    var.setText(dialog.getData());
                }
            }
        });
        button.setText(Messages.EditMappingDialog_7);

        type = new Combo(container, SWT.READ_ONLY);
        type.setBounds(81, 55, 184, 25);
        type.setItems(new String[] {"input", "output"}); //$NON-NLS-1$ //$NON-NLS-2$
        type.select(0);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 226, 327, 17);

        addr = new Spinner(container, SWT.BORDER);
        addr.setBounds(81, 94, 153, 23);

        length = new Spinner(container, SWT.BORDER);
        length.setMaximum(8);
        length.setBounds(81, 131, 153, 23);

        bit = new Spinner(container, SWT.BORDER);
        bit.setMaximum(7);
        bit.setBounds(81, 167, 153, 23);


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
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        Button button =
                        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                                        true);
        button.setText(Messages.EditMappingDialog_10);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditMappingDialog_11);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(367, 338);
    }

    @Override
    protected void initValues() {
        if (instance != null) {
            var.setText(instance.var);
            type.setText(instance.type);
            addr.setSelection(instance.addr);
            length.setSelection(instance.length);
            bit.setSelection(instance.bit);
        }
    }

    @Override
    protected void updateValues() {
        if (instance == null) instance = new Binding();

        instance.var = var.getText();
        instance.type = type.getText();
        instance.addr = addr.getSelection();
        instance.length = length.getSelection();
        instance.bit = bit.getSelection();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (var.getText().equals("")) { //$NON-NLS-1$
            handleError(Messages.EditMappingDialog_13);
            return false;
        }

        if (length.getSelection() != 0 && bit.getSelection() != 0) {
            handleError(Messages.EditMappingDialog_14);
            return false;
        }

        if (variables != null && this.variables.contains(var.getText())) {
            handleError(Messages.EditMappingDialog_15);
            return false;
        }

        return true;
    }


    public Binding getData() {
        return instance;
    }

    @Override
    protected Label getErrorLabel() {
        return errorLabel;
    }
}
