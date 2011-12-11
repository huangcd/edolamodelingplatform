package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;

public class AtomicEditPart extends BaseEditableEditPart {
    private Label typeLabel;
    private Label instanceLabel;
    private Panel panel;

    public AtomicModel getModel() {
        return (AtomicModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AtomicModel.TYPE_NAME.equals(evt.getPropertyName())) {
            typeLabel.setText(getModel().getType().getName());
        } else if (AtomicModel.NAME.equals(evt.getPropertyName())) {
            instanceLabel.setText(getModel().getName());
        }
        refresh();
    }

    public void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }

    @Override
    protected IFigure createFigure() {
        panel = new Panel();
        panel.setLayoutManager(new GridLayout(1, true));
        typeLabel = new Label(getModel().getType().getName());
        instanceLabel = new Label(getModel().getName());
        panel.add(typeLabel);
        panel.add(instanceLabel);
        return panel;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }

    @Override
    protected void performDoubleClick() {
        AtomicTypeModel container = getModel().getType();
        BIPEditor.openBIPEditor(container);
    }
}
