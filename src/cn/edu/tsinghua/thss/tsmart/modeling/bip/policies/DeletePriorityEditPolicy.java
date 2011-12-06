package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeletePriorityCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;


public class DeletePriorityEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        DeletePriorityCommand command = new DeletePriorityCommand();
        command.setParent((PriorityAreaModel) getHost().getParent().getModel());
        command.setChild((PriorityModel) getHost().getModel());
        return command;
    }
}
