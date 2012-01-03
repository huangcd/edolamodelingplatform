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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ConnectorEditPart;

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
        if (type.equals(TransitionModel.class)) {
            CreateConnectionCommand command = new CreateConnectionCommand();
            command.setConnection((IConnection) request.getNewObject());
            command.setParent((IContainer) getHost().getParent().getModel());
            if (getHost().getModel() instanceof PlaceModel) {
                command.setSource((PlaceModel) getHost().getModel());
                request.setStartCommand(command);
                return command;
            }
            return null;
        }
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        if (getHost().getModel() instanceof BulletModel) {
            // 检查端口类型和connector参数类型是否一致
            BulletModel bullet = (BulletModel) getHost().getModel();
            ConnectionModel connection =
                            (ConnectionModel) request.getConnectionEditPart().getModel();
            if (!connection.getTypeName().equals(bullet.getPortTypeName())) {
                return null;
            }
            // 检查端口是否有同一组件的多个端口同时连接到一个connector的情况
            ConnectorModel connector = connection.getSource();
            int index = connection.getArgumentIndex();
            PortModel port = bullet.getPort();
            if (connector.portFromSameComponentAlreadyExists(port, index)) {
                return null;
            }
            // 如果validateOnTheFly失败，返回false
            if (!connector.availableToBound(index, port)) {
                return null;
            }
        }
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((IConnection) request.getConnectionEditPart().getModel());
        command.setNewTarget((IInstance) getHost().getModel());
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        if (getHost() instanceof ConnectorEditPart) {
            return null;
        }
        ReconnectConnectionCommand command = new ReconnectConnectionCommand();
        command.setConnection((IConnection) request.getConnectionEditPart().getModel());
        command.setNewSource((IInstance) getHost().getModel());
        return command;
    }
}
