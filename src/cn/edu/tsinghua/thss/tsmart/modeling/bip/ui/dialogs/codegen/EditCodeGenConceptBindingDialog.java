package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
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
    private Text  hardware;
    private Label errorLabel;
    private Text  io;
    private Text  software;
    private Text  tick;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditCodeGenConceptBindingDialog(Shell parentShell) {
        super(parentShell, "Edit Data Model");
        setTitle("\u8BBE\u8BA1\u4EE3\u7801\u751F\u6210\u6982\u5FF5\u4E0E\u5B9E\u4F53\u7ED1\u5B9A");
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
        label.setText("\u786C\u4EF6");

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setAlignment(SWT.RIGHT);
        lblNewLabel.setBounds(20, 63, 36, 17);
        lblNewLabel.setText("\u8F6F\u4EF6");

        hardware = new Text(container, SWT.BORDER);
        hardware.setEnabled(false);
        hardware.setBounds(69, 17, 183, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 175, 354, 17);

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setAlignment(SWT.RIGHT);
        lblNewLabel_2.setBounds(20, 143, 36, 17);
        lblNewLabel_2.setText("Tick");

        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setAlignment(SWT.RIGHT);
        lblNewLabel_3.setBounds(20, 103, 36, 17);
        lblNewLabel_3.setText("I/O");

        io = new Text(container, SWT.BORDER);
        io.setEnabled(false);
        io.setBounds(69, 97, 183, 23);

        software = new Text(container, SWT.BORDER);
        software.setEnabled(false);
        software.setText("");
        software.setBounds(69, 57, 183, 23);

        tick = new Text(container, SWT.BORDER);
        tick.setEnabled(false);
        tick.setText("");
        tick.setBounds(69, 137, 183, 23);

        Button btnHardware = new Button(container, SWT.NONE);
        btnHardware.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                dialog.open();

                hardware.setText(dialog.getSelectEntity());

            }
        });
        btnHardware.setBounds(276, 18, 72, 22);
        btnHardware.setText("\u9009\u62E9\u5B9E\u4F53");

        Button btn = new Button(container, SWT.NONE);
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                dialog.open();

                software.setText(dialog.getSelectEntity());

            }
        });
        btn.setText("\u9009\u62E9\u5B9E\u4F53");
        btn.setBounds(276, 58, 72, 22);

        Button button_1 = new Button(container, SWT.NONE);
        button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                dialog.open();

                io.setText(dialog.getSelectEntity());

            }
        });
        button_1.setText("\u9009\u62E9\u5B9E\u4F53");
        button_1.setBounds(276, 97, 72, 22);

        Button button_2 = new Button(container, SWT.NONE);
        button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog = new EntitySelectionFromLibDialog(shell);
                dialog.open();

                tick.setText(dialog.getSelectEntity());

            }
        });
        button_2.setText("\u9009\u62E9\u5B9E\u4F53");
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
        button.setText("\u786E\u5B9A");
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText("\u53D6\u6D88");
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
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        hardware.setText(projectModel.getHardwareEntity());
        software.setText(projectModel.getSoftwareEntity());
        io.setText(projectModel.getIoEntity());
        tick.setText(projectModel.getTickEntity());
    }


    @Override
    protected void updateValues() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        projectModel.setHardwareEntity(hardware.getText());
        projectModel.setSoftwareEntity(software.getText());
        projectModel.setIoEntity(io.getText());
        projectModel.setTickEntity(tick.getText());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (hardware.getText().equals("") || software.getText().equals("")
                        || io.getText().equals("") || tick.getText().equals("")) {
            handleError("必须为每一个概念选择一个实体。");
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
                handleError("有重复项。");
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
