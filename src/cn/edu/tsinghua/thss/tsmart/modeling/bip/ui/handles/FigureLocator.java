package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.handles.RelativeHandleLocator;

/**
 * ����һ��figure��λ��һ��figure��Locatior��ֻ֧�ֶ����ϡ��������ĸ�����Ķ�λ
 * 
 * @author Huangcd
 * 
 */
public class FigureLocator extends RelativeHandleLocator {
    private int     location;
    private int     gap;
    private IFigure target;
    private boolean refUseLocalCoordinates;

    public FigureLocator(IFigure reference, IFigure target, int location,
                    boolean refUseLocalCoordinates) {
        this(reference, target, location, 0, refUseLocalCoordinates);
    }

    public FigureLocator(IFigure reference, IFigure target, int location) {
        this(reference, target, location, 0, false);
    }

    public FigureLocator(IFigure reference, IFigure target, int location, int gap,
                    boolean refUseLocalCoordinates) {
        super(reference, location);
        this.target = target;
        this.location = location;
        this.gap = gap;
        this.refUseLocalCoordinates = refUseLocalCoordinates;
    }

    public FigureLocator(IFigure reference, IFigure target, int location, int gap) {
        this(reference, target, location, gap, false);
    }

    public FigureLocator resetLocation(int newLocation) {
        location = newLocation;
        return this;
    }

    /**
     * �ض�λ��ǩ
     * 
     * @param target ��ǩͼ��
     * @param refRect �ο�λ��
     */
    public void relocate(IFigure target, Rectangle refRect) {
        refRect = refRect.getCopy();
        if (refUseLocalCoordinates) {
            Point point = refRect.getLocation();
            System.out.println(point);
            getReferenceFigure().getParent().translateToParent(point);
            System.out.println(point);
            refRect.setLocation(point);
        }
        Dimension size = target.getPreferredSize();

        int x = target.getBounds().x;
        int y = target.getBounds().y;

        if (location == PositionConstants.NORTH) {
            x = refRect.x + refRect.width / 2 - size.width / 2;
            y = refRect.y - size.height - gap;
        } else if (location == PositionConstants.SOUTH) {
            x = refRect.x + refRect.width / 2 - size.width / 2;
            y = refRect.y + refRect.height + gap;
        } else if (location == PositionConstants.WEST) {
            x = refRect.x - size.width - gap;
            y = refRect.y + refRect.height / 2 - size.height / 2;
        } else if (location == PositionConstants.EAST) {
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
