package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.draw2d.Bendpoint;

public class DeleteBendpointCommand extends BendpointCommand {
    Bendpoint oldBendpoint;
    @Override
    public void execute() {
        oldBendpoint = connection.getBendpoint(index);
        connection.removeBendpoint(index);
    }

    @Override
    public void undo() {
        connection.addBendpoint(index, oldBendpoint);
    }
}
