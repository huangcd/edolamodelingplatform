package cn.edu.tsinghua.thss.tsmart.modeling.ui.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.RelativeHandleLocator;

public class FigureLocator extends RelativeHandleLocator {
    private int     location;
    private IFigure target;

    public FigureLocator(IFigure reference, IFigure target, int location) {
        super(reference, location);
        this.target = target;
        this.location = location;
    }

    /**
     * 重定位标签 TODO：根据location计算新的位置
     * 
     * @param target 标签图像
     * @param refRect 参考位置
     */
    public void relocate(IFigure target, Rectangle refRect) {
        Dimension size = target.getPreferredSize();
        Point location =
                        new Point(refRect.x + refRect.width / 2 - size.width / 2, refRect.y
                                        - size.height);
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
