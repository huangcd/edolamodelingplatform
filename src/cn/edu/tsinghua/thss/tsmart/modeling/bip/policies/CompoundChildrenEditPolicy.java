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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;

@SuppressWarnings("rawtypes")
public class CompoundChildrenEditPolicy extends XYLayoutEditPolicy {
    private final static Pattern componentNamePattern = Pattern.compile("^COMPONENT(\\d*)$");

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
    protected Command getCreateCommand(CreateRequest request) {
        if (request.getNewObjectType().equals(AtomicTypeModel.class)) {
            CreateModelCommand command = new CreateModelCommand();
            Object obj = getHost().getModel();
            if (!(obj instanceof CompoundTypeModel)) return null;
            CompoundTypeModel parent = (CompoundTypeModel) obj;
            AtomicModel child =
                            new AtomicTypeModel().createInstance().setName(
                                            getAppropriateComponentName(parent));
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
        return null;
    }

    private String getAppropriateComponentName(CompoundTypeModel parent) {
        int maxNumber = 0;
        for (IComponentInstance model : parent.getComponents()) {
            Matcher mat = componentNamePattern.matcher(model.getName());
            if (mat.matches()) {
                int number = Integer.parseInt(mat.group(1));
                maxNumber = Math.max(number + 1, maxNumber);
            }
        }
        return "COMPONENT" + maxNumber;
    }

    @Override
    public Command getCommand(Request request) {
        return super.getCommand(request);
    }
}
