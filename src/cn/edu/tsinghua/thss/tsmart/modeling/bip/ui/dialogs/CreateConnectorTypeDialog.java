package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
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

public class CreateConnectorTypeDialog extends AbstractEditDialog {
    private Label      labelError;
    private Text       textNewArgumentName;
    private Text       textDataName;
    private StyledText styledTextInteractionPreview;
    private List       listInteractions;
    private List       listPortArguments;
    private Button     buttonDeleteArgument;
    private Button     buttonMoveArgumentUp;
    private Button     buttonMoveArgumentDown;
    private Combo      comboArguments;
    private Button     buttonAddArgument;
    private List       listDatas;
    private Button     buttonDeleteData;
    private Combo      comboDataTypes;
    private Button     buttonAddData;
    private Button     buttonMoveInteractionUp;
    private Button     buttonCreateInteraction;
    private Button     buttonMoveInteractionDown;
    private Button     buttonDeleteInteraction;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public CreateConnectorTypeDialog(Shell parentShell) {
        super(parentShell, "创建连接子");
        setTitle("\u65B0\u5EFA\u8FDE\u63A5\u5B50");
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

        Group groupArgument = new Group(container, SWT.SHADOW_ETCHED_IN);
        groupArgument.setText("\u7AEF\u53E3\u53C2\u6570");
        groupArgument.setBounds(10, 10, 237, 270);
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
            public void widgetSelected(SelectionEvent e) {}
        });
        listPortArguments.setLocation(0, -129);
        scrolledComposite.setContent(listPortArguments);
        scrolledComposite.setMinSize(listPortArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        buttonDeleteArgument = new Button(groupArgument, SWT.NONE);
        buttonDeleteArgument.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO delete
            }
        });
        buttonDeleteArgument.setBounds(119, 58, 106, 27);
        buttonDeleteArgument.setText("\u5220\u9664\u53C2\u6570");

        buttonMoveArgumentUp = new Button(groupArgument, SWT.NONE);
        buttonMoveArgumentUp.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO move up
            }
        });
        buttonMoveArgumentUp.setBounds(119, 23, 34, 27);
        buttonMoveArgumentUp.setText("\u2191");

        buttonMoveArgumentDown = new Button(groupArgument, SWT.NONE);
        buttonMoveArgumentDown.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TOOD move down
            }
        });
        buttonMoveArgumentDown.setText("\u2193");
        buttonMoveArgumentDown.setBounds(191, 23, 34, 27);

        Group group = new Group(groupArgument, SWT.NONE);
        group.setBounds(119, 91, 106, 168);

        comboArguments = new Combo(group, SWT.READ_ONLY);
        comboArguments.setBounds(10, 42, 86, 25);

        Label label = new Label(group, SWT.NONE);
        label.setBounds(10, 19, 61, 17);
        label.setText("\u7AEF\u53E3\u7C7B\u578B\uFF1A");

        Label lblNewLabel = new Label(group, SWT.NONE);
        lblNewLabel.setBounds(10, 73, 61, 17);
        lblNewLabel.setText("\u53C2\u6570\u540D\u79F0\uFF1A");

        textNewArgumentName = new Text(group, SWT.BORDER);
        textNewArgumentName.setBounds(10, 97, 86, 23);

        buttonAddArgument = new Button(group, SWT.NONE);
        buttonAddArgument.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO add
            }
        });
        buttonAddArgument.setBounds(10, 132, 86, 27);
        buttonAddArgument.setText("\u589E\u52A0\u53C2\u6570");

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 459, 821, 17);

        Group group_1 = new Group(container, SWT.SHADOW_ETCHED_IN);
        group_1.setLayout(null);
        group_1.setText("\u5185\u90E8\u53D8\u91CF");
        group_1.setBounds(261, 10, 237, 270);

        ScrolledComposite scrolledComposite_1 =
                        new ScrolledComposite(group_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_1.setExpandVertical(true);
        scrolledComposite_1.setExpandHorizontal(true);
        scrolledComposite_1.setBounds(10, 23, 103, 236);

        listDatas = new List(scrolledComposite_1, SWT.BORDER);
        listDatas.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        scrolledComposite_1.setContent(listDatas);
        scrolledComposite_1.setMinSize(listDatas.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite_1.setMinSize(new Point(71, 68));

        buttonDeleteData = new Button(group_1, SWT.NONE);
        buttonDeleteData.setText("\u5220\u9664\u53D8\u91CF");
        buttonDeleteData.setBounds(119, 56, 106, 27);

        Group group_2 = new Group(group_1, SWT.NONE);
        group_2.setBounds(119, 89, 106, 170);

        comboDataTypes = new Combo(group_2, SWT.READ_ONLY);
        comboDataTypes.setBounds(10, 46, 86, 25);

        Label label_1 = new Label(group_2, SWT.NONE);
        label_1.setText("\u53D8\u91CF\u7C7B\u578B\uFF1A");
        label_1.setBounds(10, 23, 61, 17);

        Label label_2 = new Label(group_2, SWT.NONE);
        label_2.setText("\u53C2\u6570\u540D\u79F0\uFF1A");
        label_2.setBounds(10, 77, 61, 17);

        textDataName = new Text(group_2, SWT.BORDER);
        textDataName.setBounds(10, 100, 86, 23);

        buttonAddData = new Button(group_2, SWT.NONE);
        buttonAddData.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        buttonAddData.setText("\u589E\u52A0\u53C2\u6570");
        buttonAddData.setBounds(10, 129, 86, 27);

        Group group_3 = new Group(container, SWT.NONE);
        group_3.setText("\u5BFC\u51FA\u7AEF\u53E3");
        group_3.setBounds(10, 395, 488, 58);

        Button buttonExport = new Button(group_3, SWT.CHECK);
        buttonExport.setBounds(10, 27, 45, 17);
        buttonExport.setText("\u5BFC\u51FA");

        Label label_3 = new Label(group_3, SWT.NONE);
        label_3.setBounds(83, 27, 61, 17);
        label_3.setText("\u7AEF\u53E3\u7C7B\u578B\uFF1A");

        Combo comboExportPortType = new Combo(group_3, SWT.READ_ONLY);
        comboExportPortType.setBounds(150, 23, 86, 25);

        Button buttonBoundData = new Button(group_3, SWT.NONE);
        buttonBoundData.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // TODO bounded data
            }
        });
        buttonBoundData.setBounds(261, 22, 80, 27);
        buttonBoundData.setText("\u7ED1\u5B9A\u53C2\u6570");

        Group group_4 = new Group(container, SWT.NONE);
        group_4.setText("\u4EA4\u4E92");
        group_4.setBounds(504, 10, 327, 443);

        ScrolledComposite scrolledComposite_2 =
                        new ScrolledComposite(group_4, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_2.setBounds(10, 21, 307, 119);
        scrolledComposite_2.setExpandHorizontal(true);
        scrolledComposite_2.setExpandVertical(true);

        listInteractions = new List(scrolledComposite_2, SWT.BORDER);
        listInteractions.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        scrolledComposite_2.setContent(listInteractions);
        scrolledComposite_2.setMinSize(listInteractions.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        buttonMoveInteractionUp = new Button(group_4, SWT.NONE);
        buttonMoveInteractionUp.setText("\u2191");
        buttonMoveInteractionUp.setBounds(124, 150, 34, 27);

        buttonCreateInteraction = new Button(group_4, SWT.NONE);
        buttonCreateInteraction.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        buttonCreateInteraction.setText("\u589E\u52A0\u4EA4\u4E92");
        buttonCreateInteraction.setBounds(10, 150, 106, 27);

        buttonMoveInteractionDown = new Button(group_4, SWT.NONE);
        buttonMoveInteractionDown.setText("\u2193");
        buttonMoveInteractionDown.setBounds(168, 150, 34, 27);

        buttonDeleteInteraction = new Button(group_4, SWT.NONE);
        buttonDeleteInteraction.setText("\u5220\u9664\u4EA4\u4E92");
        buttonDeleteInteraction.setBounds(211, 150, 106, 27);

        SashForm sashForm = new SashForm(group_4, SWT.NONE);
        sashForm.setBounds(10, 183, 307, 249);

        styledTextInteractionPreview =
                        new StyledText(sashForm, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        sashForm.setWeights(new int[] {1});

        return container;
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
        // TODO Auto-generated method stub

    }

    @Override
    protected void initValues() {
        // TODO Auto-generated method stub

    }

    @Override
    protected Label getErrorLabel() {
        return labelError;
    }

    @Override
    protected boolean validateUserInput() {
        // TODO Auto-generated method stub
        return false;
    }
}
