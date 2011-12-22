package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.CreateConnectionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.ReconnectConnectionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionTypeModel;

public class TransitionEditPolicy extends GraphicalNodeEditPolicy {
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        CreateConnectionCommand command = (CreateConnectionCommand) request.getStartCommand();
        PlaceModel target = (PlaceModel) getHost().getModel();
        command.setTarget(target);
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        Object type = request.getNewObjectType();
        if (type.equals(TransitionTypeModel.class)) {
            CreateConnectionCommand command = new CreateConnectionCommand();
            command.setConnection(((TransitionTypeModel) request.getNewObject()).getInstance());
            command.setSource((PlaceModel) getHost().getModel());
            command.setParent((AtomicTypeModel) getHost().getParent().getModel());
            request.setStartCommand(command);
            return command;
        }
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((TransitionModel) request.getConnectionEditPart().getModel());
        command.setNewTarget((PlaceModel) getHost().getModel());
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((TransitionModel) request.getConnectionEditPart().getModel());
        command.setNewSource((PlaceModel) getHost().getModel());
        return command;
    }
}
