package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateBendPointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DeleteBendPointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveBendPointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ConnectorPortEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TransitionEditPart;


/**
 * Created by Huangcd Date: 11-9-15 Time: ÏÂÎç8:32
 */
public class ConnectorBendPointEditPolicy extends BendpointEditPolicy {
    @Override
    protected Command getCreateBendpointCommand(BendpointRequest bendpointRequest) {
        CreateBendPointCommand command = new CreateBendPointCommand();
        command.setIndex(bendpointRequest.getIndex());
        command.setLocation(bendpointRequest.getLocation());
        ConnectionEditPart source = bendpointRequest.getSource();
        if (source instanceof ConnectorPortEditPart || source instanceof TransitionEditPart) {
            command.setOwner((BaseConnectionModel) (source).getModel());
            return command;
        }
        return null;
    }

    @Override
    protected Command getDeleteBendpointCommand(BendpointRequest bendpointRequest) {
        DeleteBendPointCommand command = new DeleteBendPointCommand();
        command.setIndex(bendpointRequest.getIndex());
        ConnectionEditPart source = bendpointRequest.getSource();
        if (source instanceof ConnectorPortEditPart || source instanceof TransitionEditPart) {
            command.setOwner((BaseConnectionModel) (source).getModel());
            return command;
        }
        return null;
    }

    @Override
    protected Command getMoveBendpointCommand(BendpointRequest bendpointRequest) {
        MoveBendPointCommand command = new MoveBendPointCommand();
        command.setIndex(bendpointRequest.getIndex());
        command.setNewLocation(bendpointRequest.getLocation());
        ConnectionEditPart source = bendpointRequest.getSource();
        if (source instanceof ConnectorPortEditPart || source instanceof TransitionEditPart) {
            command.setOwner((BaseConnectionModel) (source).getModel());
            return command;
        }
        return null;
    }
}
