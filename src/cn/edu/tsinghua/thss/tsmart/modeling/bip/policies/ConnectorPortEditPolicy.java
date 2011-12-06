package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateConnectorPortCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;


/**
 * 
 * @author Huangcd
 * 
 */
public class ConnectorPortEditPolicy extends GraphicalNodeEditPolicy {
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        Object model = getHost().getModel();
        if (model instanceof BulletModel) {
            CreateConnectorPortCommand command =
                            (CreateConnectorPortCommand) request.getStartCommand();
            ConnectorTypeModel source = command.getSource();
            BulletModel target = (BulletModel) model;
            BaseModel targetParent = target.getParent();
            CompoundTypeModel targetAncestor = null;
            if (targetParent instanceof AtomicTypeModel) {
                targetAncestor = ((AtomicTypeModel) targetParent).getParent();
            } else if (targetParent instanceof CompoundTypeModel
                            && ((CompoundTypeModel) targetParent).getParent() instanceof CompoundTypeModel) {
                targetAncestor = (CompoundTypeModel) ((CompoundTypeModel) targetParent).getParent();
            } else {
                new Exception(targetParent.toString()).printStackTrace();
                return null;
            }
            if (!targetAncestor.equals(source.getParent())) {
                return null;
            }
            command.setTarget(target);
            return command;
        }
        return null;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        Object source = getHost().getModel();
        if (source instanceof ConnectorTypeModel
                        && request.getNewObject() instanceof ConnectorPortModel) {
            CreateConnectorPortCommand command = new CreateConnectorPortCommand();
            command.setConnection((ConnectorPortModel) request.getNewObject());
            command.setSource((ConnectorTypeModel) source);
            command.setParent((CompoundTypeModel) getHost().getParent().getModel());
            request.setStartCommand(command);
            return command;
        }
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        return null;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        return null;
    }

}
