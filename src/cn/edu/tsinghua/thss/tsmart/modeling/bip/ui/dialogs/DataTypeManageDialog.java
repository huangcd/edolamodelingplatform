package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class DataTypeManageDialog extends AbstractEditDialog {
    private Text   textType;
    private Label  labelError;
    private Button buttonAdd;
    private Button buttonDelete;
    private List   listDataTypes;

    public DataTypeManageDialog(Shell parentShell) {
        super(parentShell, Messages.DataTypeManageDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(10, 10, 122, 195);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listDataTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        scrolledComposite.setContent(listDataTypes);
        scrolledComposite.setMinSize(listDataTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        buttonDelete = new Button(container, SWT.NONE);
        buttonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeTypes();
            }
        });
        buttonDelete.setBounds(152, 10, 86, 27);
        buttonDelete.setText(Messages.DataTypeManageDialog_1);

        textType = new Text(container, SWT.BORDER);
        textType.setBounds(152, 149, 86, 23);

        buttonAdd = new Button(container, SWT.NONE);
        buttonAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addType();
            }
        });
        buttonAdd.setText(Messages.DataTypeManageDialog_2);
        buttonAdd.setBounds(152, 178, 86, 27);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 212, 228, 17);

        initValues();
        return container;
    }

    @SuppressWarnings("unchecked")
    private void removeTypes() {
        String[] selections = listDataTypes.getSelection();
        boolean containsDefaultType = false;
        for (String selection : selections) {
            if (selection.equals("int") || selection.equals("bool")) { //$NON-NLS-1$ //$NON-NLS-2$
                containsDefaultType = true;
                continue;
            }
            if (DataTypeModel.isRemovable(selection)) {
                properties.getTopModel().removeChildByName(selection);
                listDataTypes.remove(selection);
            } else {
                MessageUtil.ShowDeleteDataTypeErrorDialog(selection);
            }
        }
        if (containsDefaultType) {
            handleError(Messages.DataTypeManageDialog_5);
        }
    }

    private void addType() {
        String newType = textType.getText().trim().toLowerCase();
        if (!isIdentifier(newType)) {
            handleError(MessageFormat.format(Messages.DataTypeManageDialog_6, newType));
            return;
        }
        if (newType.isEmpty()) {
            handleError(Messages.DataTypeManageDialog_7);
            return;
        }
        String[] items = listDataTypes.getItems();
        for (String type : items) {
            if (type.equals(newType)) {
                handleError(Messages.DataTypeManageDialog_8);
                return;
            }
        }
        listDataTypes.add(newType);
        textType.setText(""); //$NON-NLS-1$
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(259, 322);
    }

    @Override
    protected void updateValues() {
        HashSet<String> types = new HashSet<String>(Arrays.asList(listDataTypes.getItems()));
        HashSet<String> temp = new HashSet<String>();
        for (String type : DataTypeModel.getTypes()) {
            if (!types.contains(type)) {
                temp.add(type);
            }
        }
        for (String type : temp) {
            properties.getTopModel().removeChildByName(type);
        }
        for (String type : types) {
            if (!DataTypeModel.getTypes().contains(type)) {
                properties.getTopModel().addDataType(type);
            }
        }
    }

    @Override
    protected void initValues() {
        if (GlobalProperties.getInstance().getTopModel() instanceof ProjectModel) {
            buttonDelete.setEnabled(false);
        }
        for (String type : DataTypeModel.getTypeNamesAsArray()) {
            listDataTypes.add(type);
        }
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return labelError;
    }
}
