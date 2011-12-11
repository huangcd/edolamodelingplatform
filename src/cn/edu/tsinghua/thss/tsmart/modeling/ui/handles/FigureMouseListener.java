package cn.edu.tsinghua.thss.tsmart.modeling.ui.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;

public class FigureMouseListener implements MouseListener, MouseMotionListener {
    private int     x;
    private int     y;
    private IFigure figure;

    public FigureMouseListener(IFigure figure) {
        this.figure = figure;
    }

    @Override
    public void mousePressed(MouseEvent me) {
        x = me.x;
        y = me.y;
        me.consume();
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        me.consume();
    }

    @Override
    public void mouseDoubleClicked(MouseEvent me) {}

    @Override
    public void mouseDragged(MouseEvent me) {
        Point location = figure.getBounds().getLocation();
        location.setX(location.x() + me.x - x);
        location.setY(location.y() + me.y - y);
        x = me.x;
        y = me.y;
        figure.setLocation(location);
        me.consume();
    }

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void mouseHover(MouseEvent me) {}

    @Override
    public void mouseMoved(MouseEvent me) {}

}
