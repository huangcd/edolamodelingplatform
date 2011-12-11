package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.RelativeBendpointModel;

public class MoveBendpointCommand extends BendpointCommand {
    RelativeBendpointModel oldBendpoint;

    @Override
    public void execute() {
        oldBendpoint = connection.getBendpoint(index);
        connection.setBendpoint(index,
                        new RelativeBendpointModel().setRelativeDimensions(dimension1, dimension2));
    }

    @Override
    public void undo() {
        connection.setBendpoint(index, oldBendpoint);
    }
}
