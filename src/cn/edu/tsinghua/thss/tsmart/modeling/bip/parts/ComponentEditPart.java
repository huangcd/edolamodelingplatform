package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.FrameContainer;

@SuppressWarnings("rawtypes")
public abstract class ComponentEditPart extends BaseEditableEditPart {
    private Label          typeLabel;
    private Label          instanceLabel;
    private FrameContainer panel;

    public IComponentInstance getModel() {
        return (IComponentInstance) super.getModel();
    }

    @Override
    protected IFigure createFigure() {
        panel = new FrameContainer(getModel().getName(), getModel().getType().getName(), this);
        panel.setFont(properties.getDefaultEditorFont());

        typeLabel = new Label(getModel().getType().getName());
        typeLabel.setForegroundColor(ColorConstants.darkBlue);
        typeLabel.setFont(properties.getDefaultEditorFont());

        instanceLabel = new Label(getModel().getName());
        instanceLabel.setForegroundColor(ColorConstants.lightBlue);
        instanceLabel.setFont(properties.getDefaultEditorFont());
        resizeLabel();

        panel.add(typeLabel);
        panel.add(instanceLabel);
        return panel;
    }

    private void resizeLabel() {
        typeLabel.setTextAlignment(PositionConstants.CENTER);
        instanceLabel.setTextAlignment(PositionConstants.CENTER);
        Dimension typeSize = typeLabel.getPreferredSize();
        Dimension instanceSize = instanceLabel.getPreferredSize();

        Dimension newSize = typeSize.getUnioned(instanceSize);
        typeLabel.setSize(newSize);
        instanceLabel.setSize(newSize);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AtomicModel.TYPE_NAME.equals(evt.getPropertyName())) {
            typeLabel.setText(getModel().getType().getName());
            resizeLabel();
        } else if (AtomicModel.NAME.equals(evt.getPropertyName())) {
            instanceLabel.setText(getModel().getName());
            resizeLabel();
        }
        refresh();
    }

    public void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }

    @Override
    protected void performDoubleClick() {
        IComponentType container = (IComponentType) getModel().getType();
        BIPEditor.openBIPEditor(container);
    }
}
