package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PriorityModel;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("all")
public class EditPrioritiesDialog extends AbstractEditDialog {
    private Table             table;
    private Label             errorLabel;
    private CompoundTypeModel ctm;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditPrioritiesDialog(Shell parentShell, CompoundTypeModel ctm) {
        super(parentShell, Messages.EditPrioritiesDialog_0);
        setTitle(Messages.EditPrioritiesDialog_1);

        this.ctm = ctm;
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


                EditOnePriorityDialog dialog =
                                new EditOnePriorityDialog(Display.getCurrent().getActiveShell(),
                                                ctm, null);
                if (Window.OK == dialog.open()) {
                    PriorityModel<CompoundTypeModel> pm = dialog.getData();
                    TableItem tableItem = new TableItem(table, SWT.NONE);
                    tableItem.setText(new String[] {pm.getName(), pm.getLeftPort().getName(),
                                    pm.getRightPort().getName()});
                    tableItem.setData(pm);
                }



            }
        });
        newDevice.setText(Messages.EditPrioritiesDialog_2);

        Button modifyDevice = new Button(container, SWT.NONE);
        modifyDevice.setBounds(504, 48, 96, 27);
        modifyDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                EditOnePriorityDialog dialog =
                                new EditOnePriorityDialog(Display.getCurrent().getActiveShell(),
                                                ctm, (PriorityModel<CompoundTypeModel>) items[0]
                                                                .getData());
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    PriorityModel<CompoundTypeModel> pm = dialog.getData();
                    items[0].setText(new String[] {pm.getName(), pm.getLeftPort().getName(),
                                    pm.getRightPort().getName()});
                    items[0].setData(pm);
                }



            }
        });
        modifyDevice.setText(Messages.EditPrioritiesDialog_3);

        Button deleteDevice = new Button(container, SWT.NONE);
        deleteDevice.setBounds(504, 81, 96, 27);
        deleteDevice.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int[] items = table.getSelectionIndices();
                if (items.length == 0)
                    return;
                else
                    table.remove(items);
            }
        });
        deleteDevice.setText(Messages.EditPrioritiesDialog_4);

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(11, 15, 487, 311);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
        tableColumn_1.setWidth(161);
        tableColumn_1.setText(Messages.EditPrioritiesDialog_5);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(148);
        tblclmnNewColumn.setText(Messages.EditPrioritiesDialog_6);

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(140);
        tableColumn.setText(Messages.EditPrioritiesDialog_7);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(11, 332, 589, 17);

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
        button.setText(Messages.EditPrioritiesDialog_8);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditPrioritiesDialog_9);
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
        for (PriorityModel<CompoundTypeModel> pm : ctm.getPriorities()) {

            TableItem tableItem = new TableItem(table, SWT.NONE);

            tableItem.setText(new String[] {pm.getName(), pm.getLeftPort().getName(),
                            pm.getRightPort().getName()});

            tableItem.setData(pm);
        }
    }

    @Override
    protected void updateValues() {
        ctm.getPriorities().clear();

        for (TableItem item : table.getItems()) {
            PriorityModel<CompoundTypeModel> pm = (PriorityModel<CompoundTypeModel>) item.getData();
            ctm.getPriorities().add(pm);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }
}
