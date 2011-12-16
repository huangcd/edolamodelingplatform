package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Display;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.DataEditDialog;

@SuppressWarnings("rawtypes")
public class DataEditPart extends BaseEditableEditPart {
    private Label label;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
            refreshVisuals();
        } else if (DataModel.DATA_TYPE.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
            refreshVisuals();
        } else if (DataModel.DATA_VALUE.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
            refreshVisuals();
        }
        refreshVisuals();
    }

    @Override
    protected void performDoubleClick() {
        DataEditDialog dialog =
                        new DataEditDialog(Display.getCurrent().getActiveShell(), getModel());
        dialog.setBlockOnOpen(true);
        dialog.open();
    }

    @Override
    protected IFigure createFigure() {
        DataModel model = getModel();
        label = new Label(model.getFriendlyString());
        label.setFont(properties.getDefaultEditorFont());
        label.setForegroundColor(properties.getDataLabelColor());
        return label;
    }

    public DataModel getModel() {
        return (DataModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }
}
