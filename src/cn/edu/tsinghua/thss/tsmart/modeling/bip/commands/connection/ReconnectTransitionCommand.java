package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;

public class ReconnectTransitionCommand extends Command {
    private PlaceModel      oldSource;
    private PlaceModel      oldTarget;
    private PlaceModel      newSource;
    private PlaceModel      newTarget;
    private TransitionModel connection;

    public void execute() {
        if (newSource != null) {
            connection.detachSource();
            connection.setSource(newSource);
            connection.attachSource();
        }
        if (newTarget != null) {
            connection.detachTarget();
            connection.setTarget(newTarget);
            connection.attachTarget();
        }
    }

    public void undo() {
        if (newSource != null) {
            connection.detachSource();
            connection.setSource(oldSource);
            connection.attachSource();
        }
        if (newTarget != null) {
            connection.detachTarget();
            connection.setTarget(oldTarget);
            connection.attachTarget();
        }
    }

    public void setNewSource(PlaceModel source) {
        this.newSource = source;
        this.oldSource = connection.getSource();
    }

    public void setNewTarget(PlaceModel target) {
        this.newTarget = target;
        this.oldTarget = connection.getTarget();
    }

    public void setConnection(TransitionModel connection) {
        this.connection = connection;
    }

    public void setParent(AtomicTypeModel parent) {
        connection.setParent(parent);
    }
}