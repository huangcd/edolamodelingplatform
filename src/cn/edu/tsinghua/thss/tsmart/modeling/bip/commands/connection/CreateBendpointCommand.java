package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.draw2d.AbsoluteBendpoint;

@SuppressWarnings("rawtypes")
public class CreateBendpointCommand extends BendpointCommand {
    @Override
    public void execute() {
        AbsoluteBendpoint bendpoint = new AbsoluteBendpoint(point);
        //bendpoint.setRelativeDimensions(dimension1, dimension2);
        connection.addBendpoint(index, bendpoint);
    }

    @Override
    public void undo() {
        connection.removeBendpoint(index);
    }
}
