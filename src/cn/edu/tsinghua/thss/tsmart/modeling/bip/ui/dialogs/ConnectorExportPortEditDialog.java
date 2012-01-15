package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class ConnectorExportPortEditDialog extends AbstractEditDialog {
    private ConnectorTypeModel connector;
    private PortModel          port;
    private Table              table;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ConnectorExportPortEditDialog(Shell parentShell, ConnectorTypeModel connector) {
        super(parentShell, Messages.ConnectorExportPortEditDialog_0);
        this.connector = connector;
        this.port = connector.getPort();
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

        table = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(10, 10, 324, 216);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        TableColumn tableColumn = new TableColumn(table, SWT.CENTER);
        tableColumn.setWidth(163);
        tableColumn.setText(Messages.ConnectorExportPortEditDialog_1);

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(152);
        tblclmnNewColumn.setText(Messages.ConnectorExportPortEditDialog_2);

        initValues();
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
        return new Point(350, 321);
    }

    @Override
    protected void initValues() {}

    @Override
    protected void updateValues() {}

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
