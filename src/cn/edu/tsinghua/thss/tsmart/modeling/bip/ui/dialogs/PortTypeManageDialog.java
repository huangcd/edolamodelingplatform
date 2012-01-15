package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class PortTypeManageDialog extends AbstractEditDialog {
    private Text                    textArgs;
    private Label                   labelError;
    private Button                  buttonAdd;
    private Button                  buttonDelete;
    private List                    listPortTypes;
    private Label                   labelPortArgs;
    private Label                   labelName;
    private Text                    textName;
    private HashMap<String, String> portMap;

    public PortTypeManageDialog(Shell parentShell) {
        super(parentShell, Messages.PortTypeManageDialog_0);
        portMap = new HashMap<String, String>();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(10, 10, 208, 195);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listPortTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listPortTypes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (listPortTypes.getSelection().length > 0) {
                    getErrorLabel().setForeground(ColorConstants.black);
                    getErrorLabel().setText(portMap.get(listPortTypes.getSelection()[0]));
                }
            }
        });
        scrolledComposite.setContent(listPortTypes);
        scrolledComposite.setMinSize(listPortTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        buttonDelete = new Button(container, SWT.NONE);
        buttonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeTypes();
            }
        });
        buttonDelete.setBounds(265, 10, 86, 27);
        buttonDelete.setText(Messages.PortTypeManageDialog_1);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 212, 379, 17);

        Composite composite = new Composite(container, SWT.BORDER);
        composite.setBounds(224, 43, 165, 162);

        textArgs = new Text(composite, SWT.BORDER);
        textArgs.setLocation(10, 92);
        textArgs.setSize(141, 23);

        buttonAdd = new Button(composite, SWT.NONE);
        buttonAdd.setLocation(38, 121);
        buttonAdd.setSize(86, 27);
        buttonAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addType();
            }
        });
        buttonAdd.setText(Messages.PortTypeManageDialog_2);

        labelPortArgs = new Label(composite, SWT.NONE);
        labelPortArgs.setBounds(10, 71, 141, 17);
        labelPortArgs.setText(Messages.PortTypeManageDialog_3);

        labelName = new Label(composite, SWT.NONE);
        labelName.setBounds(10, 10, 141, 17);
        labelName.setText(Messages.PortTypeManageDialog_4);

        textName = new Text(composite, SWT.BORDER);
        textName.setBounds(10, 33, 141, 23);
        textArgs.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.keyCode == SWT.CR) {
                    addType();
                }
            }
        });

        initValues();
        return container;
    }

    private void removeTypes() {
        String[] selections = listPortTypes.getSelection();
        boolean containsDefaultType = false;
        for (String selection : selections) {
            if (selection.equals("ePort")) { //$NON-NLS-1$
                containsDefaultType = true;
                continue;
            }
            if (PortTypeModel.isRemovable(selection)) {
                listPortTypes.remove(selection);
                PortTypeModel.removeType(selection);
            } else
                MessageUtil.ShowDeletePortTypeErrorDialog(selection);
        }
        if (containsDefaultType) {
            handleError(Messages.PortTypeManageDialog_6);
        }
    }

    private void addType() {
        String newType = textName.getText().trim();
        if (!isIdentifier(newType)) {
            handleError(MessageFormat.format(Messages.PortTypeManageDialog_7, newType));
            return;
        }
        if (newType.isEmpty()) {
            handleError(Messages.PortTypeManageDialog_8);
            return;
        }
        String[] items = listPortTypes.getItems();
        for (String type : items) {
            if (type.equals(newType)) {
                handleError(Messages.PortTypeManageDialog_9);
                return;
            }
        }
        String args = textArgs.getText().trim();
        HashSet<String> names = new HashSet<String>();
        if (!args.endsWith(",")) { //$NON-NLS-1$
            Scanner scan = new Scanner(args);
            scan.useDelimiter(","); //$NON-NLS-1$
            ArrayList<String> arguments = new ArrayList<String>();
            while (scan.hasNext()) {
                String argument = scan.next().trim();
                String[] temp = argument.split("\\s+"); //$NON-NLS-1$
                if (temp.length != 2) {
                    handleError(Messages.PortTypeManageDialog_13);
                    return;
                }
                String type = temp[0].trim();
                String name = temp[1].trim();
                if (names.contains(name)) {
                    handleError(Messages.PortTypeManageDialog_14 + name);
                    return;
                }
                names.add(name);
                if (!DataTypeModel.getTypes().contains(type)) {
                    handleError(MessageFormat.format(Messages.PortTypeManageDialog_15, type));
                    return;
                }
                arguments.add(argument);
            }
        }
        portMap.put(newType, args);
        listPortTypes.add(newType);
        textArgs.setText(""); //$NON-NLS-1$
        textName.setText(""); //$NON-NLS-1$
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(405, 322);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void updateValues() {
        // FIXME 删除再增加一个同名的port会有bug，应该避免
        HashSet<String> types = new HashSet<String>(Arrays.asList(listPortTypes.getItems()));
        HashSet<String> temp = new HashSet<String>();
        for (String type : PortTypeModel.getTypes()) {
            if (!types.contains(type)) {
                temp.add(type);
            }
        }
        for (String type : temp) {
            PortTypeModel.removeType(type);
        }
        for (String type : types) {
            if (!PortTypeModel.getTypes().contains(type)) {
                PortTypeModel model = PortTypeModel.parsePort(type, portMap.get(type));
                GlobalProperties.getInstance().getTopModel().addChild(model);
            }
        }
    }

    @Override
    protected void initValues() {
        if (GlobalProperties.getInstance().getTopModel() instanceof ProjectModel) {
            buttonDelete.setEnabled(false);
        }
        for (String type : PortTypeModel.getTypeNamesAsArray()) {
            listPortTypes.add(type);
            portMap.put(type, PortTypeModel.getModelByName(type).getArgumentAsString());
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
