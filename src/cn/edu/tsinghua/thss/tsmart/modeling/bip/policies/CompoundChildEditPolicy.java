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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateAtomicCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateCompoundCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateConnectorCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;


/**
 * 
 * @author : Huangcd (huangcd.thu@gmail.com)
 * @time : 2011-7-8 下午11:59:56
 * @project : CereusBip
 * @package : cereusbip.policies
 * @class : BipChildEditPolicy.java
 * 
 */
public class CompoundChildEditPolicy extends XYLayoutEditPolicy {
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        BaseModel model = (BaseModel) child.getModel();
        if (model instanceof AtomicTypeModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            Rectangle rect = (Rectangle) constraint;
            int width = rect.width;
            int height = rect.height;
            // minimize size of Atomic should be 400 * 400
            width = Math.max(400, width);
            height = Math.max(400, height);
            command.setConstraint(new Rectangle(rect.x, rect.y, width, height));
            return command;
        } else if (model instanceof CompoundPriorityAreaModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        } else if (model instanceof CompoundTypeModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            Rectangle rect = (Rectangle) constraint;
            int width = rect.width;
            int height = rect.height;
            // minimize size of Atomic should be 400 * 400
            width = Math.max(600, width);
            height = Math.max(600, height);
            command.setConstraint(new Rectangle(rect.x, rect.y, width, height));
            return command;
        } else if (model instanceof ConnectorTypeModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            Rectangle rect = (Rectangle) constraint;
            Point location = rect.getLocation();
            Dimension size = new Dimension(30, 30);
            command.setConstraint(new Rectangle(location, size));
            return command;
        } else if (model instanceof BulletModel) {
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            Rectangle rect = (Rectangle) constraint;
            CompoundTypeModel parent = ((CompoundTypeModel) ((BulletModel) model).getParent());
            Rectangle baseRect = parent.getPositionConstraint();
            int width = baseRect.width - 2 * BaseModel.BULLET_RADIUS;
            int height = baseRect.height - 2 * BaseModel.BULLET_RADIUS;
            int x = rect.x;
            int y = rect.y;
            Dimension titleBarSize = ((CompoundTypeEditPart) getHost()).getTitleSize();
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

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals(AtomicTypeModel.class)) {
            CreateAtomicCommand command = new CreateAtomicCommand();
            command.setParent((CompoundTypeModel) getHost().getModel());
            AtomicTypeModel child = new AtomicTypeModel("Atomic");
            Point location = request.getLocation().getCopy();
            // COMMENT 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(500, 400));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        }
        if (request.getNewObjectType().equals(CompoundTypeModel.class)) {
            CreateCompoundCommand command = new CreateCompoundCommand();
            command.setParent((CompoundTypeModel) getHost().getModel());
            CompoundTypeModel child = new CompoundTypeModel("Compound");
            Point location = request.getLocation().getCopy();
            // COMMENT 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(600, 500));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        } else if (request.getNewObjectType().equals(ConnectorTypeModel.class)) {
            CreateConnectorCommand command = new CreateConnectorCommand();
            command.setParent((CompoundTypeModel) getHost().getModel());
            ConnectorTypeModel child = new ConnectorTypeModel();
            Point location = request.getLocation().getCopy();
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(30, 30));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        }
        return null;
    }

    public Command getCommand(Request request) {
        return super.getCommand(request);
    }
}
