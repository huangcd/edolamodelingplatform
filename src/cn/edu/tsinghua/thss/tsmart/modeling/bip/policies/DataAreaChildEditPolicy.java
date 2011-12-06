package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateDataCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 上午12:15:20
 * @project: CereusBip
 * @package: cereusbip.policies
 * @class: DataAreaChildEditPolicy.java
 * 
 *         Atomic里面Place、DataArea等的创建和移动
 */
public class DataAreaChildEditPolicy extends FlowLayoutEditPolicy {

    @Override
    protected Command createAddCommand(EditPart child, EditPart after) {
        return null;
    }

    @Override
    protected Command createMoveChildCommand(EditPart child, EditPart after) {
        return null;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    @Override
    public Command getCommand(Request request) {
        if (CreateDataCommand.CREATE_DATA_COMMAND.equals(request.getType())) {
            CreateDataCommand command = new CreateDataCommand();
            DataModel child = new DataModel();
            command.setChild(child);
            command.setParent((DataAreaModel) getHost().getModel());
            return command;
        }
        return super.getCommand(request);
    }
}
