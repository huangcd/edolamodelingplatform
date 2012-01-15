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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Binding;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditRelationDialog extends AbstractEditDialog {
    private Table       table;
    private Label       errorLabel;
    CodeGenManager      cgm;
    CodeGenProjectModel cgpm = null;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditRelationDialog(Shell parentShell) {
        super(parentShell, Messages.EditRelationDialog_0);
        setTitle(Messages.EditRelationDialog_1);

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.EditRelationDialog_2);
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

        Button button = new Button(container, SWT.NONE);
        button.setBounds(522, 15, 96, 27);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ArrayList<String> variables = new ArrayList<String>();

                for (TableItem item : table.getItems())
                    variables.add(item.getText(0));

                EditMappingDialog dialog =
                                new EditMappingDialog(Display.getCurrent().getActiveShell(), null,
                                                variables);
                if (Window.OK == dialog.open()) {
                    Binding b = dialog.getData();

                    TableItem tableItem = new TableItem(table, SWT.NONE);

                    tableItem.setText(new String[] {b.var, b.type, String.format("%d", b.addr), //$NON-NLS-1$
                                    String.format("%d", b.length), String.format("%d", b.bit)}); //$NON-NLS-1$ //$NON-NLS-2$

                    tableItem.setData(b);

                }
            }
        });
        button.setText(Messages.EditRelationDialog_6);

        Button button_1 = new Button(container, SWT.NONE);
        button_1.setBounds(522, 50, 96, 27);
        button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                EditMappingDialog dialog =
                                new EditMappingDialog(Display.getCurrent().getActiveShell(),
                                                (Binding) items[0].getData(), null);
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    Binding b = dialog.getData();
                    items[0].setText(new String[] {b.var, b.type, String.format("%d", b.addr), //$NON-NLS-1$
                                    String.format("%d", b.length), String.format("%d", b.bit)}); //$NON-NLS-1$ //$NON-NLS-2$

                    items[0].setData(b);
                }



            }
        });
        button_1.setText(Messages.EditRelationDialog_10);

        Button button_2 = new Button(container, SWT.NONE);
        button_2.setBounds(522, 86, 96, 27);
        button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                table.remove(table.getSelectionIndices());
            }
        });
        button_2.setText(Messages.EditRelationDialog_11);

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(10, 15, 504, 307);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(100);
        tblclmnNewColumn.setText(Messages.EditRelationDialog_12);

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(100);
        tableColumn.setText(Messages.EditRelationDialog_13);

        TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
        tableColumn_1.setWidth(100);
        tableColumn_1.setText(Messages.EditRelationDialog_14);

        TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn_1.setText(Messages.EditRelationDialog_15);
        tblclmnNewColumn_1.setWidth(100);

        TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
        tableColumn_2.setWidth(100);
        tableColumn_2.setText(Messages.EditRelationDialog_16);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 332, 591, 17);

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
        button.setText(Messages.EditRelationDialog_17);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditRelationDialog_18);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(634, 444);
    }

    @Override
    protected void initValues() {
        for (Binding b : cgm.m.getBindings()) {

            TableItem tableItem = new TableItem(table, SWT.NONE);

            tableItem.setText(new String[] {b.var, b.type, String.format("%d", b.addr), //$NON-NLS-1$
                            String.format("%d", b.length), String.format("%d", b.bit)}); //$NON-NLS-1$ //$NON-NLS-2$

            tableItem.setData(b);
        }

    }

    @Override
    protected void updateValues() {

        cgm.m.clearAllBindings();

        for (TableItem item : table.getItems()) {

            cgm.m.getBindings().add((Binding) item.getData());
        }

        cgm.saveAll();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (cgpm == null) {
            handleError(Messages.EditRelationDialog_22);
            return false;
        }

        for (TableItem item : table.getItems()) {
            if (!this.cgpm.getStartupModel().checkExistenceByName(item.getText(0))) {
                this.handleError(Messages.EditRelationDialog_23 + item.getText(0));
                return false;
            }

            cgm.m.getBindings().add((Binding) item.getData());
        }

        return true;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }
}
