package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.*;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-26 上午12:15:20
 * @project CereusBip
 * @package cereusbip.policies
 * @class AtomicChildEditPolicy.java
 *        <p/>
 *        Atomic里面Place、DataArea等的创建和移动
 */
public class AtomicChildEditPolicy extends XYLayoutEditPolicy {
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        if (!(child.getModel() instanceof BaseModel)) {
            return null;
        }
        BaseModel model = (BaseModel) child.getModel();
        if (model instanceof PlaceModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        } else if (model instanceof DataAreaModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        } else if (model instanceof PriorityAreaModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        } else if (model instanceof PortAreaModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        } else if (model instanceof BulletModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            Rectangle rect = (Rectangle) constraint;
            AtomicTypeModel parent = ((AtomicTypeModel) ((BulletModel) model).getParent());
            Rectangle baseRect = parent.getPositionConstraint();
            int width = baseRect.width - 2 * BaseModel.BULLET_RADIUS;
            int height = baseRect.height - 2 * BaseModel.BULLET_RADIUS;
            int x = rect.x;
            int y = rect.y;
            Dimension titleBarSize = ((AtomicTypeEditPart) getHost()).getTitleSize();
            command.setConstraint(calculateBulletPosition(x, y, width, height, titleBarSize.width,
                            titleBarSize.height));
            return command;
        }
        return null;
    }

    private Rectangle calculateBulletPosition(int x, int y, int width, int height, int forbiddenX,
                    int forbiddenY) {
        forbiddenX += BaseModel.BULLET_RADIUS;
        forbiddenY += BaseModel.BULLET_RADIUS;
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
        } else {
            y = yPosition ? 0 : height;
        }
        if (x == 0 && y < forbiddenY) {
            y = forbiddenY;
        }
        if (y == 0 && x < forbiddenX) {
            x = forbiddenX;
        }
        return new Rectangle(x, y, 2 * BaseModel.BULLET_RADIUS, 2 * BaseModel.BULLET_RADIUS);
    }

    private final static Pattern namePattern = Pattern.compile("^PLACE(\\d*)$");

    private String getAppropriatePlaceName(AtomicTypeModel parent) {
        int maxNumber = 0;
        for (BaseModel model : parent.getChildren()) {
            if (model instanceof PlaceModel) {
                PlaceModel place = (PlaceModel) model;
                Matcher mat = namePattern.matcher(place.getName());
                if (mat.matches()) {
                    int number = Integer.parseInt(mat.group(1));
                    maxNumber = Math.max(number + 1, maxNumber);
                }
            }
        }
        return "PLACE" + maxNumber;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals(PlaceModel.class)) {
            CreatePlaceCommand command = new CreatePlaceCommand();
            AtomicTypeModel parent = (AtomicTypeModel) getHost().getModel();
            command.setParent(parent);
            PlaceModel child = new PlaceModel(getAppropriatePlaceName(parent), parent);
            Point location = request.getLocation().getCopy();
            // COMMENT 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        }
        return null;
    }

    public Command getCommand(Request request) {
        // COMMENT 不允许改变大小
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
