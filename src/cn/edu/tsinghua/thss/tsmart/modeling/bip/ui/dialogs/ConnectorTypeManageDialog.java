package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;

public class ConnectorTypeManageDialog extends AbstractEditDialog {
    private Label      labelError;
    private List       listConnectorTypes;
    private StyledText styledTextPreview;

    public ConnectorTypeManageDialog(Shell parentShell) {
        super(parentShell, "连接子类型管理");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(10, 10, 288, 195);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listConnectorTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listConnectorTypes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selections = listConnectorTypes.getSelection();
                if (selections == null || selections.length == 0) {
                    return;
                }
                styledTextPreview.setText(ConnectorTypeModel.getConnectorTypeModel(selections[0])
                                .exportToBip());
            }
        });
        scrolledComposite.setContent(listConnectorTypes);
        scrolledComposite.setMinSize(listConnectorTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 244, 717, 17);

        Button buttonCreate = new Button(container, SWT.NONE);
        buttonCreate.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                addConnector();
            }
        });
        buttonCreate.setBounds(10, 211, 120, 27);
        buttonCreate.setText("\u589E\u52A0\u8FDE\u63A5\u5B50");

        Button buttonDelete = new Button(container, SWT.NONE);
        buttonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selections = listConnectorTypes.getSelection();
                if (selections == null || selections.length == 0) {
                    return;
                }
                String connectorType = selections[0];
                if (connectorType.equals("singleton") || connectorType.equals("rendezvous")) {
                    getErrorLabel().setText("内置类型不能被删除");
                } else {
                    ConnectorTypeModel.removeType(connectorType);
                    listConnectorTypes.remove(connectorType);
                }
            }
        });
        buttonDelete.setBounds(178, 211, 120, 27);
        buttonDelete.setText("\u5220\u9664\u8FDE\u63A5\u5B50");

        ScrolledComposite scrolledComposite_1 =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite_1.setBounds(304, 10, 423, 228);
        scrolledComposite_1.setExpandHorizontal(true);
        scrolledComposite_1.setExpandVertical(true);

        styledTextPreview =
                        new StyledText(scrolledComposite_1, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        scrolledComposite_1.setContent(styledTextPreview);
        scrolledComposite_1.setMinSize(styledTextPreview.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        initValues();
        return container;
    }

    private void addConnector() {
        ConnectorTypeEditDialog dialog = new ConnectorTypeEditDialog(getParentShell());
        dialog.setBlockOnOpen(true);
        int result = dialog.open();
        if (OK == result) {
            listConnectorTypes.add(dialog.getNewConnectorTypeName());
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(743, 356);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {
        for (String connectorName : ConnectorTypeModel.getTypes()) {
            listConnectorTypes.add(connectorName);
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
