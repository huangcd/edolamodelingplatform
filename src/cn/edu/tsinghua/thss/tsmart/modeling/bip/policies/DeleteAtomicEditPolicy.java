package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteAtomicCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;


public class DeleteAtomicEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        DeleteAtomicCommand command = new DeleteAtomicCommand();
        command.setParent((CompoundTypeModel) getHost().getParent().getModel());
        command.setChild((AtomicTypeModel) getHost().getModel());
        return command;
    }
}
