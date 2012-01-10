package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;

public class NewProjectNameWizardPage extends WizardPage {
    private Text              textName;
    private Text              textLocation;
    private CreateWizardData data;

    /**
     * Create the wizard.
     */
    public NewProjectNameWizardPage(CreateWizardData data) {
        super("set project info");
        setImageDescriptor(ResourceManager.getPluginImageDescriptor("EdolaModelingPlatform",
                        "icons/product_wiz.gif"));
        setPageComplete(false);
        setTitle("\u9879\u76EE\u914D\u7F6E");
        setDescription("\u8BBE\u7F6E\u9879\u76EE\u540D\u79F0\u53CA\u5B58\u50A8\u4F4D\u7F6E");
        this.data = data;
    }

    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new GridLayout(3, false));
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setText("\u9879\u76EE\u540D\u79F0\uFF1A");

        textName = new Text(container, SWT.BORDER);
        textName.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                update();
            }
        });
        textName.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                update();
            }
        });
        textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);
        new Label(container, SWT.NONE);

        Label lblNewLabel_1 = new Label(container, SWT.RIGHT);
        lblNewLabel_1.setText("\u4FDD\u5B58\u4F4D\u7F6E\uFF1A");

        textLocation = new Text(container, SWT.BORDER);
        textLocation.addVerifyListener(new VerifyListener() {
            public void verifyText(VerifyEvent e) {
                update();
            }
        });
        textLocation.addModifyListener(new ModifyListener() {
            public void modifyText(ModifyEvent e) {
                update();
            }
        });
        GridData gd_textLocation = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_textLocation.widthHint = 346;
        textLocation.setLayoutData(gd_textLocation);

        Button buttonBrowse = new Button(container, SWT.NONE);
        buttonBrowse.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseDirection();
            }
        });
        buttonBrowse.setText("\u6D4F\u89C8...");

        initValues();
    }

    /**
     * 选择项目文件夹
     */
    protected void chooseDirection() {
        DirectoryDialog dialog =
                        new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText("选择保存位置");
        dialog.setText("选择项目保存的位置");
        String path = dialog.open();
        if (path != null && !path.isEmpty()) {
            textLocation.setText(path);
            update();
        }
    }

    private void initValues() {
        if (data.getName() != null) {
            textName.setText(data.getName());
        }
        if (data.getLocation() != null) {
            textLocation.setText(data.getLocation());
        }
    }

    @Override
    public boolean isPageComplete() {
        if (textLocation.getText().isEmpty()) {
            return false;
        }
        String path = textLocation.getText();
        try {
            File file = new File(path);
            if (file.createNewFile()) {
                file.delete();
            }
            setErrorMessage(null);
            setMessage("\u8BBE\u7F6E\u9879\u76EE\u540D\u79F0\u53CA\u5B58\u50A8\u4F4D\u7F6E");
            getContainer().updateMessage();
        } catch (IOException ex) {
            setErrorMessage("路径不正确");
            return false;
        }
        if (textName.getText().isEmpty()) {
            return false;
        }
        return true;
    }

    private void update() {
        data.setName(textName.getText());
        data.setLocation(textLocation.getText());
        setPageComplete(isPageComplete());
    }
}
