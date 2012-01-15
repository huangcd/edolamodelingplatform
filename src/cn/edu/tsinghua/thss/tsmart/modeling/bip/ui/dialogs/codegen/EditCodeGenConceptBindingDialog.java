package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.EntitySelectionFromLibDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditCodeGenConceptBindingDialog extends AbstractEditDialog {
    private Text                hardware;
    private Label               errorLabel;
    private Text                io;
    private Text                software;
    private Text                tick;

    private CodeGenProjectModel cgpm;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditCodeGenConceptBindingDialog(Shell parentShell) {
        super(parentShell, Messages.EditCodeGenConceptBindingDialog_0);
        setTitle(Messages.EditCodeGenConceptBindingDialog_1);
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
        label.setAlignment(SWT.RIGHT);
        label.setBounds(20, 23, 36, 17);
        label.setText(Messages.EditCodeGenConceptBindingDialog_2);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setAlignment(SWT.RIGHT);
        lblNewLabel.setBounds(20, 63, 36, 17);
        lblNewLabel.setText(Messages.EditCodeGenConceptBindingDialog_3);

        hardware = new Text(container, SWT.BORDER);
        hardware.setEnabled(false);
        hardware.setBounds(69, 17, 183, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 175, 354, 17);

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setAlignment(SWT.RIGHT);
        lblNewLabel_2.setBounds(20, 143, 36, 17);
        lblNewLabel_2.setText("Tick"); //$NON-NLS-1$

        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setAlignment(SWT.RIGHT);
        lblNewLabel_3.setBounds(20, 103, 36, 17);
        lblNewLabel_3.setText("I/O"); //$NON-NLS-1$

        io = new Text(container, SWT.BORDER);
        io.setEnabled(false);
        io.setBounds(69, 97, 183, 23);

        software = new Text(container, SWT.BORDER);
        software.setEnabled(false);
        software.setText(""); //$NON-NLS-1$
        software.setBounds(69, 57, 183, 23);

        tick = new Text(container, SWT.BORDER);
        tick.setEnabled(false);
        tick.setText(""); //$NON-NLS-1$
        tick.setBounds(69, 137, 183, 23);

        Button btnHardware = new Button(container, SWT.NONE);
        btnHardware.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                if (Window.OK == dialog.open()) {
                    hardware.setText(dialog.getSelectEntity());
                }

            }
        });
        btnHardware.setBounds(276, 18, 72, 22);
        btnHardware.setText(Messages.EditCodeGenConceptBindingDialog_8);

        Button btn = new Button(container, SWT.NONE);
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                if (Window.OK == dialog.open()) {
                    software.setText(dialog.getSelectEntity());
                }

            }
        });
        btn.setText(Messages.EditCodeGenConceptBindingDialog_9);
        btn.setBounds(276, 58, 72, 22);

        Button button_1 = new Button(container, SWT.NONE);
        button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                if (Window.OK == dialog.open()) {
                    io.setText(dialog.getSelectEntity());
                }

            }
        });
        button_1.setText(Messages.EditCodeGenConceptBindingDialog_10);
        button_1.setBounds(276, 97, 72, 22);

        Button button_2 = new Button(container, SWT.NONE);
        button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                if (Window.OK == dialog.open()) {
                    tick.setText(dialog.getSelectEntity());
                }

            }
        });
        button_2.setText(Messages.EditCodeGenConceptBindingDialog_11);
        button_2.setBounds(276, 137, 72, 22);

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
        button.setText(Messages.EditCodeGenConceptBindingDialog_12);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditCodeGenConceptBindingDialog_13);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(380, 280);
    }

    @Override
    protected void initValues() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.EditCodeGenConceptBindingDialog_14);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        cgpm = (CodeGenProjectModel) topModel;

        hardware.setText(cgpm.getHardwareEntity());
        software.setText(cgpm.getSoftwareEntity());
        io.setText(cgpm.getIoEntity());
        tick.setText(cgpm.getTickEntity());
    }


    @Override
    protected void updateValues() {

        cgpm.setHardwareEntity(hardware.getText());
        cgpm.setSoftwareEntity(software.getText());
        cgpm.setIoEntity(io.getText());
        cgpm.setTickEntity(tick.getText());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (cgpm == null) {
            handleError(Messages.EditCodeGenConceptBindingDialog_15);
            return false;
        }

        if (hardware.getText().equals("") || software.getText().equals("") //$NON-NLS-1$ //$NON-NLS-2$
                        || io.getText().equals("") || tick.getText().equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            handleError(Messages.EditCodeGenConceptBindingDialog_20);
            return false;
        }

        ArrayList<String> names = new ArrayList<String>();

        names.add(hardware.getText());
        names.add(software.getText());
        names.add(io.getText());
        names.add(tick.getText());

        ArrayList<String> ns = new ArrayList<String>();

        for (String s : names) {
            if (ns.contains(s)) {
                handleError(Messages.EditCodeGenConceptBindingDialog_21);
                return false;
            }
            ns.add(s);
        }



        return true;
    }

    public Device getData() {
        return null;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }

}
