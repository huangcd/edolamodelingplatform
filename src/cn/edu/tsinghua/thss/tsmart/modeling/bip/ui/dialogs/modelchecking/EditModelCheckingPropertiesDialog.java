package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingManager;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingProperty;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditModelCheckingPropertiesDialog extends AbstractEditDialog {
    private Table        table;
    private Label        errorLabel;

    ModelCheckingManager mcm  = null;
    CodeGenProjectModel  cgpm = null;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditModelCheckingPropertiesDialog(Shell parentShell) {
        super(parentShell, Messages.EditModelCheckingPropertiesDialog_0);
        setTitle(Messages.EditModelCheckingPropertiesDialog_1);

        // 需要把其他对话框对model的引用改成这个 TODO
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.EditModelCheckingPropertiesDialog_2);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        cgpm = (CodeGenProjectModel) topModel;
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

        Button newDevice = new Button(container, SWT.NONE);
        newDevice.setBounds(504, 15, 96, 27);
        newDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                EditPropertyDialog dialog =
                                new EditPropertyDialog(Display.getCurrent().getActiveShell(), null);

                if (Window.OK == dialog.open()) {
                    ModelCheckingProperty p = dialog.getData();
                    TableItem tableItem = new TableItem(table, SWT.NONE);
                    tableItem.setText(new String[] {p.description, p.property});
                    tableItem.setData(p);
                }
            }
        });
        newDevice.setText(Messages.EditModelCheckingPropertiesDialog_3);

        Button modifyDevice = new Button(container, SWT.NONE);
        modifyDevice.setBounds(504, 48, 96, 27);
        modifyDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                EditPropertyDialog dialog =
                                new EditPropertyDialog(Display.getCurrent().getActiveShell(),
                                                (ModelCheckingProperty) items[0].getData());
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    ModelCheckingProperty p = dialog.getData();
                    items[0].setText(new String[] {p.description, p.property});
                    items[0].setData(p);
                }



            }
        });
        modifyDevice.setText(Messages.EditModelCheckingPropertiesDialog_4);

        Button deleteDevice = new Button(container, SWT.NONE);
        deleteDevice.setBounds(504, 81, 96, 27);
        deleteDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                // items[0].get
                // TODO 删除
                // table.remove(items[0]);


            }
        });
        deleteDevice.setText(Messages.EditModelCheckingPropertiesDialog_5);

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(11, 15, 487, 311);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(178);
        tblclmnNewColumn.setText(Messages.EditModelCheckingPropertiesDialog_6);

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(272);
        tableColumn.setText(Messages.EditModelCheckingPropertiesDialog_7);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(11, 332, 589, 17);

        mcm = new ModelCheckingManager();

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



            }
        });
        button.setText(Messages.EditModelCheckingPropertiesDialog_8);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditModelCheckingPropertiesDialog_9);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(617, 444);
    }

    @Override
    protected void initValues() {

        for (ModelCheckingProperty p : mcm.mcps.properties) {

            TableItem tableItem = new TableItem(table, SWT.NONE);

            tableItem.setText(new String[] {p.description, p.property});

            tableItem.setData(p);
        }

    }

    @Override
    protected void updateValues() {

        mcm.mcps.clearAllProperties();

        for (TableItem item : table.getItems()) {

            mcm.mcps.properties.add((ModelCheckingProperty) item.getData());
        }

        mcm.saveAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (cgpm == null) {
            handleError(Messages.EditModelCheckingPropertiesDialog_10);
            return false;
        }



        // TODO



        return true;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }
}
