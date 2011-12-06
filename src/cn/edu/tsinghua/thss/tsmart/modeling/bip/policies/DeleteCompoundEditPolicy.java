package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteCompoundCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;


public class DeleteCompoundEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        if (getHost().getParent().getModel() instanceof CompoundTypeModel) {
            DeleteCompoundCommand command = new DeleteCompoundCommand();
            command.setParent((CompoundTypeModel) getHost().getParent().getModel());
            command.setChild((CompoundTypeModel) getHost().getModel());
            return command;
        }
        return null;
    }
}
