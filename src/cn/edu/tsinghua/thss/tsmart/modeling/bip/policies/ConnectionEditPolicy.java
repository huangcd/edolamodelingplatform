package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.CreateConnectionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.ReconnectConnectionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionTypeModel;

@SuppressWarnings("rawtypes")
public class ConnectionEditPolicy extends GraphicalNodeEditPolicy {
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        CreateConnectionCommand command = (CreateConnectionCommand) request.getStartCommand();
        IInstance target = (IInstance) getHost().getModel();
        if (target instanceof BulletModel || target instanceof PlaceModel) {
            command.setTarget(target);
            return command;
        }
        return null;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        Object type = request.getNewObjectType();
        if (type.equals(TransitionTypeModel.class)) {
            CreateConnectionCommand command = new CreateConnectionCommand();
            command.setConnection((IConnection) ((IType) request.getNewObject()).getInstance());
            command.setParent((IContainer) getHost().getParent().getModel());
            if (getHost().getModel() instanceof PlaceModel) {
                command.setSource((PlaceModel) getHost().getModel());
                request.setStartCommand(command);
                return command;
            }
            return null;
        } else if (type.equals(ConnectionTypeModel.class)) {
            CreateConnectionCommand command = new CreateConnectionCommand();
            command.setConnection((IConnection) ((IType) request.getNewObject()).getInstance());
            //command.setParent((IContainer) getHost().getParent().getModel());
            if (getHost().getModel() instanceof ConnectorModel) {
                command.setSource((IInstance) getHost().getModel());
                request.setStartCommand(command);
                return command;
            }
            return null;
        }
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((IConnection) request.getConnectionEditPart().getModel());
        command.setNewTarget((IInstance) getHost().getModel());
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((IConnection) request.getConnectionEditPart().getModel());
        command.setNewSource((IInstance) getHost().getModel());
        return command;
    }
}