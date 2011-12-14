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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class AtomicChildrenEditPolicy extends XYLayoutEditPolicy {
    private final static Pattern placeNamePattern = Pattern.compile("^PLACE(\\d*)$");
    private final static Pattern dataNamePattern  = Pattern.compile("^data(\\d*)$");
    private final static Pattern portNamePattern  = Pattern.compile("^port(\\d*)$");

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        CreateModelCommand command = new CreateModelCommand();
        Object obj = getHost().getModel();
        if (!(obj instanceof AtomicTypeModel)) return null;
        AtomicTypeModel parent = (AtomicTypeModel) obj;
        command.setParent(parent);
        if (request.getNewObjectType().equals(PlaceTypeModel.class)) {
            PlaceModel child = new PlaceTypeModel().createInstance();
            child.setName(getAppropriatePlaceName(parent));
            Point location = request.getLocation().getCopy();
            // 相对位置
            getHostFigure().translateToRelative(location);
            getHostFigure().translateFromParent(location);
            Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
            child.setPositionConstraint(rect);
            command.setChild(child);
            return command;
        } else if (request.getNewObjectType().equals(DataTypeModel.class)) {
            // TODO 判断getNewObject().getTypeName()是否为空，如果是空的话，弹出对话框提示用户设置
            DataModel<AtomicTypeModel> child =
                            ((DataTypeModel<AtomicTypeModel>) request.getNewObject()).getInstance();
            child.setName(getAppropriateDataName(parent));
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
            child.setName(getAppropriatePortName(parent));
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

    private String getAppropriatePlaceName(AtomicTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = placeNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "PLACE" + maxNumber;
    }

    private String getAppropriateDataName(AtomicTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = dataNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "data" + maxNumber;
    }

    private String getAppropriatePortName(AtomicTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = portNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "port" + maxNumber;
    }

    @Override
    public Command getCommand(Request request) {
        // 不允许改变Atomic内部元素的大小
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
