package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeletePlaceCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;


public class DeletePlaceEditPolicy extends ComponentEditPolicy {
    @Override
    protected Command createDeleteCommand(GroupRequest deleteRequest) {
        PlaceModel child = (PlaceModel) getHost().getModel();
        if (child.isInitialPlace()) return null;
        DeletePlaceCommand command = new DeletePlaceCommand();
        command.setParent((AtomicTypeModel) getHost().getParent().getModel());
        command.setChild(child);
        return command;
    }
}
