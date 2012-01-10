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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.MoveModelCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AtomicTypeChildrenEditPolicy extends XYLayoutEditPolicy {

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        CreateModelCommand command = new CreateModelCommand();
        Object obj = getHost().getModel();
        if (!(obj instanceof AtomicTypeModel)) return null;
        AtomicTypeModel parent = (AtomicTypeModel) obj;
        command.setParent(parent);
        if (request.getNewObjectType().equals(PlaceModel.class)) {
            PlaceModel child = new PlaceModel();
            child.setName("place0");
            Point location = request.getLocation().getCopy();
            // 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        } else if (request.getNewObjectType().equals(DataTypeModel.class)) {
            DataModel<AtomicTypeModel> child =
                            ((DataTypeModel<AtomicTypeModel>) request.getNewObject()).getInstance();
            child.setName("data0");
            Point location = request.getLocation().getCopy();
            // 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        } else if (request.getNewObjectType().equals(PortTypeModel.class)) {
            PortModel child = ((PortTypeModel) request.getNewObject()).getInstance();
            child.setName("port0");
            Point location = request.getLocation().getCopy();
            // 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        }
        return null;
    }

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

    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
