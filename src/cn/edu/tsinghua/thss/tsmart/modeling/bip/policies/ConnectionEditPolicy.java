package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.DeleteTransitionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TransitionEditPart;

public class ConnectionEditPolicy extends org.eclipse.gef.editpolicies.ConnectionEditPolicy {

    @Override
    protected Command getDeleteCommand(GroupRequest request) {
        if (getHost() instanceof TransitionEditPart) {
            DeleteTransitionCommand command = new DeleteTransitionCommand();
            command.setTransition((TransitionModel) getHost().getModel());
            return command;
        }
        // TODO Auto-generated method stub
        return null;
    }
}
