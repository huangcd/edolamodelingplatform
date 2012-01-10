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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class CodeGenerateDialog extends AbstractEditDialog {

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public CodeGenerateDialog(Shell parentShell) {
        super(parentShell, "Edit Data Model");
        setTitle("\u4EE3\u7801\u751F\u6210");
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
        group.setBounds(10, 0, 228, 144);
        group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        group.setLayout(null);

        Label lblNewLabel = new Label(group, SWT.NONE);
        lblNewLabel.setBounds(10, 17, 48, 22);
        lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        lblNewLabel.setText("\u76EE\u6807\u5E73\u53F0");

        Combo combo = new Combo(group, SWT.READ_ONLY);
        combo.setBounds(98, 14, 100, 25);
        combo.setItems(new String[] {"PLC"});
        combo.setText("PLC");

        Label label = new Label(group, SWT.NONE);
        label.setBounds(10, 47, 48, 17);
        label.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        label.setText("\u4F18\u5316\u9009\u9879");

        Combo combo_1 = new Combo(group, SWT.READ_ONLY);
        combo_1.setBounds(98, 43, 100, 25);
        combo_1.setItems(new String[] {"1"});
        combo_1.setText("1");

        Label label_1 = new Label(group, SWT.NONE);
        label_1.setBounds(10, 75, 48, 22);
        label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        label_1.setText("\u8C03\u8BD5\u4FE1\u606F");

        Combo combo_2 = new Combo(group, SWT.READ_ONLY);
        combo_2.setBounds(98, 72, 100, 25);
        combo_2.setItems(new String[] {"1"});
        combo_2.setText("1");

        Button editMapping = new Button(group, SWT.NONE);
        editMapping.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = Display.getCurrent().getActiveShell();
                IWorkbench workbench = PlatformUI.getWorkbench();
                IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                IWorkbenchPage page = window.getActivePage();
                IEditorPart editor = page.getActiveEditor();
                if (editor instanceof CompoundEditor) {
                    CompoundTypeModel model =
                                    (CompoundTypeModel) ((CompoundEditor) editor).getModel();
                    EditRelationDialog dialog = new EditRelationDialog(shell, model);
                    dialog.setBlockOnOpen(true);
                    if (Dialog.OK == dialog.open()) {

                    }

                }

            }
        });
        editMapping.setBounds(52, 108, 80, 27);
        editMapping.setText("\u7F16\u8F91\u6620\u5C04\u5173\u7CFB");

        Button editDevices = new Button(group, SWT.NONE);
        editDevices.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = Display.getCurrent().getActiveShell();
                IWorkbench workbench = PlatformUI.getWorkbench();
                IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
                IWorkbenchPage page = window.getActivePage();
                IEditorPart editor = page.getActiveEditor();
                if (editor instanceof CompoundEditor) {
                    CompoundTypeModel model =
                                    (CompoundTypeModel) ((CompoundEditor) editor).getModel();
                    EditPeripheralsDialog dialog = new EditPeripheralsDialog(shell, model);
                    dialog.setBlockOnOpen(true);
                    if (Dialog.OK == dialog.open()) {

                    }

                }

            }
        });
        editDevices.setBounds(138, 108, 80, 27);
        editDevices.setText("\u7F16\u8F91\u5916\u8BBE");


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
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String workplace = System.getProperty("user.dir");
                String filePathDD = workplace + "/data/test-dd.xml";
                String filePathMapping = workplace + "/data/test-mapping.xml";

                CodeGenManager cgm = new CodeGenManager(filePathDD, filePathMapping);

                if (!cgm.checkValid()) {
                    return;
                }
            }
        });
        button.setText("\u4EE3\u7801\u751F\u6210");
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
        return new Point(262, 239);
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void updateValues() {

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
