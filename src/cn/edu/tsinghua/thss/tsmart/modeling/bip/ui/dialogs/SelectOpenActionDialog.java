package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;
@SuppressWarnings("rawtypes")
public class SelectOpenActionDialog extends AbstractEditDialog {

    private int    selection;
    private Button button_newProject;
    private Button button_openProject;
    private Button button_newLibrary;
    private Button button_openLibrary;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public SelectOpenActionDialog(Shell parentShell) {
        super(parentShell, Messages.SelectOpenActionDialog_0);
        setTitle(Messages.SelectOpenActionDialog_1);
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
        group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        group.setBounds(0, 0, 235, 168);

        Label labelType = new Label(group, SWT.NONE);
        labelType.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        labelType.setLocation(10, 23);
        labelType.setSize(67, 18);
        labelType.setText(Messages.SelectOpenActionDialog_2);

        Label labelName = new Label(group, SWT.NONE);
        labelName.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        labelName.setLocation(10, 98);
        labelName.setSize(93, 18);
        labelName.setText(Messages.SelectOpenActionDialog_3);

        button_newProject = new Button(group, SWT.RADIO);
        button_newProject.setBounds(50, 47, 93, 16);
        button_newProject.setText(Messages.SelectOpenActionDialog_4);

        button_openProject = new Button(group, SWT.RADIO);
        button_openProject.setBounds(50, 70, 93, 16);
        button_openProject.setText(Messages.SelectOpenActionDialog_5);

        button_newLibrary = new Button(group, SWT.RADIO);
        button_newLibrary.setBounds(50, 117, 93, 16);
        button_newLibrary.setText(Messages.SelectOpenActionDialog_6);

        button_openLibrary = new Button(group, SWT.RADIO);
        button_openLibrary.setBounds(50, 139, 93, 16);
        button_openLibrary.setText(Messages.SelectOpenActionDialog_7);

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
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected void updateValues() {
        if (button_newProject.getSelection()) {
            this.selection = GlobalProperties.NEWPROJECT;
        } else if (button_openProject.getSelection()) {
            this.selection = GlobalProperties.OPENPROJECT;
        } else if (button_newLibrary.getSelection()) {
            this.selection = GlobalProperties.NEWLIBRARY;
        } else if (button_openLibrary.getSelection()) {
            this.selection = GlobalProperties.OPENLIBRARY;
        } 
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

    @Override
    protected void initValues() {
        this.selection = GlobalProperties.NEWPROJECT;

    }

    public int getSelection() {
        return selection;
    }

    protected void setShellStyle(int arg0) {
        super.setShellStyle(SWT.TITLE);
    }

}
