package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;

@SuppressWarnings("rawtypes")
public class DeleteModelEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        DeleteModelCommand command = new DeleteModelCommand();
        command.setParent((IContainer) getHost().getParent().getModel());
        command.setChild((IInstance) getHost().getModel());
        return command;
    }
}
