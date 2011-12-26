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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.InvisibleBulletEditPart;

@SuppressWarnings("rawtypes")
public class CompoundChildrenEditPolicy extends XYLayoutEditPolicy {
    private final static Pattern componentNamePattern = Pattern.compile("^component(\\d*)$");
    private final static Pattern connectorNamePattern = Pattern.compile("^connector(\\d*)$");

    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child,
                    Object constraint) {
        if (child instanceof InvisibleBulletEditPart) {
            return null;
        }
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
    protected Command getCreateCommand(CreateRequest request) {
        CreateModelCommand command = new CreateModelCommand();
        Object obj = getHost().getModel();
        if (!(obj instanceof CompoundTypeModel)) return null;
        CompoundTypeModel parent = (CompoundTypeModel) obj;
        IInstance child = null;
        // Atomic
        if (request.getNewObjectType().equals(AtomicTypeModel.class)) {
            child =
                            new AtomicTypeModel().createInstance().setName(
                                            getAppropriateComponentName(parent));
            child.getType().setName(child.getName() + "Type");
        }
        // Compound
        else if (request.getNewObjectType().equals(CompoundTypeModel.class)) {
            child =
                            new CompoundTypeModel().createInstance().setName(
                                            getAppropriateComponentName(parent));
            child.getType().setName(child.getName() + "Type");
        }
        // Connector
        else if (request.getNewObjectType().equals(ConnectorTypeModel.class)) {
            child = ((ConnectorTypeModel) request.getNewObject()).getInstance();
            child.setName(getAppropriateConnectorName(parent));
        }
        Point location = request.getLocation().getCopy();
        // COMMENT œ‡∂‘Œª÷√
        getHostFigure().translateToRelative(location);
        getHostFigure().translateFromParent(location);
        Rectangle rect = new Rectangle(location, new Dimension(-1, -1));
        child.setPositionConstraint(rect);
        command.setChild(child);
        command.setParent(parent);
        return command;
    }

    private String getAppropriateComponentName(CompoundTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = componentNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "component" + maxNumber;
    }

    private String getAppropriateConnectorName(CompoundTypeModel parent) {
        int maxNumber = 0;
        for (IInstance model : parent.getChildren()) {
            Matcher mat = connectorNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "connector" + maxNumber;
    }

    @Override
    public Command getCommand(Request request) {
        if (REQ_RESIZE_CHILDREN.equals(request.getType())) return null;
        return super.getCommand(request);
    }
}
