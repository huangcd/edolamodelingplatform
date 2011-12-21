package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.DeleteTransitionCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TransitionEditPart;

public class BIPConnectionEditPolicy extends ConnectionEditPolicy {

    @Override
    protected Command getDeleteCommand(GroupRequest request) {
        if (getHost() instanceof TransitionEditPart) {
            DeleteTransitionCommand command = new DeleteTransitionCommand();
            command.setTransition((TransitionModel) getHost().getModel());
            return command;
        }
        return null;
    }
}
