package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class ConnectorTypeManageDialog extends AbstractEditDialog {
    private Label      labelError;
    private List       listTypes;
    private StyledText styledTextPreview;
    private Button buttonDelete;

    public ConnectorTypeManageDialog(Shell parentShell) {
        super(parentShell, Messages.ConnectorTypeManageDialog_0);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        SashForm sashForm = new SashForm(container, SWT.NONE);
        sashForm.setBounds(10, 10, 717, 329);

        Group groupSelection = new Group(sashForm, SWT.NONE);
        groupSelection.setText(Messages.ConnectorTypeManageDialog_1);
        groupSelection.setLayout(new FillLayout(SWT.HORIZONTAL));

        SashForm sashFormSelection = new SashForm(groupSelection, SWT.VERTICAL);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(sashFormSelection, SWT.BORDER | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listTypes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selections = listTypes.getSelection();
                if (selections == null || selections.length == 0) {
                    return;
                }
                styledTextPreview.setText(ConnectorTypeModel.getModelByName(selections[0])
                                .exportToBip());
            }
        });
        scrolledComposite.setContent(listTypes);
        scrolledComposite.setMinSize(listTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        SashForm sashFormEdit = new SashForm(sashFormSelection, SWT.NONE);

        Button buttonCreate = new Button(sashFormEdit, SWT.NONE);
        buttonCreate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addConnector();
            }
        });
        buttonCreate.setText(Messages.ConnectorTypeManageDialog_2);

        Button buttonEdit = new Button(sashFormEdit, SWT.NONE);
        buttonEdit.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                editConnectorType();
            }
        });
        buttonEdit.setText(Messages.ConnectorTypeManageDialog_3);

        buttonDelete = new Button(sashFormEdit, SWT.NONE);
        buttonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                removeConnectorType();
            }
        });
        buttonDelete.setText(Messages.ConnectorTypeManageDialog_4);
        sashFormEdit.setWeights(new int[] {1, 1, 1});
        sashFormSelection.setWeights(new int[] {10, 1});

        Group groupPreview = new Group(sashForm, SWT.NONE);
        groupPreview.setText(Messages.ConnectorTypeManageDialog_5);
        groupPreview.setLayout(new FillLayout(SWT.HORIZONTAL));


        styledTextPreview =
                        new StyledText(groupPreview, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP
                                        | SWT.H_SCROLL | SWT.V_SCROLL);
        sashForm.setWeights(new int[] {231, 483});

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 345, 717, 17);

        initValues();
        return container;
    }

    private void editConnectorType() {
        int index = listTypes.getSelectionIndex();
        if (index < 0 || index >= listTypes.getItemCount()) {
            return;
        }
        ConnectorTypeModel model = ConnectorTypeModel.getModelByName(listTypes.getItem(index));
        ConnectorTypeEditDialog dialog = new ConnectorTypeEditDialog(getParentShell(), model);
        dialog.setBlockOnOpen(true);
        int result = dialog.open();
        if (OK == result) {
            listTypes.remove(index);
            listTypes.add(dialog.getNewConnectorTypeName(), index);
        }
    }

    private void removeConnectorType() {
        String[] selections = listTypes.getSelection();
        if (selections == null || selections.length == 0) {
            return;
        }
        for (String selection : selections) {
            if (selection.equals("singleton") || selection.equals("rendezvous")) { //$NON-NLS-1$ //$NON-NLS-2$
                getErrorLabel().setText(Messages.ConnectorTypeManageDialog_8);
            } else {
                if (ConnectorTypeModel.isRemovable(selection)) {
                    properties.getTopModel().removeChildByName(selection);
                    listTypes.remove(selection);
                } else
                    MessageUtil.ShowDeleteConnectorTypeErrorDialog(selection);
            }
        }
    }

    private void addConnector() {
        ConnectorTypeEditDialog dialog = new ConnectorTypeEditDialog(getParentShell());
        dialog.setBlockOnOpen(true);
        int result = dialog.open();
        if (OK == result) {
            listTypes.add(dialog.getNewConnectorTypeName());
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(743, 456);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {
        if (GlobalProperties.getInstance().getTopModel() instanceof ProjectModel) {
            buttonDelete.setEnabled(false);
        }
        for (String connectorName : ConnectorTypeModel.getTypes()) {
            listTypes.add(connectorName);
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
