package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteConnectorCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorTypeModel;


public class DeleteConnectorEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        ConnectorTypeModel child = (ConnectorTypeModel) getHost().getModel();
        DeleteConnectorCommand command = new DeleteConnectorCommand();
        command.setParent((CompoundTypeModel) getHost().getParent().getModel());
        command.setChild(child);
        return command;
    }
}
