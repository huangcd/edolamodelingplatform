package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveBulletCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ComponentEditPart;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ComponentChildrenEditPolicy extends XYLayoutEditPolicy {

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        return null;
    }

    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        if (child.getModel() instanceof BulletModel) {
            BulletModel model = (BulletModel) child.getModel();
            ComponentEditPart parent = (ComponentEditPart) getHost();
            Rectangle rect = (Rectangle) constraint;
            Point point = rect.getLocation();
            int direction = parent.getModel().ensureInFrame(point);
            rect.setLocation(point);
            MoveBulletCommand command = new MoveBulletCommand();
            command.setModel(model);
            command.setDirection(direction);
            command.setConstraint(rect);
            return command;
        }
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
