package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.draw2d.AbsoluteBendpoint;

public class MoveBendpointCommand extends BendpointCommand {
    AbsoluteBendpoint oldBendpoint;

    @Override
    public void execute() {
        oldBendpoint = (AbsoluteBendpoint) connection.getBendpoint(index);
        AbsoluteBendpoint bendpoint = new AbsoluteBendpoint(point);
        // bendpoint.setRelativeDimensions(dimension1, dimension2);
        connection.setBendpoint(index, bendpoint);
    }

    @Override
    public void undo() {
        connection.setBendpoint(index, oldBendpoint);
    }
}
