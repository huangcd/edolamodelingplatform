package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceTypeModel;

@SuppressWarnings("rawtypes")
public class AtomicChildrenEditPolicy extends XYLayoutEditPolicy {
    private final static Pattern namePattern = Pattern.compile("^PLACE(\\d*)$");

    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        if (child.getModel() instanceof IModel) {
            IModel model = (IModel) child.getModel();
            MoveModelCommand command = new MoveModelCommand();
            command.setModel(model);
            command.setConstraint((Rectangle) constraint);
            return command;
        }
        return super.createChangeConstraintCommand(request, child, constraint);
    }

    private String getAppropriatePlaceName(AtomicTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
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
        System.out.println(request.getNewObject());
        if (request.getNewObjectType().equals(PlaceModel.class)) {
            CreateModelCommand command = new CreateModelCommand();
            Object obj = getHost().getModel();
            if (!(obj instanceof AtomicTypeModel)) return null;
            AtomicTypeModel parent = (AtomicTypeModel) obj;
            PlaceModel child =
                            new PlaceTypeModel().createInstance().setName(
                                            getAppropriatePlaceName(parent));
            Point location = request.getLocation().getCopy();
            // COMMENT 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            command.setParent(parent);
            return command;
        }
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Command getCommand(Request request) {
        // 不允许改变Atomic内部元素的大小
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }

}
