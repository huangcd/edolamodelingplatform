package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;

@SuppressWarnings("rawtypes")
public class MoveBulletCommand extends MoveModelCommand {
    private BulletModel model;
    private Rectangle   constraint;
    private Rectangle   oldConstraint;
    private int         direction;
    private int         oldDirection;

    public void execute() {
        model.setDirection(direction);
        model.setPositionConstraint(constraint);
    }

    public void setConstraint(Rectangle rect) {
        this.oldConstraint = model.getPositionConstraint();
        this.constraint = rect;
    }

    public void setModel(BulletModel model) {
        this.model = model;
    }

    public void setDirection(int direction) {
        this.oldDirection = model.getDirection();
        this.direction = direction;
    }

    public void undo() {
        model.setDirection(oldDirection);
        model.setPositionConstraint(oldConstraint);
    }
}
