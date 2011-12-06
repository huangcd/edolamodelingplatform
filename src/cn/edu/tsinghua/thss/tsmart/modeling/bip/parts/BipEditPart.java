package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BipModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.BipChildEditPolicy;


public class BipEditPart extends BaseGraphicalEditPart {
    public BipEditPart() {}

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (BipModel.CHILDREN.equals(evt.getPropertyName())) {
            refresh();
        } else {
            refreshVisuals();
        }
    }

    @Override
    protected IFigure createFigure() {
        FreeformLayer figure = new FreeformLayer();
        figure.setLayoutManager(new FreeformLayout());
        return figure;
    }

    @SuppressWarnings("rawtypes")
    protected List getModelChildren() {
        return ((BipModel) getModel()).getChildren();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new BipChildEditPolicy());
    }
}
