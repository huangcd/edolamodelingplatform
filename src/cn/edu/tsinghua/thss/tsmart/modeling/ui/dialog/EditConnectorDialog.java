package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.util.MessageBoxUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EditConnectorDialog extends AbstractEditDialog {
    private Text                       nameText;
    private Button                     isExportButton;
    private ConnectorTypeModel         owner;
    private CompoundTypeModel          _parent;
    private Table                      actionTable;
    private Text                       defineExpressionText;
    private Table                      dataTable;
    private HashMap<TableItem, Button> dataExportButtonMap = new HashMap<TableItem, Button>();

    public EditConnectorDialog(Shell parentShell, ConnectorTypeModel owner,
                    CompoundTypeModel _parent) {
        super(parentShell, "Edit Connector Properties");
        this.owner = owner;
        this._parent = _parent;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 2;

        Label lblGuard = new Label(container, SWT.NONE);
        lblGuard.setAlignment(SWT.RIGHT);
        lblGuard.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblGuard.setText("name:");

        nameText = new Text(container, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Label(container, SWT.NONE);

        isExportButton = new Button(container, SWT.CHECK);
        isExportButton.setText("export");

        Label lblDefine = new Label(container, SWT.NONE);
        lblDefine.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDefine.setText("define:");

        defineExpressionText = new Text(container, SWT.BORDER);
        defineExpressionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblDatas = new Label(container, SWT.NONE);
        lblDatas.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDatas.setAlignment(SWT.RIGHT);
        lblDatas.setText("datas:");

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_scrolledComposite = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_scrolledComposite.heightHint = 132;
        gd_scrolledComposite.widthHint = 587;
        scrolledComposite.setLayoutData(gd_scrolledComposite);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        dataTable = new Table(scrolledComposite, SWT.BORDER | SWT.FULL_SELECTION);
        dataTable.setHeaderVisible(true);
        dataTable.setLinesVisible(true);

        TableColumn tblclmnExport = new TableColumn(dataTable, SWT.NONE);
        tblclmnExport.setWidth(100);
        tblclmnExport.setText("export");

        TableColumn tblclmnType = new TableColumn(dataTable, SWT.NONE);
        tblclmnType.setWidth(160);
        tblclmnType.setText("type");

        TableColumn tblclmnName = new TableColumn(dataTable, SWT.NONE);
        tblclmnName.setWidth(152);
        tblclmnName.setText("name");

        TableColumn tblclmnValue = new TableColumn(dataTable, SWT.NONE);
        tblclmnValue.setWidth(138);
        tblclmnValue.setText("value");
        scrolledComposite.setContent(dataTable);
        scrolledComposite.setMinSize(dataTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        Label lblActions = new Label(container, SWT.NONE);
        lblActions.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblActions.setText("actions:");

        ScrolledComposite scrolledComposite_1 =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        GridData gd_scrolledComposite_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_scrolledComposite_1.heightHint = 223;
        gd_scrolledComposite_1.widthHint = 748;
        scrolledComposite_1.setLayoutData(gd_scrolledComposite_1);
        scrolledComposite_1.setExpandHorizontal(true);
        scrolledComposite_1.setExpandVertical(true);

        actionTable = new Table(scrolledComposite_1, SWT.BORDER | SWT.FULL_SELECTION);
        actionTable.setHeaderVisible(true);
        actionTable.setLinesVisible(true);

        TableColumn tblclmnInteraction = new TableColumn(actionTable, SWT.NONE);
        tblclmnInteraction.setToolTipText("interaction ports");
        tblclmnInteraction.setText("interaction");
        tblclmnInteraction.setWidth(134);

        TableColumn tblclmnGuard = new TableColumn(actionTable, SWT.NONE);
        tblclmnGuard.setWidth(100);
        tblclmnGuard.setText("guard");

        TableColumn tblclmnUpaction = new TableColumn(actionTable, SWT.NONE);
        tblclmnUpaction.setWidth(257);
        tblclmnUpaction.setText("upAction");

        TableColumn tblclmnDownaction = new TableColumn(actionTable, SWT.NONE);
        tblclmnDownaction.setWidth(253);
        tblclmnDownaction.setText("downAction");
        scrolledComposite_1.setContent(actionTable);
        scrolledComposite_1.setMinSize(actionTable.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        container.setTabList(new Control[] {nameText, isExportButton});

        initValues();
        return container;
    }

    private void initDataTable() {
        for (DataModel data : owner.getDatas()) {
            TableItem item = new TableItem(dataTable, SWT.NONE);
            item.setText(1, data.getType());
            item.setText(2, data.getName());
            item.setText(3, data.getValue());
            TableEditor editor = new TableEditor(dataTable);
            Button button = new Button(dataTable, SWT.CHECK);
            if (owner.getAssociateDatas().contains(data)) {
                button.setSelection(true);
            }
            button.pack();
            editor.minimumWidth = button.getSize().x;
            editor.horizontalAlignment = SWT.CENTER;
            editor.setEditor(button, item, 0);
            dataExportButtonMap.put(item, button);
        }
        final TableEditor tableEditor = new TableEditor(dataTable);
        tableEditor.horizontalAlignment = SWT.LEFT;
        tableEditor.grabHorizontal = true;
        dataTable.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
                Rectangle clientArea = dataTable.getClientArea();
                Point eventLocation = new Point(event.x, event.y);
                int index = dataTable.getTopIndex();
                while (index < dataTable.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = dataTable.getItem(index);
                    for (int i = 1; i < dataTable.getColumnCount(); i++) {
                        Rectangle itemArea = item.getBounds(i);
                        if (itemArea.contains(eventLocation)) {
                            EditAsText(item, i, tableEditor, dataTable);
                            return;
                        }
                        if (!visible && itemArea.intersects(clientArea)) {
                            visible = true;
                        }
                    }
                    if (!visible) return;
                    index++;
                }
                if (index >= dataTable.getItemCount()) {
                    addDataTableItem();
                }
            }
        });
    }

    private TableItem addDataTableItem() {
        TableEditor editor = new TableEditor(dataTable);
        TableItem item = new TableItem(dataTable, SWT.NONE);
        Button button = new Button(dataTable, SWT.CHECK);
        button.pack();
        editor.minimumWidth = button.getSize().x;
        editor.horizontalAlignment = SWT.CENTER;
        editor.setEditor(button, item, 0);
        dataExportButtonMap.put(item, button);
        return item;
    }

    private void EditAsText(TableItem item, int i, TableEditor tableEditor, Table table) {
        Text text = new Text(table, SWT.NONE);
        Listener textListener = createTextListener(item, i, text);
        text.addListener(SWT.FocusOut, textListener);
        text.addListener(SWT.Traverse, textListener);
        tableEditor.setEditor(text, item, i);
        text.setText(item.getText(i));
        text.selectAll();
        text.setFocus();
    }

    private Listener createTextListener(final TableItem item, final int column, final Text text) {
        return new Listener() {
            public void handleEvent(final Event e) {
                switch (e.type) {
                    case SWT.FocusOut:
                        item.setText(column, text.getText());
                        text.dispose();
                        break;
                    case SWT.Traverse:
                        switch (e.detail) {
                            case SWT.TRAVERSE_RETURN:
                                item.setText(column, text.getText());
                                // FALL THROUGH
                            case SWT.TRAVERSE_ESCAPE:
                                text.dispose();
                                e.doit = false;
                        }
                        break;
                }
            }
        };
    }

    protected void initValues() {
        nameText.setText(owner.getName() == null ? "" : owner.getName());
        isExportButton.setSelection(owner.isExport());
        defineExpressionText.setText(owner.getDefineExpression() == null ? "" : owner
                        .getDefineExpression());
        initDataTable();
        initActionTable();
    }

    private void initActionTable() {
        for (InteractionModel interaction : owner.getInteractions()) {
            TableItem item = new TableItem(actionTable, SWT.NONE);
            item.setText(0, interaction.getInteraction());
            item.setText(1, interaction.getGuard());
            item.setText(2, interaction.getUpAction());
            item.setText(3, interaction.getDownAction());
        }
        final TableEditor tableEditor = new TableEditor(actionTable);
        tableEditor.horizontalAlignment = SWT.LEFT;
        tableEditor.grabHorizontal = true;
        actionTable.addListener(SWT.MouseDown, new Listener() {
            public void handleEvent(Event event) {
                Point eventLocation = new Point(event.x, event.y);
                int index = actionTable.getTopIndex();
                while (index < actionTable.getItemCount()) {
                    final TableItem item = actionTable.getItem(index);
                    if (item.getBounds(0).contains(eventLocation)
                                    || item.getBounds(1).contains(eventLocation)
                                    || item.getBounds(2).contains(eventLocation)
                                    || item.getBounds(3).contains(eventLocation)) {
                        String interaction = item.getText(0);
                        String guard = item.getText(1);
                        String upAction = item.getText(2);
                        String downAction = item.getText(3);
                        EditInteractionDialog dialog =
                                        new EditInteractionDialog(getShell(), interaction, guard,
                                                        upAction, downAction);
                        dialog.setBlockOnOpen(true);
                        int result = dialog.open();
                        if (result == Window.OK) {
                            item.setText(0, dialog.getInteraction());
                            item.setText(1, dialog.getGuard());
                            item.setText(2, dialog.getUpAction());
                            item.setText(3, dialog.getDownAction());
                        }
                    }
                    index++;
                }
                if (index >= actionTable.getItemCount()) {
                    new TableItem(actionTable, SWT.NONE);
                }
            }
        });
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected boolean validateUserInput() {
        if (validateName(nameText.getText().trim())) {
            return false;
        }
        if (!datasValidate()) {
            return false;
        }
        return true;
    }

    private boolean isEmptyOrNull(String str) {
        return str == null || str.isEmpty();
    }

    private boolean datasValidate() {
        HashSet<String> dataNames = new HashSet<String>();
        for (TableItem item : dataTable.getItems()) {
            String type = getDataType(item);
            String name = getDataName(item);
            String value = getDataValue(item);
            if ((!isEmptyOrNull(type) && isEmptyOrNull(name))
                            || (isEmptyOrNull(type) && !isEmptyOrNull(name))
                            || (isEmptyOrNull(type) && isEmptyOrNull(name) && !isEmptyOrNull(value))) {
                MessageBoxUtil.ShowErrorMessage(this.getShell(), "Data declaration error",
                                "Both name and type should be set");
                return false;
            }
            if (dataNames.contains(name)) {
                MessageBoxUtil.ShowErrorMessage(this.getShell(), "Duplicate data name error",
                                "There are more than one data named: " + name);
                return false;
            }
            if (!isEmptyOrNull(name)) {
                dataNames.add(name);
            }
        }
        return true;
    }

    private String getDataValue(TableItem item) {
        return item.getText(3).trim();
    }

    private String getDataName(TableItem item) {
        return item.getText(2).trim();
    }

    private String getDataType(TableItem item) {
        return item.getText(1).trim();
    }

    private boolean validateName(String name) {
        if (isEmptyOrNull(name)) {
            MessageBoxUtil.ShowErrorMessage(this.getShell(), "Empty name error",
                            "Connector's name must be set");
            return true;
        }
        for (BaseModel model : _parent.getChildren()) {
            if (model instanceof ConnectorTypeModel) {
                ConnectorTypeModel child = (ConnectorTypeModel) model;
                if (child.equals(this.owner)) {
                    continue;
                }
                if (child.getName().equals(name)) {
                    MessageBoxUtil.ShowErrorMessage(this.getShell(), "Name conflict error",
                                    "Connector name exists in this compound");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(847, 594);
    }

    @Override
    protected void okPressed() {
        if (!validateUserInput()) {
            return;
        }
        owner.setName(nameText.getText().trim());
        owner.setExport(isExportButton.getSelection());
        owner.setDefineExpression(defineExpressionText.getText());
        setDatas();
        setInteraction();
        super.okPressed();
    }

    private void setInteraction() {
        owner.clearInteractions();
        for (TableItem item : actionTable.getItems()) {
            String interaction = item.getText(0).trim();
            String guard = item.getText(1).trim();
            String upAction = item.getText(2).trim();
            String downAction = item.getText(3).trim();
            if (!isEmptyOrNull(interaction)) {
                InteractionModel interactionModel = new InteractionModel();
                interactionModel.setInteraction(interaction);
                interactionModel.setGuard(guard);
                interactionModel.setUpAction(upAction);
                interactionModel.setDownAction(downAction);
                owner.addInteraction(interactionModel);
            }
        }
    }

    private void setDatas() {
        owner.clearDatas();
        List<DataModel> associateDatas = new ArrayList<DataModel>();
        for (TableItem item : dataTable.getItems()) {
            String type = getDataType(item);
            String name = getDataName(item);
            String value = getDataValue(item);
            DataModel data = new DataModel();
            if (isEmptyOrNull(type)) {
                continue;
            }
            data.setType(type);
            data.setName(name);
            if (!isEmptyOrNull(value)) {
                data.setValue(value);
            }
            owner.addData(data);
            Button checkButton = dataExportButtonMap.get(item);
            if (checkButton == null || !checkButton.getSelection()) {
                continue;
            }
            associateDatas.add(data);
        }
        owner.setAssociateDatas(associateDatas);
    }
}
