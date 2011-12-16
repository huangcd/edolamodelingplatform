package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;

public class BulletEditPart extends BaseEditableEditPart {
    private Ellipse figure;

    @Override
    protected IFigure createFigure() {
        figure = new Ellipse();
        figure.setFill(true);
        figure.setForegroundColor(ColorConstants.black);
        figure.setBackgroundColor(ColorConstants.black);
        return figure;
    }

    public BulletModel getModel() {
        return (BulletModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (IModel.CONSTRAINT.equals(propertyName)) {
            refreshVisuals();
        }
        refreshVisuals();
    }

    @Override
    protected void performDoubleClick() {}

    @Override
    protected void createEditPolicies() {
        // TODO Auto-generated method stub
    }

}
