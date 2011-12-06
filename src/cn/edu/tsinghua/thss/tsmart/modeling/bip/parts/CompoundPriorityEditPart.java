package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CustomDirectEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteCompoundPriorityEditPolicy;


public class CompoundPriorityEditPart extends BaseEditableEditPart {
    private Label label;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        label.setText(getModel().toString());
        refreshVisuals();
    }

    @Override
    protected IFigure createFigure() {
        label = new Label(getModel().toString());
        LineBorder inner = new LineBorder(ColorConstants.lightBlue, 1);
        // MarginBorder outer = new MarginBorder(3);
        label.setBorder(inner);
        return label;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteCompoundPriorityEditPolicy());
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CustomDirectEditPolicy()); // Ö±½Ó±à¼­
    }

    // Ë«»÷±à¼­
    @Override
    protected void performDoubleClick() {
        // TODO Ë«»÷±à¼­
    }
}
