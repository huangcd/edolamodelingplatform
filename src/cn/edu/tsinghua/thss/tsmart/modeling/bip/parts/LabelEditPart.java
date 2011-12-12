package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LabelModel;

public class LabelEditPart extends BaseGraphicalEditPart {
    private Label label;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (LabelModel.COLOR.equals(evt.getPropertyName())) {
            label.setForegroundColor(getModel().getTextColor());
        } else if (LabelModel.LABEL.equals(evt.getPropertyName())) {
            label.setText(getModel().getLabel());
        }
        refresh();
    }

    @Override
    protected IFigure createFigure() {
        LabelModel model = getModel();
        label = new Label(model.getLabel());
        label.setFont(properties.getDefaultEditorFont());
        label.setForegroundColor(model.getTextColor());
        return label;
    }

    protected void refreshVisuals() {
        ((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), getModel()
                        .getPositionConstraint());
    }

    public LabelModel getModel() {
        return (LabelModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {}
}
