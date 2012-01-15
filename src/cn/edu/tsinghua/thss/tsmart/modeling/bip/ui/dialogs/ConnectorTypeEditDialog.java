package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.text.MessageFormat;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InteractionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ConnectorTypeEditDialog extends AbstractEditDialog {
    private ConnectorTypeModel connector;
    private Label              labelError;
    private Text               textNewArgumentName;
    private Text               textNewDataName;
    private StyledText         styledTextInteractionPreview;
    private List               listInteractions;
    private List               listPortArguments;
    private Button             buttonDeleteArgument;
    private Button             buttonMoveArgumentUp;
    private Button             buttonMoveArgumentDown;
    private Combo              comboArguments;
    private Button             buttonAddArgument;
    private List               listDatas;
    private Button             buttonDeleteData;
    private Combo              comboDatas;
    private Button             buttonAddData;
    private Button             buttonMoveInteractionUp;
    private Button             buttonCreateInteraction;
    private Button             buttonMoveInteractionDown;
    private Button             buttonDeleteInteraction;
    private Text               textConnectorName;
    private Combo              comboExportPortType;
    private Text               textInteractor;
    private Button             buttonExport;
    private Button             buttonBoundData;
    private Button             buttonEditInteraction;
    private Text               textBoundArguments;

    /**
     * @wbp.parser.constructor
     */
    public ConnectorTypeEditDialog(Shell parentShell) {
        super(parentShell, Messages.ConnectorTypeEditDialog_0);
        connector = new ConnectorTypeModel();
    }

    public ConnectorTypeEditDialog(Shell parentShell, ConnectorTypeModel connector) {
        super(parentShell, Messages.ConnectorTypeEditDialog_1);
        this.connector = connector;
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

        Label lblNewLabel_1 = new Label(container, SWT.RIGHT);
        lblNewLabel_1.setBounds(10, 23, 87, 17);
        lblNewLabel_1.setText(Messages.ConnectorTypeEditDialog_2);

        textConnectorName = new Text(container, SWT.BORDER);
        textConnectorName.setBounds(103, 20, 395, 23);

        Label label_4 = new Label(container, SWT.RIGHT);
        label_4.setText(Messages.ConnectorTypeEditDialog_3);
        label_4.setBounds(10, 357, 87, 17);

        textInteractor = new Text(container, SWT.BORDER);
        textInteractor.setBounds(103, 354, 395, 23);

        Group groupArgument = new Group(container, SWT.SHADOW_ETCHED_IN);
        groupArgument.setText(Messages.ConnectorTypeEditDialog_4);
        groupArgument.setBounds(10, 64, 237, 270);
        groupArgument.setLayout(null);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(groupArgument, SWT.BORDER | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        scrolledComposite.setBounds(10, 23, 103, 236);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listPortArguments = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listPortArguments.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previewPortArgument();
            }
        });
        listPortArguments.setLocation(0, -129);
        scrolledComposite.setContent(listPortArguments);
        scrolledComposite.setMinSize(listPortArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        Group group = new Group(groupArgument, SWT.NONE);
        group.setBounds(119, 23, 106, 168);

        Label label = new Label(group, SWT.NONE);
        label.setBounds(10, 19, 61, 17);
        label.setText(Messages.ConnectorTypeEditDialog_5);

        comboArguments = new Combo(group, SWT.READ_ONLY);
        comboArguments.setBounds(10, 42, 86, 25);

        Label lblNewLabel = new Label(group, SWT.NONE);
        lblNewLabel.setBounds(10, 73, 61, 17);
        lblNewLabel.setText(Messages.ConnectorTypeEditDialog_6);

        textNewArgumentName = new Text(group, SWT.BORDER);
        textNewArgumentName.setBounds(10, 97, 86, 23);

        buttonAddArgument = new Button(group, SWT.NONE);
        buttonAddArgument.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addArgument();
            }
        });
        buttonAddArgument.setBounds(10, 132, 86, 27);
        buttonAddArgument.setText(Messages.ConnectorTypeEditDialog_7);

        buttonDeleteArgument = new Button(groupArgument, SWT.NONE);
        buttonDeleteArgument.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeArgument();
            }
        });
        buttonDeleteArgument.setBounds(119, 232, 106, 27);
        buttonDeleteArgument.setText(Messages.ConnectorTypeEditDialog_8);

        buttonMoveArgumentUp = new Button(groupArgument, SWT.NONE);
        buttonMoveArgumentUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveArgumentUp();
            }
        });
        buttonMoveArgumentUp.setBounds(119, 197, 34, 27);
        buttonMoveArgumentUp.setText("\u2191"); //$NON-NLS-1$

        buttonMoveArgumentDown = new Button(groupArgument, SWT.NONE);
        buttonMoveArgumentDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveArgumentDown();
            }
        });
        buttonMoveArgumentDown.setText("\u2193"); //$NON-NLS-1$
        buttonMoveArgumentDown.setBounds(191, 197, 34, 27);

        Group groupData = new Group(container, SWT.SHADOW_ETCHED_IN);
        groupData.setLayout(null);
        groupData.setText(Messages.ConnectorTypeEditDialog_11);
        groupData.setBounds(261, 64, 237, 270);

        ScrolledComposite scrolledComposite_1 =
                        new ScrolledComposite(groupData, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_1.setExpandVertical(true);
        scrolledComposite_1.setExpandHorizontal(true);
        scrolledComposite_1.setBounds(10, 23, 103, 236);

        listDatas = new List(scrolledComposite_1, SWT.BORDER);
        listDatas.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previewData();
            }
        });
        scrolledComposite_1.setContent(listDatas);
        scrolledComposite_1.setMinSize(listDatas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite_1.setMinSize(new Point(71, 68));

        buttonDeleteData = new Button(groupData, SWT.NONE);
        buttonDeleteData.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeData();
            }
        });
        buttonDeleteData.setText(Messages.ConnectorTypeEditDialog_12);
        buttonDeleteData.setBounds(119, 232, 106, 27);

        Group group_2 = new Group(groupData, SWT.NONE);
        group_2.setBounds(119, 23, 106, 170);

        comboDatas = new Combo(group_2, SWT.READ_ONLY);
        comboDatas.setBounds(10, 46, 86, 25);

        Label label_1 = new Label(group_2, SWT.NONE);
        label_1.setText(Messages.ConnectorTypeEditDialog_13);
        label_1.setBounds(10, 23, 61, 17);

        Label label_2 = new Label(group_2, SWT.NONE);
        label_2.setText(Messages.ConnectorTypeEditDialog_14);
        label_2.setBounds(10, 77, 61, 17);

        textNewDataName = new Text(group_2, SWT.BORDER);
        textNewDataName.setBounds(10, 100, 86, 23);

        buttonAddData = new Button(group_2, SWT.NONE);
        buttonAddData.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addData();
            }
        });
        buttonAddData.setText(Messages.ConnectorTypeEditDialog_15);
        buttonAddData.setBounds(10, 129, 86, 27);

        Group groupInteraction = new Group(container, SWT.NONE);
        groupInteraction.setText(Messages.ConnectorTypeEditDialog_16);
        groupInteraction.setBounds(504, 10, 327, 443);

        ScrolledComposite scrolledComposite_2 =
                        new ScrolledComposite(groupInteraction, SWT.BORDER | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        scrolledComposite_2.setBounds(10, 21, 307, 119);
        scrolledComposite_2.setExpandHorizontal(true);
        scrolledComposite_2.setExpandVertical(true);

        listInteractions = new List(scrolledComposite_2, SWT.BORDER);
        listInteractions.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previewInteraction();
            }
        });
        scrolledComposite_2.setContent(listInteractions);
        scrolledComposite_2.setMinSize(listInteractions.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        buttonCreateInteraction = new Button(groupInteraction, SWT.NONE);
        buttonCreateInteraction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addInteraction();
            }
        });
        buttonCreateInteraction.setText(Messages.ConnectorTypeEditDialog_17);
        buttonCreateInteraction.setBounds(9, 150, 70, 27);

        buttonEditInteraction = new Button(groupInteraction, SWT.NONE);
        buttonEditInteraction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                editInteraction();
            }
        });
        buttonEditInteraction.setText(Messages.ConnectorTypeEditDialog_18);
        buttonEditInteraction.setBounds(88, 150, 70, 27);

        buttonMoveInteractionUp = new Button(groupInteraction, SWT.NONE);
        buttonMoveInteractionUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveInteractionUp();
            }
        });
        buttonMoveInteractionUp.setText("\u2191"); //$NON-NLS-1$
        buttonMoveInteractionUp.setBounds(167, 150, 30, 27);

        buttonMoveInteractionDown = new Button(groupInteraction, SWT.NONE);
        buttonMoveInteractionDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                moveInteractionDown();
            }
        });
        buttonMoveInteractionDown.setText("\u2193"); //$NON-NLS-1$
        buttonMoveInteractionDown.setBounds(206, 150, 30, 27);

        buttonDeleteInteraction = new Button(groupInteraction, SWT.NONE);
        buttonDeleteInteraction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeInteraction();
            }
        });
        buttonDeleteInteraction.setText(Messages.ConnectorTypeEditDialog_21);
        buttonDeleteInteraction.setBounds(245, 150, 70, 27);

        styledTextInteractionPreview =
                        new StyledText(groupInteraction, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP
                                        | SWT.H_SCROLL | SWT.V_SCROLL);
        styledTextInteractionPreview.setBounds(10, 183, 307, 249);

        Group groupExport = new Group(container, SWT.NONE);
        groupExport.setText(Messages.ConnectorTypeEditDialog_22);
        groupExport.setBounds(10, 395, 488, 58);

        buttonExport = new Button(groupExport, SWT.CHECK);
        buttonExport.setSelection(true);
        buttonExport.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (buttonExport.getSelection()) {
                    comboExportPortType.setEnabled(true);
                    buttonBoundData.setEnabled(true);
                    textBoundArguments.setEnabled(true);
                } else {
                    comboExportPortType.setEnabled(false);
                    buttonBoundData.setEnabled(false);
                    textBoundArguments.setEnabled(false);
                }
            }
        });
        buttonExport.setBounds(10, 27, 45, 17);
        buttonExport.setText(Messages.ConnectorTypeEditDialog_23);

        Label label_3 = new Label(groupExport, SWT.NONE);
        label_3.setBounds(57, 27, 61, 17);
        label_3.setText(Messages.ConnectorTypeEditDialog_24);

        comboExportPortType = new Combo(groupExport, SWT.READ_ONLY);
        comboExportPortType.setBounds(124, 23, 86, 25);

        buttonBoundData = new Button(groupExport, SWT.NONE);
        buttonBoundData.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                boundData();
            }
        });
        buttonBoundData.setBounds(378, 22, 100, 27);
        buttonBoundData.setText(Messages.ConnectorTypeEditDialog_25);

        textBoundArguments = new Text(groupExport, SWT.BORDER);
        textBoundArguments.setBounds(221, 24, 151, 23);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 459, 821, 17);

        initValues();

        return container;
    }

    protected void previewInteraction() {
        String[] selections = listInteractions.getSelection();
        if (selections == null || selections.length == 0) {
            return;
        }
        String interactionName = selections[0];
        InteractionModel interaction = connector.getInteractionByName(interactionName);
        styledTextInteractionPreview.setText(interaction.exportToBip());
    }

    protected void editInteraction() {
        int index = listInteractions.getSelectionIndex();
        if (index == -1) return;
        String interactionName = listInteractions.getItem(index);
        InteractionModel interaction = connector.getInteractionByName(interactionName);
        InteractionEditDialog dialog =
                        new InteractionEditDialog(getParentShell(), connector, interaction);
        dialog.setBlockOnOpen(true);
        if (OK == dialog.open()) {
            listInteractions.setItem(index, interaction.getInteractionString());
        }
    }

    protected void boundData() {
        String portType = comboExportPortType.getText();
        connector.setPort(PortTypeModel.getModelByName(portType).getInstance());
        PortModel port = connector.getPort();
        int size = port.getPortArguments().size();
        boolean emptyString = textBoundArguments.getText().trim().isEmpty();
        String[] arguments = textBoundArguments.getText().replaceAll("\\s+", "").split(","); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        if ((emptyString && size != 0) || (!emptyString && arguments.length != size)) {
            handleError(Messages.ConnectorTypeEditDialog_29);
            return;
        }
        java.util.List<String> list = ((PortTypeModel) port.getType()).getArgumentTypes();
        for (int i = 0; i < size; i++) {
            DataModel data = connector.getDataByName(arguments[i]);
            if (data == null) {
                handleError(Messages.ConnectorTypeEditDialog_30 + arguments[i] + Messages.ConnectorTypeEditDialog_31);
                return;
            }
            if (!connector.getDataByName(arguments[i]).getType().getName().equals(list.get(i))) {
                handleError(Messages.ConnectorTypeEditDialog_32 + list.get(i) + Messages.ConnectorTypeEditDialog_33);
                return;
            }
        }
        for (int i = 0; i < size; i++) {
            port.bound(i, connector.getDataByName(arguments[i]));
        }
        Label info = getErrorLabel();
        info.setForeground(ColorConstants.black);
        info.setText(Messages.ConnectorTypeEditDialog_34);
    }

    protected void removeInteraction() {
        int index = listInteractions.getSelectionIndex();
        if (index == -1) {
            return;
        }
        listInteractions.remove(index);
        connector.removeInteraction(index);
        handleError(""); //$NON-NLS-1$
    }

    protected void moveInteractionDown() {
        int index = listInteractions.getSelectionIndex();
        if (index == listInteractions.getItemCount() - 1 || index == -1) {
            return;
        }
        String name = listInteractions.getItem(index);
        listInteractions.remove(index);
        listInteractions.add(name, index + 1);
        listInteractions.select(index + 1);
        connector.moveInteractionBackward(index);
    }

    protected void moveInteractionUp() {
        int index = listInteractions.getSelectionIndex();
        if (index == 0 || index == -1) {
            return;
        }
        String name = listInteractions.getItem(index);
        listInteractions.remove(index);
        listInteractions.add(name, index - 1);
        listInteractions.select(index - 1);
        connector.moveInteractionForward(index);
    }

    private void moveArgumentDown() {
        int index = listPortArguments.getSelectionIndex();
        if (index == listPortArguments.getItemCount() - 1 || index == -1) {
            return;
        }
        String name = listPortArguments.getItem(index);
        listPortArguments.remove(index);
        listPortArguments.add(name, index + 1);
        listPortArguments.select(index + 1);
        connector.moveArgumentBackward(index);
    }

    private void moveArgumentUp() {
        int index = listPortArguments.getSelectionIndex();
        if (index == 0 || index == -1) {
            return;
        }
        String name = listPortArguments.getItem(index);
        listPortArguments.remove(index);
        listPortArguments.add(name, index - 1);
        listPortArguments.select(index - 1);
        connector.moveArgumentForward(index);
    }

    protected void addInteraction() {
        InteractionEditDialog dialog = new InteractionEditDialog(getParentShell(), connector, null);
        dialog.setBlockOnOpen(true);
        if (OK == dialog.open()) {
            listInteractions.add(dialog.getInteractionName());
        }
    }

    protected void removeData() {
        String[] selections = listDatas.getSelection();
        if (selections == null || selections.length == 0) {
            return;
        }
        listDatas.remove(selections[0]);
        handleError(""); //$NON-NLS-1$
        connector.removeDataByName(selections[0]);
    }

    protected void removeArgument() {
        String[] selections = listPortArguments.getSelection();
        if (selections == null || selections.length == 0) {
            return;
        }
        listPortArguments.remove(selections[0]);
        handleError(""); //$NON-NLS-1$
        connector.removeArgumentByName(selections[0]);
    }

    protected void addData() {
        int index = comboDatas.getSelectionIndex();
        if (index == -1) {
            getErrorLabel().setText(Messages.ConnectorTypeEditDialog_38);
            return;
        }
        String dataType = comboDatas.getItem(index);
        String dataName = textNewDataName.getText().trim();
        if (!isIdentifier(dataName)) {
            getErrorLabel().setText(Messages.ConnectorTypeEditDialog_39);
            return;
        }
        if (connector.nameExistsInConnector(dataName)) {
            getErrorLabel().setText(Messages.ConnectorTypeEditDialog_40);
            return;
        }
        DataModel<ConnectorTypeModel> data =
                        (DataModel<ConnectorTypeModel>) DataTypeModel.getModelByName(dataType)
                                        .getInstance().setName(dataName);
        connector.addData(data);
        listDatas.add(dataName);
        handleError(""); //$NON-NLS-1$
    }

    protected void addArgument() {
        int index = comboArguments.getSelectionIndex();
        if (index == -1) {
            handleError(Messages.ConnectorTypeEditDialog_42);
            return;
        }
        String portType = comboArguments.getItem(index);
        String portName = textNewArgumentName.getText().trim();
        if (!isIdentifier(portName)) {
            handleError(Messages.ConnectorTypeEditDialog_43);
            return;
        }
        if (connector.nameExistsInConnector(portName)) {
            handleError(Messages.ConnectorTypeEditDialog_44);
            return;
        }
        PortTypeModel port = PortTypeModel.getModelByName(portType);
        connector.addArgument(port, portName);
        listPortArguments.add(portName);
        handleError(""); //$NON-NLS-1$
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(847, 581);
    }

    @Override
    protected void updateValues() {
        connector.setName(textConnectorName.getText().trim());
        GlobalProperties.getInstance().getTopModel().addChild(connector);
    }

    @Override
    protected void initValues() {
        for (String portTypeName : PortTypeModel.getTypes()) {
            comboArguments.add(portTypeName);
            comboExportPortType.add(portTypeName);
            if (portTypeName.equals("ePort")) { //$NON-NLS-1$
                comboArguments.select(comboArguments.getItemCount() - 1);
                comboExportPortType.select(comboExportPortType.getItemCount() - 1);
            }
        }
        for (String dataTypeName : DataTypeModel.getTypes()) {
            comboDatas.add(dataTypeName);
            if (dataTypeName.equals("bool")) { //$NON-NLS-1$
                comboDatas.select(comboDatas.getItemCount() - 1);
            }
        }
    }

    @Override
    protected Label getErrorLabel() {
        labelError.setForeground(ColorConstants.red);
        return labelError;
    }

    @Override
    protected boolean validateUserInput() {
        return validateConnectorName() && validateArguments() && validateExport()
                        && validateInteractor();
    }

    private boolean validateInteractor() {
        try {
            String interactor = textInteractor.getText().trim();
            if (interactor.isEmpty()) {
                for (String str : listPortArguments.getItems()) {
                    interactor += str + " "; //$NON-NLS-1$
                }
                interactor = interactor.trim();
            }
            connector.parseInteractor(interactor);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            handleError(Messages.ConnectorTypeEditDialog_49);
        }
        return false;
    }

    private boolean validateExport() {
        if (!buttonExport.getSelection()) {
            return true;
        }
        if (comboExportPortType.getSelectionIndex() == -1) {
            handleError(Messages.ConnectorTypeEditDialog_50);
            return false;
        }
        if (!((PortTypeModel) connector.getPort().getType()).allSets()) {
            handleError(Messages.ConnectorTypeEditDialog_51);
            return false;
        }
        return true;
    }

    private boolean validateArguments() {
        if (listPortArguments.getItemCount() == 0) {
            handleError(Messages.ConnectorTypeEditDialog_52);
            return false;
        }
        return true;
    }

    private boolean validateConnectorName() {
        String connectorName = textConnectorName.getText().trim();
        if (!isIdentifier(connectorName)) {
            handleError(Messages.ConnectorTypeEditDialog_53);
            return false;
        }
        if (ConnectorTypeModel.getTypes().contains(connectorName)) {
            handleError(Messages.ConnectorTypeEditDialog_54);
            return false;
        }
        return true;
    }

    public String getNewConnectorTypeName() {
        return connector.getName();
    }

    private void previewPortArgument() {
        Label info = getErrorLabel();
        info.setForeground(ColorConstants.black);
        int index = listPortArguments.getSelectionIndex();
        info.setText(connector.getArgumentAsString(index));
    }

    private void previewData() {
        Label info = getErrorLabel();
        info.setForeground(ColorConstants.black);
        int index = listDatas.getSelectionIndex();
        DataModel<ConnectorTypeModel> data = connector.getDatas().get(index);
        info.setText(MessageFormat.format("{0} {1}", data.getType().getName(), data.getName())); //$NON-NLS-1$
    }
}
