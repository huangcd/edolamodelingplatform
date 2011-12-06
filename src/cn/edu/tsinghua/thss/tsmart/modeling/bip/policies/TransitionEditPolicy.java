package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateTransitionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.ReconnectTransitionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.TransitionModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-27 ÏÂÎç02:59:13
 * @project: CereusBip
 * @package: cereusbip.policies
 * @class: TransitionEditPolicy.java
 * 
 */
public class TransitionEditPolicy extends GraphicalNodeEditPolicy {
    @Override
    protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
        CreateTransitionCommand command = (CreateTransitionCommand) request.getStartCommand();
        PlaceModel source = command.getSource();
        PlaceModel target = (PlaceModel) (getHost().getModel());
        if (!source.getParent().equals(target.getParent())) {
            return null;
        }
        command.setTarget(target);
        return command;
    }

    @Override
    protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
        Object newObject = request.getNewObject();
        if (newObject instanceof TransitionModel) {
            CreateTransitionCommand command = new CreateTransitionCommand();
            command.setConnection((TransitionModel) request.getNewObject());
            command.setSource((PlaceModel) getHost().getModel());
            command.setParent((AtomicTypeModel) getHost().getParent().getModel());
            request.setStartCommand(command);
            return command;
        }
        return null;
    }

    @Override
    protected Command getReconnectTargetCommand(ReconnectRequest request) {
        ReconnectTransitionCommand command = new ReconnectTransitionCommand();
        command.setConnection((TransitionModel) request.getConnectionEditPart().getModel());
        command.setNewTarget((PlaceModel) getHost().getModel());
        return command;
    }

    @Override
    protected Command getReconnectSourceCommand(ReconnectRequest request) {
        ReconnectTransitionCommand command = new ReconnectTransitionCommand();
        command.setConnection((TransitionModel) request.getConnectionEditPart().getModel());
        command.setNewSource((PlaceModel) getHost().getModel());
        return command;
    }
}
