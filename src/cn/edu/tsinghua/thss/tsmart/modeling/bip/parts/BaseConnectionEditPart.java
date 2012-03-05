package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureMouseListener;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public abstract class BaseConnectionEditPart extends AbstractConnectionEditPart
                implements
                    PropertyChangeListener {
    protected GlobalProperties properties = GlobalProperties.getInstance();

    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
    }

    protected abstract Point getSourceLocation();

    protected abstract Point getTargetLocation();

    protected Point getRelocateLocation(ArrayList<Bendpoint> bendpoints) {
        Point location;
        if (bendpoints.isEmpty()) {
            Point ref1 = getSourceLocation();
            Point ref2 = getTargetLocation();
            location = new Point((ref1.x + ref2.x) / 2, (ref1.y + ref2.y) / 2);
        } else if ((bendpoints.size() & 1) != 0) {
            location = bendpoints.get(bendpoints.size() / 2).getLocation().getCopy();
        } else {
            int index = (bendpoints.size() - 1) >> 1;
            Point ref1 = bendpoints.get(index).getLocation();
            Point ref2 = bendpoints.get(index + 1).getLocation();
            location = new Point((ref1.x + ref2.x) / 2, (ref1.y + ref2.y) / 2);
        }
        return location;
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
}
