package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.RelativeBendpointModel;

public class DeleteBendpointCommand extends BendpointCommand {
    RelativeBendpointModel oldBendpoint;
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
