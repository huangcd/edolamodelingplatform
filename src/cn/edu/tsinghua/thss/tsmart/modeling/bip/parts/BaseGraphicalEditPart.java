package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureMouseListener;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public abstract class BaseGraphicalEditPart extends AbstractGraphicalEditPart
                implements
                    PropertyChangeListener {
    protected GlobalProperties properties = GlobalProperties.getInstance();

    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
    }

    public IModel getModel() {
        return (IModel) super.getModel();
    }

    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }

    protected void addFigureMouseEvent(IFigure figure) {
        FigureMouseListener listener = new FigureMouseListener(figure);
        figure.addMouseListener(listener);
        figure.addMouseMotionListener(listener);
    }

    public GraphicalEditPart getParent() {
        return (GraphicalEditPart) super.getParent();
    }

    @Override
    protected void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }
}
