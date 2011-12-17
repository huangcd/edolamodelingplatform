package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveBulletCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;

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
            AtomicEditPart parent = (AtomicEditPart) getHost();
            Rectangle rect = (Rectangle) constraint;
            Point point = rect.getLocation();
            int direction = ensureInFrame(point, parent.getFigure().getBounds());
            rect.setLocation(point);
            MoveBulletCommand command = new MoveBulletCommand();
            command.setModel(model);
            command.setDirection(direction);
            command.setConstraint(rect);
            return command;
        }
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    /**
     * 确保bullet在方框之上
     * 
     * @param point bullet被拖动到的位置
     * @param componentBound component的范围
     * @return
     */
    protected int ensureInFrame(Point point, Rectangle componentBound) {
        int x = point.x;
        int y = point.y;
        int direction = PositionConstants.NORTH;
        int width = componentBound.width - 2 * IModel.BULLET_RADIUS;
        int height = componentBound.height - 2 * IModel.BULLET_RADIUS;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > width) x = width;
        if (y > height) y = height;
        boolean xPosition = x < width / 2;
        boolean yPosition = y < height / 2;
        int xValue = xPosition ? x : width - x;
        int yValue = yPosition ? y : height - y;
        if (xValue < yValue) {
            x = xPosition ? 0 : width;
            direction = xPosition ? PositionConstants.WEST : PositionConstants.EAST;
        } else {
            y = yPosition ? 0 : height;
            direction = yPosition ? PositionConstants.NORTH : PositionConstants.SOUTH;
        }
        point.setLocation(x, y);
        return direction;
    }

    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
