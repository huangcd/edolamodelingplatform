package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.BendpointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.CreateBendpointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.DeleteBendpointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection.MoveBendpointCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;

@SuppressWarnings("rawtypes")
public class ConnectionBendpointEditPolicy extends BendpointEditPolicy {

    @Override
    protected Command getCreateBendpointCommand(BendpointRequest request) {
        BendpointCommand command = new CreateBendpointCommand();

        Point location = request.getLocation();

        Connection conn = getConnection();
        conn.translateToRelative(location);

        Point ref1 = conn.getSourceAnchor().getReferencePoint();
        Point ref2 = conn.getTargetAnchor().getReferencePoint();

        conn.translateToRelative(ref1);
        conn.translateToRelative(ref2);

        command.setDimension1(location.getDifference(ref1));
        command.setDimension2(location.getDifference(ref2));
        command.setPoint(location);

        command.setConnection((IConnection) getHost().getModel());
        command.setIndex(request.getIndex());
        return command;
    }

    @Override
    protected Command getDeleteBendpointCommand(BendpointRequest request) {
        BendpointCommand command = new DeleteBendpointCommand();
        command.setConnection((IConnection) getHost().getModel());
        command.setIndex(request.getIndex());
        return command;
    }

    @Override
    protected Command getMoveBendpointCommand(BendpointRequest request) {
        BendpointCommand command = new MoveBendpointCommand();
        Point location = request.getLocation();
        Connection conn = getConnection();
        conn.translateToRelative(location);

        Point ref1 = conn.getSourceAnchor().getReferencePoint();
        Point ref2 = conn.getTargetAnchor().getReferencePoint();

        conn.translateToRelative(ref1);
        conn.translateToRelative(ref2);

        command.setDimension1(location.getDifference(ref1));
        command.setDimension2(location.getDifference(ref2));

        command.setConnection((IConnection) getHost().getModel());
        command.setIndex(request.getIndex());
        command.setPoint(location);
        return command;
    }

}
