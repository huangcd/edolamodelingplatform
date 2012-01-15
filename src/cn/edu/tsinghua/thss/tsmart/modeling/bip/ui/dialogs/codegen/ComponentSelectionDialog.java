package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import java.util.List;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 选择一个外设构件
 * @author aleck
 *
 */
@SuppressWarnings("rawtypes")
public class ComponentSelectionDialog extends AbstractEditDialog {
	
    private CodeGenProjectModel cgpm = null;
    private Tree                tree;
    private ComponentModel		component;
    private Label               labelErr;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ComponentSelectionDialog(Shell parentShell) {
        super(parentShell, Messages.ComponentSelectionDialog_0);
        setShellStyle(SWT.MAX | SWT.RESIZE);

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return;
        } else {
        	// CodeGenProjectModel
        	if (topModel instanceof CodeGenProjectModel) {
                cgpm = (CodeGenProjectModel) topModel;
        	} else {
        		throw new RuntimeException("something seems wrong"); //$NON-NLS-1$
        	}
        }
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
        
        Label label = new Label(container, SWT.NONE);
        label.setText(Messages.ComponentSelectionDialog_2);

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
        return new Point(538, 484);
    }

    @Override
    protected void updateValues() {
        TreeItem[] item = tree.getSelection();
        component = (ComponentModel) item[0].getData();
    }

    @Override
    protected void initValues() {
        if (cgpm == null) return;

        TreeItem item = new TreeItem(tree, SWT.BOLD);
        item.setText("Components"); //$NON-NLS-1$
        addComponents(cgpm.getStartupModel(), item);
        item.setExpanded(true);
    }

    /**
     * 将startupModel中的组建全部加入到列表中
     * @param startupModel
     * @param item
     */
    private void addComponents(CompoundTypeModel model, TreeItem parent) {
    	if (model == null) {
    		return; 
    	} else if (model instanceof CompoundTypeModel) {
            List<IInstance> children = model.getChildren();
            for (IInstance com : children) {
            	if (com instanceof AtomicModel) {
                    TreeItem childItem = new TreeItem(parent, SWT.BOLD);
                    childItem.setText(com.getName());
                    childItem.setData(com);
            	} else if (com instanceof CompoundModel) {
                    TreeItem childItem = new TreeItem(parent, SWT.BOLD);
                    childItem.setText(com.getName());
                    addComponents((CompoundTypeModel) com.getType(), childItem);
                }
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
        TreeItem[] selected = tree.getSelection();
        if (selected.length != 1) {
        	handleError(Messages.ComponentSelectionDialog_4);
            return false;
        }
        return true;
    }

    public ComponentModel getComponent() {
        return component;
    }
}
