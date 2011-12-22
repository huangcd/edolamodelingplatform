package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureLocator;

public class BulletEditPart extends BaseEditableEditPart {
    private Ellipse       figure;
    private Label         portName;
    private FigureLocator labelLocator;

    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
        ((GraphicalEditPart) getParent().getParent()).getFigure().remove(portName);
    }

    @Override
    protected IFigure createFigure() {
        if (figure == null) {
            figure = new Ellipse();
        }
        figure.setFill(true);
        figure.setForegroundColor(ColorConstants.black);
        figure.setBackgroundColor(ColorConstants.black);

        if (portName == null) {
            portName = new Label(getModel().getPortDescription());
        }
        portName.setFont(properties.getDefaultEditorFont());
        portName.setForegroundColor(ColorConstants.blue);
        addFigureMouseEvent(portName);

        ((GraphicalEditPart) getParent().getParent()).getFigure().add(portName);
        labelLocator = new FigureLocator(figure, portName, PositionConstants.NORTH, 3, true);
        // FIXME 第一次显示Bullet的时候将portName定位到正确的位置（现在需要拖动一下才能定位）
        // FIXME 拖动大框的时候标签没有随之改变位置
        // FIXME 下面语句会导致出错，考虑在BulletEditPart做translate而不是FigureLocator里面做
        // labelLocator.relocate(getModel().getPositionConstraint());
        return figure;
    }

    public BulletModel getModel() {
        return (BulletModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        String propertyName = evt.getPropertyName();
        if (IModel.CONSTRAINT.equals(propertyName)) {
            labelLocator.resetLocation(getModel().getDirection()).relocate(
                            (Rectangle) evt.getNewValue());
        } else if (IModel.NAME.equals(propertyName)) {
            portName.setText(getModel().getPortDescription());
        }
    }

    @Override
    protected void performDoubleClick() {}

    @Override
    protected void createEditPolicies() {}
}
