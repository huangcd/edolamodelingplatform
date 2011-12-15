package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;

@SuppressWarnings("rawtypes")
public class PortEditPart extends BaseEditableEditPart {
    private Label label;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
        } else if (PortModel.EXPORT.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
        } else if (IModel.CHILDREN.equals(evt.getPropertyName())) {
            label.setText(getModel().getFriendlyString());
        }
        refreshVisuals();
    }

    @Override
    protected void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }

    @Override
    protected void performDoubleClick() {}

    @Override
    protected IFigure createFigure() {
        PortModel model = getModel();
        label = new Label(model.getFriendlyString());
        label.setFont(properties.getDefaultEditorFont());
        label.setForegroundColor(properties.getDataLabelColor());
        return label;
    }

    public PortModel getModel() {
        return (PortModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }
}
