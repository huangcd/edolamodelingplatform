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
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.*;


@SuppressWarnings("rawtypes")
public class VariableSelectionDialog extends AbstractEditDialog {
    private ComponentTypeModel model;
    private DataModel          selectionData;
    private Label              labelError;
    private Tree               tree;
    private String             variableName;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public VariableSelectionDialog(Shell parentShell, ComponentTypeModel model) {
        super(parentShell, "选择参数");
        this.model = model;
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
    protected void updateValues() {
    }

    public void setSelectionData(DataModel data) {
        this.selectionData = data;
    }

    public DataModel getSelectionData() {
        return selectionData;
    }

    @Override
    protected void initValues() {

        TreeItem item = new TreeItem(tree, SWT.BOLD);
        item.setText("Variables");
        addVariables(model, item);

        item = new TreeItem(tree, SWT.BOLD);
        item.setText("Places");
        addPlaces(model, item);
    }

    private void addVariables(ComponentTypeModel ctm, TreeItem item) {
        if (ctm == null) return;

        if (ctm instanceof CompoundTypeModel) {

            CompoundTypeModel cm = (CompoundTypeModel) ctm;
            TreeItem cmItem = new TreeItem(item, SWT.BOLD);
            cmItem.setText(cm.getInstance().getName());

            List<IInstance> children = cm.getChildren();
            for (IInstance com : children) {
                if (com instanceof ComponentModel) {
                    addVariables((ComponentTypeModel) com.getType(), cmItem);
                }
            }
        }

        if (ctm instanceof AtomicTypeModel) {
            AtomicTypeModel atm = (AtomicTypeModel) ctm;
            TreeItem atmItem = new TreeItem(item, SWT.BOLD);
            atmItem.setText(atm.getInstance().getName());

            TreeItem dataItem = new TreeItem(atmItem, SWT.BOLD);
            dataItem.setText("place");
            dataItem.setData(atm);

            for (IModel data : atm.getChildren()) {
                if (data instanceof DataModel) {
                    dataItem = new TreeItem(atmItem, SWT.BOLD);
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
            TreeItem cmItem = new TreeItem(item, SWT.BOLD);
            cmItem.setText(cm.getInstance().getName());

            List<IInstance> children = cm.getChildren();
            for (IInstance com : children) {
                if (com instanceof ComponentModel) {
                    addPlaces((ComponentTypeModel) com.getType(), cmItem);
                }
            }
        }

        if (ctm instanceof AtomicTypeModel) {
            AtomicTypeModel atm = (AtomicTypeModel) ctm;
            TreeItem atmItem = new TreeItem(item, SWT.BOLD);
            atmItem.setText(atm.getInstance().getName());

            for (IModel place : atm.getChildren()) {
                if (place instanceof PlaceModel) {
                    TreeItem placeItem = new TreeItem(atmItem, SWT.BOLD);
                    placeItem.setText(place.getName());
                    placeItem.setData(place);
                }
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
        TreeItem[] item = tree.getSelection();
        if (item.length == 0) {
            Label label = getErrorLabel();
            label.setText("请选择一个节点。");
            return false;
        }


        if (item[0].getData() == null) {
            Label label = getErrorLabel();
            label.setText("请选择一个叶子节点。");
            return false;
        }

        variableName = item[0].getText();

        TreeItem it = item[0].getParentItem();
        while (it.getParentItem() != null) {
            variableName = it.getText() + "." + variableName;
            it = it.getParentItem();
        }
        
//        variableName = ((BaseInstanceModel)item[0].getData()).getFullName();

        return true;
    }

    public String getVariableName() {
        return variableName;
    }
}
