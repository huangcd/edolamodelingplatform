package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.LinkBind;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.PropertyEntry;
import cn.edu.tsinghua.thss.virtualenv.plc.devices.devs.FixedSpeedMotor;
import cn.edu.tsinghua.thss.virtualenv.plc.devices.devs.Light;
import cn.edu.tsinghua.thss.virtualenv.plc.devices.devs.PushButton;
import cn.edu.tsinghua.thss.virtualenv.plc.devices.devs.VariantSpeedMotor;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditOnePeripheralDialog extends AbstractEditDialog {
    private Device            dev;
    private ArrayList<String> names;

    private Text              name;
    private Label             errorLabel;
    private Table             tableVariables;
    private Combo             type;
    private Table             tableIOBinding;
    private Text              description;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditOnePeripheralDialog(Shell parentShell, Device dev, ArrayList<String> names) {
        super(parentShell, "Edit Data Model");
        setTitle("\u7F16\u8F91\u5916\u8BBE");
        this.dev = dev;
        this.names = names;
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
        label.setBounds(57, 25, 36, 17);
        label.setText("\u5916\u8BBE\u540D");

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(40, 64, 53, 17);
        lblNewLabel.setText("\u5916\u8BBE\u7C7B\u578B");

        name = new Text(container, SWT.BORDER);
        name.setBounds(99, 22, 183, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 473, 655, 17);

        type = new Combo(container, SWT.READ_ONLY);
        type.setTouchEnabled(true);
        type.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (type.getText().equals("PushButton") && dev instanceof PushButton) return;

                if (type.getText().equals("FixedSpeedMotor") && dev instanceof FixedSpeedMotor)
                    return;

                if (type.getText().equals("VariantSpeedMotor") && dev instanceof VariantSpeedMotor)
                    return;

                newDeviceBasedOnType();
                // removeAllText(tableVariables);
                // removeAllText(tableIOBinding);
                refleshValues();
            }
        });
        type.setItems(new String[] {"PushButton", "FixedSpeedMotor", "VariantSpeedMotor"});
        type.setBounds(99, 61, 184, 25);
        type.select(0);
        type.setText("PushButton\r\nFixedSpeedMotor\r\nVariantSpeedMotor");

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setBounds(21, 323, 72, 17);
        lblNewLabel_1.setText("\u5916\u8BBE\u7AEF\u53E3\u7ED1\u5B9A");

        tableVariables = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableVariables.setBounds(99, 135, 566, 182);
        tableVariables.setHeaderVisible(true);
        tableVariables.setLinesVisible(true);

        TableColumn tableColumn = new TableColumn(tableVariables, SWT.NONE);
        tableColumn.setWidth(125);
        tableColumn.setText("\u521D\u59CB\u5316\u53D8\u91CF");


        TableColumn tableColumn_1 = new TableColumn(tableVariables, SWT.NONE);
        tableColumn_1.setWidth(100);
        tableColumn_1.setText("\u503C");

        tableIOBinding = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableIOBinding.setBounds(99, 323, 566, 144);
        tableIOBinding.setHeaderVisible(true);
        tableIOBinding.setLinesVisible(true);

        TableColumn tableColumn_2 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_2.setWidth(136);
        tableColumn_2.setText("\u53D8\u91CF\u540D");

        TableColumn tableColumn_6 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_6.setWidth(100);
        tableColumn_6.setText("\u8F93\u5165/\u8F93\u51FA");

        TableColumn tableColumn_3 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_3.setWidth(100);
        tableColumn_3.setText("\u5730\u5740");

        TableColumn tableColumn_4 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_4.setWidth(100);
        tableColumn_4.setText("\u957F\u5EA6");

        TableColumn tableColumn_5 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_5.setWidth(100);
        tableColumn_5.setText("\u4F4D");

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setBounds(32, 135, 61, 17);
        lblNewLabel_2.setText("\u521D\u59CB\u5316\u53D8\u91CF");

        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setBounds(69, 103, 24, 17);
        lblNewLabel_3.setText("\u63CF\u8FF0");

        description = new Text(container, SWT.BORDER);
        description.setBounds(99, 97, 566, 23);

        if (dev != null) {
            name.setEnabled(false);
            type.setEnabled(false);
        }

        initValues();
        // 编辑事件
        {
            tableVariables.addMouseListener(new MouseAdapter() {
                public void mouseDown(MouseEvent e) {
                    removeAllText(tableVariables);

                    TableEditor editor = new TableEditor(tableVariables);
                    editor.horizontalAlignment = SWT.LEFT;
                    editor.grabHorizontal = true;
                    Control c = editor.getEditor();
                    if (c != null) {
                        c.dispose();
                    }
                    Point point = new Point(e.x, e.y);
                    final TableItem tableitem = tableVariables.getItem(point);
                    final int column = getTableColumn(tableVariables, point);
                    if (column == 1) {
                        editAsText(tableitem, column, editor, tableVariables);
                    }
                }
            });

            tableIOBinding.addMouseListener(new MouseAdapter() {
                public void mouseDown(MouseEvent e) {
                    removeAllText(tableIOBinding);

                    TableEditor editor = new TableEditor(tableIOBinding);
                    editor.horizontalAlignment = SWT.LEFT;
                    editor.grabHorizontal = true;
                    Control c = editor.getEditor();
                    if (c != null) {
                        c.dispose();
                    }
                    Point point = new Point(e.x, e.y);
                    final TableItem tableitem = tableIOBinding.getItem(point);
                    final int column = getTableColumn(tableIOBinding, point);
                    if (column == 2 || column == 3 || column == 4) {
                        editAsText(tableitem, column, editor, tableIOBinding);
                    }
                }
            });
        }
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
        return new Point(681, 583);
    }

    @Override
    protected void initValues() {
        if (dev == null) newDeviceBasedOnType();

        refleshValues();
    }

    private void newDeviceBasedOnType() {
        String t = type.getText();

        if (t.equals("PushButton")) {
            dev = new PushButton();

        } else if (t.equals("FixedSpeedMotor")) {
            dev = new FixedSpeedMotor();

        } else if (t.equals("VariantSpeedMotor")) {
            dev = new VariantSpeedMotor();
        }

        dev.initValues();
    }

    @Override
    protected void updateValues() {
        dev.name = name.getText();
        dev.description = description.getText();

        // TODO 更新 links 和 initializer
    }

    protected void refleshValues() {
        name.setText("");
        description.setText("");
        tableIOBinding.removeAll();
        tableVariables.removeAll();

        if (dev != null) {
            name.setText(dev.name);

            if (dev instanceof Light) {
                type.setText("Light");
            } else if (dev instanceof FixedSpeedMotor) {
                type.setText("FixedSpeedMotor");
            } else if (dev instanceof PushButton) {
                type.setText("PushButton");
            } else if (dev instanceof VariantSpeedMotor) {
                type.setText("VariantSpeedMotor");
            }

            for (LinkBind l : dev.linking.links) {
                TableItem tableItem = new TableItem(tableIOBinding, SWT.NONE);

                tableItem.setText(new String[] {l.var, l.type, String.format("%d", l.addr),
                                String.format("%d", l.length), String.format("%d", l.bit)});

                tableItem.setData(l);
            }

            for (PropertyEntry p : dev.initializer.properties) {
                TableItem tableItem = new TableItem(tableVariables, SWT.NONE);

                tableItem.setText(new String[] {p.property, p.value});

                tableItem.setData(p);
            }

            description.setText(dev.description);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (names != null && names.contains(name.getText())) {
            handleError("外设名已存在。");
            return false;
        }

        // TODO 检查 links 和 initializer 是否合法



        return true;
    }


    public Device getData() {
        return dev;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }

    private void editAsText(final TableItem tableItem, final int col, TableEditor tableEditor,
                    Table table) {
        final Text txt = new Text(table, SWT.NONE);
        txt.setText(tableItem.getText(col));
        tableEditor.setEditor(txt, tableItem, col);
        txt.forceFocus();
        txt.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                tableItem.setText(col, txt.getText());
            }
        });
    }

    private void removeAllText(Table table) {
        Control[] children = table.getChildren();
        for (Control child : children) {
            if (child.getClass().equals(Text.class)) {
                child.setEnabled(false);
                child.setVisible(false);
            }
        }
    }

    private int getTableColumn(Table table, Point point) {
        final TableItem tableitem = table.getItem(point);
        int column = -1;
        if (tableitem == null) return -1;
        for (int i = 0; i < table.getColumnCount(); i++) {
            Rectangle rec = tableitem.getBounds(i);
            if (rec.contains(point)) {
                column = i;
            }
        }
        return column;
    }
}
