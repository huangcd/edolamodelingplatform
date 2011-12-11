package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.RelativeBendpointModel;

@SuppressWarnings("rawtypes")
public class CreateBendpointCommand extends BendpointCommand {
    @Override
    public void execute() {
        connection.addBendpoint(index,
                        new RelativeBendpointModel().setRelativeDimensions(dimension1, dimension2));
    }

    @Override
    public void undo() {
        connection.removeBendpoint(index);
    }
}
