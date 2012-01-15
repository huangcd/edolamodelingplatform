package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteComponentCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteConnectorCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeletePlaceCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;

@SuppressWarnings("rawtypes")
public class DeleteModelEditPolicy extends ComponentEditPolicy {

    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        IModel model = (IModel) getHost().getModel();
        IModel parentModel = (IModel) getHost().getParent().getModel();
        // 删除Place和相关的Transition
        if (model instanceof PlaceModel) {
            DeletePlaceCommand command = new DeletePlaceCommand();
            if (parentModel instanceof AtomicTypeModel)
                command.setParent((AtomicTypeModel) parentModel);
            else if (parentModel instanceof AtomicModel)
                command.setParent(((AtomicModel) parentModel).getType());
            command.setChild((PlaceModel) model);
            return command;
        }
        // 删除Connector和相关连线
        if (model instanceof ConnectorModel) {
            DeleteConnectorCommand command = new DeleteConnectorCommand();
            command.setParent((CompoundTypeModel) parentModel);
            command.setChild((ConnectorModel) model);
            return command;
        }
        // 删除Component和相关连线
        if (model instanceof ComponentModel) {
            DeleteComponentCommand command = new DeleteComponentCommand();
            command.setParent((CompoundTypeModel) parentModel);
            command.setChild((ComponentModel) model);
            return command;
        }
        if (!(parentModel instanceof IContainer)) {
            return null;
        }
        DeleteModelCommand command = new DeleteModelCommand();
        command.setParent((IContainer) parentModel);
        command.setChild(model);
        return command;
    }
}
