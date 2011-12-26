package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.RelativeHandleLocator;

/**
 * 根据一个figure定位另一个figure的Locatior，只支持东、南、西、北四个方向的定位
 * 
 * @author Huangcd
 * 
 */
public class FigureLocator extends RelativeHandleLocator {
    private int     direction;
    private int     gap;
    private IFigure target;

    public FigureLocator(IFigure reference, IFigure target, int location) {
        this(reference, target, location, 0);
    }

    public FigureLocator(IFigure reference, IFigure target, int direction, int gap) {
        super(reference, direction);
        this.target = target;
        this.direction = direction;
        this.gap = gap;
    }

    public FigureLocator resetDirection(int newDirection) {
        if (newDirection == PositionConstants.NORTH || newDirection == PositionConstants.SOUTH
                        || newDirection == PositionConstants.EAST
                        || newDirection == PositionConstants.WEST) {
            direction = newDirection;
        }
        return this;
    }

    /**
     * 重定位标签
     * 
     * @param target 标签图像
     * @param refRect 参考位置
     */
    public void relocate(IFigure target, Rectangle refRect) {
        refRect = refRect.getCopy();
        Dimension size = target.getPreferredSize();

        int x = target.getBounds().x;
        int y = target.getBounds().y;

        if (direction == PositionConstants.NORTH) {
            x = refRect.x + refRect.width / 2 - size.width / 2;
            y = refRect.y - size.height - gap;
        } else if (direction == PositionConstants.SOUTH) {
            x = refRect.x + refRect.width / 2 - size.width / 2;
            y = refRect.y + refRect.height + gap;
        } else if (direction == PositionConstants.WEST) {
            x = refRect.x - size.width - gap;
            y = refRect.y + refRect.height / 2 - size.height / 2;
        } else if (direction == PositionConstants.EAST) {
            x = refRect.x + refRect.width + gap;
            y = refRect.y + refRect.height / 2 - size.height / 2;
        }

        Point location = new Point(x, y);
        target.setBounds(new Rectangle(location, size));
    }

    public void relocate() {
        relocate(target, getReferenceFigure().getBounds());
    }

    public void relocate(Rectangle refRect) {
        relocate(target, refRect);
    }

    public void relocate(IFigure target) {
        relocate(target, getReferenceFigure().getBounds());
    }
}
