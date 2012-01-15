package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class NewLibraryNameWizardPage extends WizardPage {
    private Text             textName;
    private Text             textLocation;
    private CreateWizardData data;

    /**
     * Create the wizard.
     */
    public NewLibraryNameWizardPage(CreateWizardData data) {
        super(Messages.NewLibraryNameWizardPage_0);
        setImageDescriptor(ResourceManager.getPluginImageDescriptor("EdolaModelingPlatform", //$NON-NLS-1$
                        "icons/product_wiz.gif")); //$NON-NLS-1$
        setPageComplete(false);
        setTitle(Messages.NewLibraryNameWizardPage_3);
        setDescription(Messages.NewLibraryNameWizardPage_4);
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
        lblNewLabel.setText(Messages.NewLibraryNameWizardPage_5);

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
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText(Messages.NewLibraryNameWizardPage_6);

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
        buttonBrowse.setText(Messages.NewLibraryNameWizardPage_7);

        initValues();
    }

    /**
     * 选择项目文件夹
     */
    protected void chooseDirection() {
        DirectoryDialog dialog =
                        new DirectoryDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterPath(Activator.getPreferenceDirection().getAbsolutePath());
        dialog.setText(Messages.NewLibraryNameWizardPage_8);
        dialog.setText(Messages.NewLibraryNameWizardPage_9);
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
        } else {
            textLocation.setText(Activator.getPreferenceDirection().getAbsolutePath());
        }
    }

    @Override
    public boolean isPageComplete() {
        if (textLocation.getText().isEmpty()) {
            return false;
        }
        String path = textLocation.getText();
        try {
            if (!path.contains(":")) { //$NON-NLS-1$
                path = Activator.getPreferenceDirection() + "/" + path; //$NON-NLS-1$
            }
            File file = new File(path);
            if (file.createNewFile()) {
                file.delete();
            }
            String fullPath = file.getCanonicalPath();
            String libraryLocation = Activator.getPreferenceDirection().getCanonicalPath();
             if (!fullPath.startsWith(libraryLocation)) {
                setErrorMessage(Messages.NewLibraryNameWizardPage_12 + libraryLocation + "下"); //$NON-NLS-2$
                //textLocation.setText(Activator.getPreferenceDirection().getCanonicalPath());
                return false;
            }
            setErrorMessage(null);
            setMessage(Messages.NewLibraryNameWizardPage_14);
            getContainer().updateMessage();
        } catch (IOException ex) {
            setErrorMessage(Messages.NewLibraryNameWizardPage_15);
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
