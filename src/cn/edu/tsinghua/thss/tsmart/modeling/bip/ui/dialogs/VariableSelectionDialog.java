package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


@SuppressWarnings("rawtypes")
public class VariableSelectionDialog extends AbstractEditDialog {
    private CodeGenProjectModel cgpm = null;
    private Tree                tree;
    private String              variableName;
    private Label               labelErr;
    private boolean             onlyVariables;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public VariableSelectionDialog(Shell parentShell, boolean onlyVariables) {
        super(parentShell, Messages.VariableSelectionDialog_0);
        setShellStyle(SWT.MAX | SWT.RESIZE);
        this.onlyVariables = onlyVariables;

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.VariableSelectionDialog_1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        cgpm = (CodeGenProjectModel) topModel;
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
        return new Point(538, 484);
    }

    @Override
    protected void updateValues() {
        TreeItem[] item = tree.getSelection();
        variableName = item[0].getText();

        if (!(item[0].getData() instanceof PlaceModel)) {
            TreeItem it = item[0].getParentItem();
            while (it.getParentItem() != null) {
                variableName = it.getText() + "." + variableName; //$NON-NLS-1$
                it = it.getParentItem();
            }
        }
    }

    @Override
    protected void initValues() {

        if (cgpm == null) return;

        TreeItem item = new TreeItem(tree, SWT.BOLD);
        item.setText(Messages.VariableSelectionDialog_3);
        addVariables(cgpm.getStartupModel(), item);

        if (!onlyVariables) {
            item = new TreeItem(tree, SWT.BOLD);
            item.setText(Messages.VariableSelectionDialog_4);
            addPlaces(cgpm.getStartupModel(), item);
        }
    }

    private void addVariables(ComponentTypeModel ctm, TreeItem item) {
        if (ctm == null) return;

        if (ctm instanceof CompoundTypeModel) {

            CompoundTypeModel cm = (CompoundTypeModel) ctm;

            List<IInstance> children = cm.getChildren();
            for (IInstance com : children) {
                if (com instanceof ComponentModel) {
                    TreeItem cmItem = new TreeItem(item, SWT.BOLD);
                    cmItem.setText(com.getName());

                    addVariables((ComponentTypeModel) com.getType(), cmItem);
                }
            }
        }

        if (ctm instanceof AtomicTypeModel) {
            AtomicTypeModel atm = (AtomicTypeModel) ctm;

            TreeItem dataItem;
            if (!onlyVariables) {
                dataItem = new TreeItem(item, SWT.BOLD);
                dataItem.setText(Messages.VariableSelectionDialog_5);
                dataItem.setData(atm);
            }

            for (IModel data : atm.getChildren()) {
                if (data instanceof DataModel) {
                    dataItem = new TreeItem(item, SWT.BOLD);
                    dataItem.setText(data.getName());
                    dataItem.setData(data);
                }
            }
        }
    }

    private void addPlaces(ComponentTypeModel ctm, TreeItem item) {
        if (ctm == null) return;

        if (ctm instanceof CompoundTypeModel) {

            CompoundTypeModel cm = (CompoundTypeModel) ctm;

            List<IInstance> children = cm.getChildren();
            for (IInstance com : children) {
                if (com instanceof ComponentModel) {
                    TreeItem cmItem = new TreeItem(item, SWT.BOLD);
                    cmItem.setText(com.getName());

                    addPlaces((ComponentTypeModel) com.getType(), cmItem);
                }
            }
        }

        if (ctm instanceof AtomicTypeModel) {
            AtomicTypeModel atm = (AtomicTypeModel) ctm;

            for (IModel place : atm.getChildren()) {
                if (place instanceof PlaceModel) {
                    TreeItem placeItem = new TreeItem(item, SWT.BOLD);
                    placeItem.setText(place.getName());
                    placeItem.setData(place);
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
        TreeItem[] item = tree.getSelection();
        if (item.length == 0 || item[0].getData() == null) {
            handleError(Messages.VariableSelectionDialog_6);
            return false;
        }


        // variableName = ((BaseInstanceModel)item[0].getData()).getFullName();

        return true;
    }

    public String getData() {
        return variableName;
    }
}
