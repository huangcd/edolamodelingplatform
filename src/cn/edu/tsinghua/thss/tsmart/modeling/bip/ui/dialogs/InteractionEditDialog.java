package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InteractionModel;

public class InteractionEditDialog extends AbstractEditDialog {
    private InteractionModel   interaction;
    private ConnectorTypeModel connector;
    private boolean            isCreate;
    private List               listArguments;
    private List               listInteractArguments;
    private Label              labelError;
    private Button             buttonAdd;
    private Button             buttonRemove;
    private StyledText         textDownAction;
    private StyledText         textUpAction;
    private Label              label;
    private Label              label_1;

    public InteractionEditDialog(Shell shell, ConnectorTypeModel connector,
                    InteractionModel interaction) {
        super(shell, "编辑连接子");
        this.connector = connector;
        this.interaction = interaction;
        this.isCreate = interaction == null;
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

        Group group = new Group(container, SWT.NONE);
        group.setBounds(13, 10, 236, 250);
        group.setText("\u9009\u62E9\u4EA4\u4E92\u7AEF\u53E3");

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(3, 17, 90, 230);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listArguments = new List(scrolledComposite, SWT.BORDER | SWT.H_SCROLL | SWT.MULTI);
        scrolledComposite.setContent(listArguments);
        scrolledComposite.setMinSize(listArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        ScrolledComposite scrolledComposite_1 =
                        new ScrolledComposite(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_1.setBounds(143, 17, 90, 230);
        scrolledComposite_1.setExpandVertical(true);
        scrolledComposite_1.setExpandHorizontal(true);

        listInteractArguments =
                        new List(scrolledComposite_1, SWT.BORDER | SWT.H_SCROLL | SWT.MULTI);
        listInteractArguments.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previewPortArgument();
            }
        });
        scrolledComposite_1.setContent(listInteractArguments);
        scrolledComposite_1.setMinSize(listInteractArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite_1.setMinSize(new Point(71, 68));

        buttonAdd = new Button(group, SWT.NONE);
        buttonAdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addArugment();
            }
        });
        buttonAdd.setBounds(103, 40, 30, 27);
        buttonAdd.setText("\u2192");

        buttonRemove = new Button(group, SWT.NONE);
        buttonRemove.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeArgument();
            }
        });
        buttonRemove.setBounds(103, 167, 30, 27);
        buttonRemove.setText("\u2190");

        label = new Label(container, SWT.NONE);
        label.setBounds(272, 10, 61, 17);
        label.setText("\u4E0A\u52A8\u4F5C");

        ScrolledComposite scrolledComposite_2 =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_2.setExpandVertical(true);
        scrolledComposite_2.setExpandHorizontal(true);
        scrolledComposite_2.setBounds(272, 30, 260, 121);

        textUpAction = new StyledText(scrolledComposite_2, SWT.BORDER);
        textUpAction.setText("// up action");
        scrolledComposite_2.setContent(textUpAction);
        scrolledComposite_2.setMinSize(textUpAction.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite_2.setMinSize(new Point(73, 21));

        label_1 = new Label(container, SWT.NONE);
        label_1.setText("\u4E0B\u52A8\u4F5C");
        label_1.setBounds(272, 157, 61, 17);

        ScrolledComposite scrolledComposite_3 =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_3.setExpandVertical(true);
        scrolledComposite_3.setExpandHorizontal(true);
        scrolledComposite_3.setBounds(272, 177, 260, 123);

        textDownAction = new StyledText(scrolledComposite_3, SWT.BORDER);
        textDownAction.setText("// down action");
        scrolledComposite_3.setContent(textDownAction);
        scrolledComposite_3.setMinSize(textDownAction.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(13, 283, 236, 17);

        initValues();
        return container;
    }

    protected void previewPortArgument() {
        Label info = getErrorLabel();
        info.setForeground(ColorConstants.black);
        int index = listInteractArguments.getSelectionIndex();
        if (index == -1) {
            return;
        }
        info.setText(connector.getArgumentAsString(index));
    }

    protected void removeArgument() {
        String[] arguments = listInteractArguments.getSelection();
        if (arguments.length == 0) {
            getErrorLabel().setText("没有选择任何参数");
            return;
        }
        for (String argument : arguments) {
            listInteractArguments.remove(argument);
            listArguments.add(argument);
        }
    }

    protected void addArugment() {
        String[] arguments = listArguments.getSelection();
        if (arguments.length == 0) {
            getErrorLabel().setText("没有选择任何参数");
            return;
        }
        for (String argument : arguments) {
            listArguments.remove(argument);
            listInteractArguments.add(argument);
        }
    }

    @Override
    protected void updateValues() {
        ArrayList<String> list = new ArrayList<String>();
        java.util.List<String> interactionArguments =
                        Arrays.asList(listInteractArguments.getItems());
        for (String argument : connector.getArgumentNames()) {
            if (interactionArguments.contains(argument)) {
                list.add(argument);
            }
        }

        if (isCreate) {
            interaction =
                            connector.addInteraction(list, textUpAction.getText(),
                                            textDownAction.getText());
        } else {
            connector.updateInteraction(interaction.getInteractionString(), list,
                            textUpAction.getText(), textDownAction.getText());
        }
    }

    @Override
    protected void initValues() {
        if (isCreate) {
            for (String argument : connector.getArgumentNames()) {
                listArguments.add(argument);
            }
        } else {
            for (String argument : connector.getArgumentNames()) {
                listArguments.add(argument);
            }
            for (String interactionArgument : interaction.getInteractionPortsAsStringArray()) {
                listInteractArguments.add(interactionArgument);
                listArguments.remove(interactionArgument);
            }
            textUpAction.setText(interaction.getUpAction().getAction());
            textDownAction.setText(interaction.getDownAction().getAction());
        }
    }

    @Override
    protected Label getErrorLabel() {
        labelError.setForeground(ColorConstants.red);
        return labelError;
    }

    @Override
    protected boolean validateUserInput() {
        if (listInteractArguments.getItemCount() == 0) {
            getErrorLabel().setText("参与交互的端口不能为空");
            return false;
        }
        StringBuilder buffer = new StringBuilder();
        java.util.List<String> interactionArguments =
                        Arrays.asList(listInteractArguments.getItems());
        for (String argument : connector.getArgumentNames()) {
            if (interactionArguments.contains(argument)) {
                buffer.append(argument).append(' ');
            }
        }

        String newInteractionName = buffer.toString().trim();
        if ((interaction == null || !interaction.getInteractionString().equals(newInteractionName))
                        && connector.interactionNameExistsInConnector(newInteractionName)) {
            getErrorLabel().setText("交互已经存在");
            return false;
        }
        return true;
    }

    public String getInteractionName() {
        return interaction.getInteractionString();
    }
}
