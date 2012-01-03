package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DeletePlaceCommand extends Command {
    private PlaceModel            child;
    private AtomicTypeModel       parent;
    private List<TransitionModel> sourceConnections = new ArrayList<TransitionModel>();
    private List<TransitionModel> targetConnections = new ArrayList<TransitionModel>();

    @Override
    public boolean canExecute() {
        if (parent.getInitPlace().equals(child)) {
            return false;
        }
        return super.canExecute();
    }

    @Override
    public void execute() {
        parent.removeChild(child);
        sourceConnections.addAll(child.getSourceConnections());
        targetConnections.addAll(child.getTargetConnections());
        for (TransitionModel transition : sourceConnections) {
            transition.detachSource();
            transition.detachTarget();
        }
        for (TransitionModel transition : targetConnections) {
            transition.detachSource();
            transition.detachTarget();
        }
        child.getSourceConnections().clear();
        child.getTargetConnections().clear();
    }

    @Override
    public void undo() {
        parent.addChild(child);
        child.getSourceConnections().addAll(sourceConnections);
        child.getTargetConnections().addAll(targetConnections);
        for (TransitionModel transition : sourceConnections) {
            transition.attachSource();
            transition.attachTarget();
        }
        for (TransitionModel transition : targetConnections) {
            transition.attachSource();
            transition.attachTarget();
        }
        sourceConnections.clear();
        targetConnections.clear();
    }

    public void setChild(PlaceModel child) {
        this.child = child;
    }

    public void setParent(AtomicTypeModel parent) {
        this.parent = parent;
    }
}
