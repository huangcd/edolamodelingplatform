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
import org.eclipse.wb.swt.SWTResourceManager;
import java.util.ArrayList;


@SuppressWarnings("rawtypes")
public class EntitySelectionDialog extends AbstractEditDialog {
    private Label             labelError;
    private Tree              tree;
    private ArrayList<String> entityNames;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EntitySelectionDialog(Shell parentShell, ArrayList<String> entityNames) {
        super(parentShell, "选择参数");
        this.entityNames = new ArrayList<String>(entityNames);
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
        tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 109, 221, 17);


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
    protected void updateValues() {}

    @Override
    protected void initValues() {

        TreeItem item;
        
        if (!entityNames.contains("HW")) {
            item = new TreeItem(tree, SWT.BOLD);
            item.setText("HW");
        }
        
        if (!entityNames.contains("Functional")) {
            item = new TreeItem(tree, SWT.BOLD);
            item.setText("Functional");
        }
        
        if (!entityNames.contains("SwC")) {
            item = new TreeItem(tree, SWT.BOLD);
            item.setText("SwC");
        }
    }


    @Override
    protected Label getErrorLabel() {
        labelError.setForeground(ColorConstants.red);
        return labelError;
    }

    @Override
    protected boolean validateUserInput() {
        TreeItem[] item = tree.getSelection();
        if (item.length == 0) {
            Label label = getErrorLabel();
            label.setText("请选择一个节点。");
            return false;
        }

       
        if(!entityNames.contains(item[0].getText())){
            entityNames.add(item[0].getText());
        }

        return true;
    }

    public ArrayList<String> getEntityNames() {
        return entityNames;
    }
}
