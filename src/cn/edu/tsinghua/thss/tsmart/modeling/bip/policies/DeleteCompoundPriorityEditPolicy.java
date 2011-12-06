package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteCompoundPriorityCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityModel;


public class DeleteCompoundPriorityEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        DeleteCompoundPriorityCommand command = new DeleteCompoundPriorityCommand();
        command.setParent((CompoundPriorityAreaModel) getHost().getParent().getModel());
        command.setChild((CompoundPriorityModel) getHost().getModel());
        return command;
    }
}
