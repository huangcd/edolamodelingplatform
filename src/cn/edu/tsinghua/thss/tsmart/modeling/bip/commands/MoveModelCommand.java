package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public class MoveModelCommand extends Command {
    private IModel    model;
    private Rectangle constraint;
    private Rectangle oldConstraint;

    public void execute() {
        model.setPositionConstraint(constraint);
    }

    public void setConstraint(Rectangle rect) {
        this.oldConstraint = model.getPositionConstraint();
        this.constraint = rect;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    public void undo() {
        model.setPositionConstraint(oldConstraint);
    }
}
