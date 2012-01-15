package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;


@SuppressWarnings("rawtypes")
public class ConnectorSelectionDialog extends AbstractEditDialog {
    private CompoundTypeModel ctm;
    private Tree              tree;
    private ConnectorModel    connector;
    private Label             labelErr;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ConnectorSelectionDialog(Shell parentShell, CompoundTypeModel ctm) {
        super(parentShell, Messages.ConnectorSelectionDialog_0);
        this.ctm = ctm;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);

        tree = new Tree(container, SWT.BORDER);
        GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_tree.widthHint = 376;
        tree.setLayoutData(gd_tree);

        labelErr = new Label(container, SWT.HORIZONTAL);
        GridData gd_labelErr = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_labelErr.widthHint = 422;
        labelErr.setLayoutData(gd_labelErr);


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
        return new Point(450, 300);
    }

    @Override
    protected void updateValues() {
        TreeItem[] item = tree.getSelection();
        connector = (ConnectorModel) item[0].getData();
    }

    @Override
    protected void initValues() {

        if (ctm == null) return;

        for (IInstance child : ctm.getChildren()) {
            if (child instanceof ConnectorModel) {
                TreeItem item = new TreeItem(tree, SWT.BOLD);
                item.setText(child.getName());
                item.setData(child);
            }
        }


    }

    @Override
    protected Label getErrorLabel() {
        labelErr.setForeground(ColorConstants.red);
        return labelErr;
    }

    @Override
    protected boolean validateUserInput() {
        TreeItem[] item = tree.getSelection();
        if (item.length == 0) {
            handleError(Messages.ConnectorSelectionDialog_1);
            return false;
        }

        return true;
    }

    public ConnectorModel getData() {
        return connector;
    }
}
