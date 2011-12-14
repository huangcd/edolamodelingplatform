package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;

@SuppressWarnings("rawtypes")
public abstract class BendpointCommand extends Command {
    protected int         index;
    protected IConnection connection;
    protected Point       point;
    protected Dimension   dimension1;
    protected Dimension   dimension2;

    public void setIndex(int index) {
        this.index = index;
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }

    public void setDimension1(Dimension dimension1) {
        this.dimension1 = dimension1;
    }

    public void setDimension2(Dimension dimension2) {
        this.dimension2 = dimension2;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

}
