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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditPeripheralsDialog extends AbstractEditDialog {
    private Table  table;
    private Label  errorLabel;
    CodeGenManager cgm;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditPeripheralsDialog(Shell parentShell) {
        super(parentShell, Messages.EditPeripheralsDialog_0);
        setTitle(Messages.EditPeripheralsDialog_1);
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
                ArrayList<String> names = new ArrayList<String>();

                for (TableItem item : table.getItems()) {
                    names.add(item.getText(0));
                }

                EditOnePeripheralDialog dialog =
                                new EditOnePeripheralDialog(Display.getCurrent().getActiveShell(),
                                                null, names);
                if (Window.OK == dialog.open()) {
                    Device d = dialog.getData();
                    TableItem tableItem = new TableItem(table, SWT.NONE);
                    tableItem.setText(new String[] {d.name, d.description});
                    tableItem.setData(d);
                }
            }
        });
        newDevice.setText(Messages.EditPeripheralsDialog_2);

        Button modifyDevice = new Button(container, SWT.NONE);
        modifyDevice.setBounds(504, 48, 96, 27);
        modifyDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                EditOnePeripheralDialog dialog =
                                new EditOnePeripheralDialog(Display.getCurrent().getActiveShell(),
                                                (Device) items[0].getData(), null);
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    Device d = dialog.getData();
                    items[0].setText(new String[] {d.name, d.description});
                    items[0].setData(d);
                }
            }
        });
        modifyDevice.setText(Messages.EditPeripheralsDialog_3);

        Button deleteDevice = new Button(container, SWT.NONE);
        deleteDevice.setBounds(504, 81, 96, 27);
        deleteDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int[] items = table.getSelectionIndices();
                if (items.length == 0) return;
                else
                    table.remove(items);               
            }
        });
        deleteDevice.setText(Messages.EditPeripheralsDialog_4);

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(11, 15, 487, 311);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(268);
        tblclmnNewColumn.setText(Messages.EditPeripheralsDialog_5);

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(252);
        tableColumn.setText(Messages.EditPeripheralsDialog_6);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(11, 332, 589, 17);

        cgm = new CodeGenManager();

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
        button.setText(Messages.EditPeripheralsDialog_7);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditPeripheralsDialog_8);
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
        for (Device d : cgm.dd.devices) {
            TableItem tableItem = new TableItem(table, SWT.NONE);
            tableItem.setText(new String[] {d.name, d.description});
            tableItem.setData(d);
        }
    }

    @Override
    protected void updateValues() {
        cgm.dd.clearAllDevices();
        for (TableItem item : table.getItems()) {
            cgm.dd.devices.add((Device) item.getData());
        }
        cgm.saveAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        // TODO 放到哪里合适？
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }
}
