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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Binding;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.CodeGenManager;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditRelationDialog extends AbstractEditDialog {
    private Table     table;
    private Label     errorLabel;
    CodeGenManager    cgm;
    CompoundTypeModel ctm;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditRelationDialog(Shell parentShell, CompoundTypeModel ctm) {
        super(parentShell, "Edit Data Model");
        setTitle("\u7F16\u8F91\u53D8\u91CF\u6620\u5C04\u5173\u7CFB");
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

        Button button = new Button(container, SWT.NONE);
        button.setBounds(504, 15, 96, 20);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ArrayList<String> variables = new ArrayList<String>();

                for (TableItem item : table.getItems())
                    variables.add(item.getText(0));



                EditMappingDialog dialog =
                                new EditMappingDialog(Display.getCurrent().getActiveShell(), null,
                                                ctm, variables);
                if (Window.OK == dialog.open()) {
                    Binding b = dialog.getData();
                    
                    TableItem tableItem = new TableItem(table, SWT.NONE);

                    tableItem.setText(new String[] {b.var, b.type, String.format("%d", b.addr),
                                    String.format("%d", b.length), String.format("%d", b.bit)});

                    tableItem.setData(b);

                }
            }
        });
        button.setText("\u65B0\u5EFA\u6620\u5C04");

        Button button_1 = new Button(container, SWT.NONE);
        button_1.setBounds(504, 50, 96, 27);
        button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                EditMappingDialog dialog =
                                new EditMappingDialog(Display.getCurrent().getActiveShell(),
                                                (Binding) items[0].getData(), ctm, null);
                dialog.setBlockOnOpen(true);
                if (Window.OK == dialog.open()) {
                    Binding b = dialog.getData();
                    items[0].setText(new String[] {b.var, b.type, String.format("%d", b.addr),
                                    String.format("%d", b.length), String.format("%d", b.bit)});
                    
                    items[0].setData(b);
                }



            }
        });
        button_1.setText("\u4FEE\u6539\u6620\u5C04");

        Button button_2 = new Button(container, SWT.NONE);
        button_2.setBounds(504, 92, 96, 27);
        button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TableItem[] items = table.getSelection();
                if (items.length == 0) return;

                // items[0].get
                // TODO 林颖，删除选中 items
                // table.remove(items[0]);


            }
        });
        button_2.setText("\u5220\u9664\u6620\u5C04");

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(11, 15, 487, 307);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(180);
        tblclmnNewColumn.setText("\u53D8\u91CF\u540D");

        TableColumn tableColumn = new TableColumn(table, SWT.NONE);
        tableColumn.setWidth(65);
        tableColumn.setText("\u8F93\u5165/\u8F93\u51FA");

        TableColumn tableColumn_1 = new TableColumn(table, SWT.NONE);
        tableColumn_1.setWidth(70);
        tableColumn_1.setText("\u5730\u5740");

        TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn_1.setText("\u957F\u5EA6");
        tblclmnNewColumn_1.setWidth(65);

        TableColumn tableColumn_2 = new TableColumn(table, SWT.NONE);
        tableColumn_2.setWidth(45);
        tableColumn_2.setText("\u4F4D");

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 332, 591, 17);

        String workplace = System.getProperty("user.dir");
        String filePathDD = workplace + "/data/GateCtrl-devices.xml";
        String filePathMapping = workplace + "/data/GateCtrl-mapping.xml";

        cgm = new CodeGenManager(filePathDD, filePathMapping);

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
        return new Point(617, 444);
    }

    @Override
    protected void initValues() {
        for (Binding b : cgm.m.getBindings()) {

            TableItem tableItem = new TableItem(table, SWT.NONE);

            tableItem.setText(new String[] {b.var, b.type, String.format("%d", b.addr),
                            String.format("%d", b.length), String.format("%d", b.bit)});

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

        for (TableItem item : table.getItems()) {

            if (!ctm.checkExistenceByName(item.getText(0))) {
                this.handleError("模型中不存在变量：" + item.getText(0));
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
