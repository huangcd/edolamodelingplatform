package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseConnectionModel;


/**
 * Created by Huangcd Date: 11-9-15 Time: ÏÂÎç8:34
 */
public class DeleteBendPointCommand extends Command {
    private BaseConnectionModel owner;
    private Point               location;
    private int                 index;

    public void setOwner(BaseConnectionModel owner) {
        this.owner = owner;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void execute() {
        setLocation(owner.getBendPoints().get(index));
        owner.removeBendPoint(index);
    }

    @Override
    public void undo() {
        owner.addBendPoint(index, location);
    }
}
