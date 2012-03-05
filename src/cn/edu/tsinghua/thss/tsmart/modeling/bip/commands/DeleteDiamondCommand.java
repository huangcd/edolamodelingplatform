package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BaseInstanceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;

@SuppressWarnings("rawtypes")
public class DeleteDiamondCommand extends Command {
    private CompoundTypeModel parent;
    private DiamondModel      diamond;
    private ConnectionModel   newConnection;
    private ConnectionModel   sourceConnection;
    private ConnectionModel   targetConnection;
    private boolean           createNewConnection = true;

    @Override
    public void execute() {
        sourceConnection = diamond.getSourceConnection();
        BaseInstanceModel target = sourceConnection.getTarget();
        sourceConnection.detachSource();
        sourceConnection.detachTarget();

        targetConnection = diamond.getTargetConnection();
        BaseInstanceModel source = targetConnection.getSource();
        targetConnection.detachSource();
        targetConnection.detachTarget();

        if (createNewConnection) {
            newConnection = new ConnectionModel();
            newConnection.setParent(parent);
            newConnection.setSource(source);
            newConnection.setTarget(target);
            newConnection.attachSource();
            newConnection.attachTarget();
        }

        parent.removeChild(diamond);
    }

    @Override
    public void undo() {
        diamond = new DiamondModel().setPositionConstraint(diamond.getPositionConstraint());
        parent.addChild(diamond);
        sourceConnection.setSource(diamond);
        targetConnection.setTarget(diamond);

        if (createNewConnection) {
            newConnection.detachSource();
            newConnection.detachTarget();
        }

        targetConnection.attachSource();
        targetConnection.attachTarget();

        sourceConnection.attachSource();
        sourceConnection.attachTarget();
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public void setDiamond(DiamondModel diamond) {
        this.diamond = diamond;
    }

    public CompoundTypeModel getParent() {
        return parent;
    }

    public DiamondModel getDiamond() {
        return diamond;
    }

    public ConnectionModel getNewConnection() {
        return newConnection;
    }

    public ConnectionModel getSourceConnection() {
        return sourceConnection;
    }

    public ConnectionModel getTargetConnection() {
        return targetConnection;
    }

    public void setCreateNewConnection(boolean createNewConnection) {
        this.createNewConnection = createNewConnection;
    }
}
