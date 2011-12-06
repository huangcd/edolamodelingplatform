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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateCompoundCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BipModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;


/**
 * 
 * @author: Huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-8 下午11:59:56
 * @project: CereusBip
 * @package: cereusbip.policies
 * @class: BipChildEditPolicy.java
 * 
 */
public class BipChildEditPolicy extends XYLayoutEditPolicy {
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        BaseModel model = (BaseModel) child.getModel();
        if (model instanceof CompoundTypeModel) {
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
        }
        return null;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        BipModel model = (BipModel) getHost().getModel();
        if (model.getChildren().size() >= 1) return null;
        if (request.getNewObjectType().equals(CompoundTypeModel.class)) {
            CreateCompoundCommand command = new CreateCompoundCommand();
            command.setParent(model);
            CompoundTypeModel child = new CompoundTypeModel("Compound");
            Point location = request.getLocation().getCopy();
            // COMMENT 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(600, 500));
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
