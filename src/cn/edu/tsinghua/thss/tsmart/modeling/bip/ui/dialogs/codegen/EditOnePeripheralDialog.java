package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.InitializerDefinition;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.InitializerDefinition.DeviceInitializer;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.InitializerDefinition.InitializeItem;
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
	private static final String INITIALIZER_DEFINITION_FILENAME = "./docs/CodeGen/device-initializer.xml"; //$NON-NLS-1$
	private static InitializerDefinition initializer;
	
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
        super(parentShell, Messages.EditOnePeripheralDialog_0);
        setTitle(Messages.EditOnePeripheralDialog_1);
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
        label.setText(Messages.EditOnePeripheralDialog_2);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(40, 64, 53, 17);
        lblNewLabel.setText(Messages.EditOnePeripheralDialog_3);

        name = new Text(container, SWT.BORDER);
        name.setBounds(99, 22, 183, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 473, 655, 23);

        type = new Combo(container, SWT.READ_ONLY);
        type.setTouchEnabled(true);
        type.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (type.getText().equals("PushButton") && dev instanceof PushButton) //$NON-NLS-1$
                    return; //$NON-NLS-1$

                if (type.getText().equals("FixedSpeedMotor") && dev instanceof FixedSpeedMotor) //$NON-NLS-1$
                    return;
                if (type.getText().equals("VariantSpeedMotor") && dev instanceof VariantSpeedMotor) //$NON-NLS-1$
                    return;

                newDeviceBasedOnType();
                // removeAllText(tableVariables);
                // removeAllText(tableIOBinding);
                refleshValues();
            }
        });
        type.setItems(new String[] {"PushButton", "FixedSpeedMotor", "VariantSpeedMotor"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        type.setBounds(99, 61, 184, 25);
        type.select(0);
        type.setText("PushButton\r\nFixedSpeedMotor\r\nVariantSpeedMotor"); //$NON-NLS-1$

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setBounds(21, 323, 72, 17);
        lblNewLabel_1.setText(Messages.EditOnePeripheralDialog_11);

        tableVariables = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableVariables.setBounds(99, 135, 566, 182);
        tableVariables.setHeaderVisible(true);
        tableVariables.setLinesVisible(true);

        TableColumn tableColumn = new TableColumn(tableVariables, SWT.NONE);
        tableColumn.setWidth(125);
        tableColumn.setText(Messages.EditOnePeripheralDialog_12);


        TableColumn tableColumn_1 = new TableColumn(tableVariables, SWT.NONE);
        tableColumn_1.setWidth(100);
        tableColumn_1.setText(Messages.EditOnePeripheralDialog_13);

        tableIOBinding = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        tableIOBinding.setBounds(99, 323, 566, 144);
        tableIOBinding.setHeaderVisible(true);
        tableIOBinding.setLinesVisible(true);

        TableColumn tableColumn_2 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_2.setWidth(136);
        tableColumn_2.setText(Messages.EditOnePeripheralDialog_14);

        TableColumn tableColumn_6 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_6.setWidth(100);
        tableColumn_6.setText(Messages.EditOnePeripheralDialog_15);

        TableColumn tableColumn_3 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_3.setWidth(100);
        tableColumn_3.setText(Messages.EditOnePeripheralDialog_16);

        TableColumn tableColumn_4 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_4.setWidth(100);
        tableColumn_4.setText(Messages.EditOnePeripheralDialog_17);

        TableColumn tableColumn_5 = new TableColumn(tableIOBinding, SWT.NONE);
        tableColumn_5.setWidth(100);
        tableColumn_5.setText(Messages.EditOnePeripheralDialog_18);

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setBounds(32, 135, 61, 17);
        lblNewLabel_2.setText(Messages.EditOnePeripheralDialog_19);

        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setBounds(69, 103, 24, 17);
        lblNewLabel_3.setText(Messages.EditOnePeripheralDialog_20);

        description = new Text(container, SWT.BORDER);
        description.setBounds(99, 97, 566, 23);
        
        Button button = new Button(container, SWT.NONE);
        button.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		ComponentSelectionDialog csd = new ComponentSelectionDialog(getShell());
        		csd.setBlockOnOpen(true);
        		// open a component select dialog
        		if (OK == csd.open()) {
        			ComponentModel com = csd.getComponent();
            		// update the initializer
        			if (com != null)
        				guessInitializer(type.getText(), com);
        		}
        	}
        });
        button.setBounds(32, 158, 61, 27);
        button.setText(Messages.EditOnePeripheralDialog_6);

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
     * Run in the UI's thread
     * @param devType
     * @param com
     */
    protected void guessInitializer(String devType, ComponentModel com) {
    	if (ensureInitializerDefinition()) {
        	DeviceInitializer devInitializer = null;
        	for (DeviceInitializer di : initializer.initializers) {
        		if (di.devType.equals(devType)) {
            		devInitializer = di;
            		break;
        		}
        	}
        	if (devInitializer == null) {
        		handleError(Messages.EditOnePeripheralDialog_7);
        	} else {
            	Map<String, String> map = new HashMap<String, String>();
            	if (com.getType() instanceof AtomicTypeModel) {
            		AtomicTypeModel atm = (AtomicTypeModel) com.getType();
            		for (IInstance child : atm.getChildren()) {
            			if (child instanceof DataModel) {
            				DataModel dm = (DataModel) child;
            				map.put(dm.getName(), dm.getValue());
            			}
            		}
            	}
            	final List<String[]> newDatas = new ArrayList<String[]>();
            	for (InitializeItem ii : devInitializer.items) {
            		String var = ii.devProperty;
            		String value = null;
            		if (map.containsKey(ii.modelVar)) {
            			value = map.get(ii.modelVar);
            		} else if (ii.defaultValue != null) {
            			value = ii.defaultValue;
            		} else {
            			value = "XXX"; //$NON-NLS-1$
            		}
            		newDatas.add(new String[] {var, value});
            	}
            	Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
		            	tableVariables.setRedraw(false);
		            	tableVariables.removeAll();
		            	for (String[] data : newDatas) {
			        		TableItem item = new TableItem(tableVariables, SWT.None);
			        		item.setText(data);
		            	}
		            	tableVariables.setRedraw(true);
					}
            	});
        	}
    	}
	}

	private boolean ensureInitializerDefinition() {
		if (initializer == null) {
			Serializer serializer = new Persister();
			try {
				InitializerDefinition id = new InitializerDefinition();
				serializer.read(id, new File(INITIALIZER_DEFINITION_FILENAME));
				initializer = id;
			} catch (Exception e) {
				e.printStackTrace();
				handleError(Messages.EditOnePeripheralDialog_4 + INITIALIZER_DEFINITION_FILENAME + "。"); //$NON-NLS-2$
				return false;
			}
		}
		return true;
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
        button.setText(Messages.EditOnePeripheralDialog_21);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditOnePeripheralDialog_22);
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

        if (t.equals("PushButton")) { //$NON-NLS-1$
            dev = new PushButton();
        } else if (t.equals("FixedSpeedMotor")) { //$NON-NLS-1$
            dev = new FixedSpeedMotor();
        } else if (t.equals("VariantSpeedMotor")) { //$NON-NLS-1$
            dev = new VariantSpeedMotor();
        }

        dev.initValues();
    }

    @Override
    protected void updateValues() {
        dev.name = name.getText();
        dev.description = description.getText();

        for (TableItem item : tableIOBinding.getItems()) {
            dev.linking.searchLink(item.getText(0)).addr = Integer.parseInt(item.getText(2));
            dev.linking.searchLink(item.getText(0)).length = Integer.parseInt(item.getText(3));
            dev.linking.searchLink(item.getText(0)).bit = Integer.parseInt(item.getText(4));
        }
        
        dev.initializer.properties.clear();
        
        for (TableItem item : tableVariables.getItems()) {
            dev.initializer.properties.add(new PropertyEntry(item.getText(0), item.getText(1)));
        }

    }

    protected void refleshValues() {
        name.setText(""); //$NON-NLS-1$
        description.setText(""); //$NON-NLS-1$
        tableIOBinding.removeAll();
        tableVariables.removeAll();

        if (dev != null) {
            name.setText(dev.name);

            if (dev instanceof Light) {
                type.setText("Light"); //$NON-NLS-1$
            } else if (dev instanceof FixedSpeedMotor) {
                type.setText("FixedSpeedMotor"); //$NON-NLS-1$
            } else if (dev instanceof PushButton) {
                type.setText("PushButton"); //$NON-NLS-1$
            } else if (dev instanceof VariantSpeedMotor) {
                type.setText("VariantSpeedMotor"); //$NON-NLS-1$
            }

            for (LinkBind l : dev.linking.links) {
                TableItem tableItem = new TableItem(tableIOBinding, SWT.NONE);

                tableItem.setText(new String[] {l.var, l.type, String.format("%d", l.addr), //$NON-NLS-1$
                                String.format("%d", l.length), String.format("%d", l.bit)}); //$NON-NLS-1$ //$NON-NLS-2$

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
        if (name.getText().equals("")) { //$NON-NLS-1$
            handleError(Messages.EditOnePeripheralDialog_36);
            return false;
        }

        if (names != null && names.contains(name.getText())) {
            handleError(Messages.EditOnePeripheralDialog_37);
            return false;
        }

        // tableVariables 初始化变量值是否为数字
        {
            ArrayList<String> values = getTableColumnDatas(tableVariables, 1);
            if (!isAllInteger(values)) {
                handleError(Messages.EditOnePeripheralDialog_38);
                return false;
            }
        }
        // TableIo 2）地址范围 0-100 3)长度范围 0-4 4）位范围 0-7 5）
        {
            ArrayList<String> values = null;
            values = getTableColumnDatas(tableIOBinding, 2);
            if (!isInRange(values, 0, 101)) {
                handleError(Messages.EditOnePeripheralDialog_39);
                return false;
            }
            values = getTableColumnDatas(tableIOBinding, 3);
            if (!isInRange(values, 0, 5)) {
                handleError(Messages.EditOnePeripheralDialog_40);
                return false;
            }
            values = getTableColumnDatas(tableIOBinding, 4);
            if (!isInRange(values, 0, 8)) {
                handleError(Messages.EditOnePeripheralDialog_41);
                return false;
            }
        }
        // TableIo 长度和位不能同时非0
        {
            ArrayList<String> valuesl = null;
            ArrayList<String> valuesb = null;

            valuesl = getTableColumnDatas(tableIOBinding, 3);
            valuesb = getTableColumnDatas(tableIOBinding, 4);
            for (int i = 0; i < valuesl.size(); i++) {
                if (Integer.parseInt(valuesl.get(i)) != 0 && Integer.parseInt(valuesb.get(i)) != 0) {
                    handleError(Messages.EditOnePeripheralDialog_42);
                    return false;
                }
            }
        }


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

    private ArrayList<String> getTableColumnDatas(Table table, int column) {
        ArrayList<String> datas = new ArrayList<String>();
        TableItem[] items = table.getItems();
        int columns = table.getColumnCount();
        if (column > columns || column < 0) return null;
        for (TableItem item : items) {
            String txt = item.getText(column);
            datas.add(txt);
        }
        return datas;
    }

    /**
     * 检查是否全部为数值
     * 
     * @param values
     * @return
     */
    private boolean isAllInteger(ArrayList<String> values) {
        for (String value : values) {
            try {
                Integer.parseInt(value);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * [minValue, maxValue)
     * @param values
     * @param minValue
     * @param maxValue
     * @return
     */
    private boolean isInRange(ArrayList<String> values, int minValue, int maxValue) {
        boolean result = isAllInteger(values);
        if (result) {
            for (String value : values) {
                int v = Integer.parseInt(value);
                if (v >= minValue && v < maxValue) {
                    continue;
                } else
                    return false;
            }
        }
        return result;
    }
}
