package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;
import org.eclipse.swt.widgets.Spinner;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class CodeGenerateDialog extends AbstractEditDialog {
    private Combo   platform;
    private Spinner ticktime;
    private Combo   opti;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public CodeGenerateDialog(Shell parentShell) {
        super(parentShell, Messages.CodeGenerateDialog_0);
        setTitle(Messages.CodeGenerateDialog_1);
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

        Group group = new Group(container, SWT.NONE);
        group.setBounds(10, 0, 228, 170);
        group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        group.setLayout(null);

        Label lblNewLabel = new Label(group, SWT.NONE);
        lblNewLabel.setBounds(10, 17, 48, 22);
        lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        lblNewLabel.setText(Messages.CodeGenerateDialog_2);

        platform = new Combo(group, SWT.READ_ONLY);
        platform.setBounds(98, 14, 100, 25);
        platform.setItems(new String[] {"PLC"}); //$NON-NLS-1$
        platform.setText("PLC"); //$NON-NLS-1$

        Label label = new Label(group, SWT.NONE);
        label.setBounds(10, 54, 48, 17);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        label.setText(Messages.CodeGenerateDialog_5);

        Label lblTick = new Label(group, SWT.NONE);
        lblTick.setBounds(10, 89, 82, 22);
        lblTick.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        lblTick.setText(Messages.CodeGenerateDialog_6);

        Button editMapping = new Button(group, SWT.NONE);
        editMapping.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = Display.getCurrent().getActiveShell();
                EditRelationDialog dialog = new EditRelationDialog(shell);
                dialog.setBlockOnOpen(true);
                if (Dialog.OK == dialog.open()) {

                }
            }
        });
        editMapping.setBounds(10, 130, 80, 27);
        editMapping.setText(Messages.CodeGenerateDialog_7);

        Button editDevices = new Button(group, SWT.NONE);
        editDevices.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = Display.getCurrent().getActiveShell();
                EditPeripheralsDialog dialog = new EditPeripheralsDialog(shell);
                dialog.setBlockOnOpen(true);
                if (Dialog.OK == dialog.open()) {

                }
            }
        });
        editDevices.setBounds(118, 130, 80, 27);
        editDevices.setText(Messages.CodeGenerateDialog_8);

        ticktime = new Spinner(group, SWT.BORDER);
        ticktime.setMaximum(1000);
        ticktime.setSelection(100);
        ticktime.setBounds(98, 86, 100, 23);

        opti = new Combo(group, SWT.READ_ONLY);
        opti.setItems(new String[] {"0", "1"}); //$NON-NLS-1$ //$NON-NLS-2$
        opti.setBounds(98, 51, 100, 25);
        opti.select(0);


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
        Button button = createButton(parent, 
        		IDialogConstants.OK_ID, 
        		IDialogConstants.OK_LABEL,
        		true);
        button.setText(Messages.CodeGenerateDialog_11);
        Button button_1 = createButton(parent, 
        		IDialogConstants.CANCEL_ID,
        		IDialogConstants.CANCEL_LABEL, 
        		false);
        button_1.setText(Messages.CodeGenerateDialog_12);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(262, 262);
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void updateValues() {
        CodeGenManager cgm = new CodeGenManager();
        cgm.setOptimization(Integer.parseInt(opti.getText()));
        cgm.setPlatForm(platform.getText());
        cgm.setTickTime(ticktime.getSelection());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {

        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
