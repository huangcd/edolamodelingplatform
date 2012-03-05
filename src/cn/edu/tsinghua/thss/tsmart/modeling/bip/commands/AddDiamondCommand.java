package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BaseInstanceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;

@SuppressWarnings("rawtypes")
public class AddDiamondCommand extends Command {
    private CompoundTypeModel parent;
    private BaseInstanceModel source;
    private BaseInstanceModel target;
    private DiamondModel      diamond;
    private ConnectionModel   oldConnection;
    private ConnectionModel   targetConnection;
    private ConnectionModel   sourceConnection;

    public void execute() {
        parent.addChild(diamond);

        oldConnection.detachTarget();
        oldConnection.detachSource();

        targetConnection = new ConnectionModel();
        targetConnection.setParent(parent);
        targetConnection.setSource(source);
        targetConnection.setTarget(diamond);
        targetConnection.attachSource();
        targetConnection.attachTarget();

        sourceConnection = new ConnectionModel();
        sourceConnection.setParent(parent);
        sourceConnection.setSource(diamond);
        sourceConnection.setTarget(target);
        sourceConnection.attachSource();
        sourceConnection.attachTarget();
    }

    public void undo() {
        targetConnection.detachSource();
        targetConnection.detachTarget();
        sourceConnection.detachSource();
        sourceConnection.detachTarget();
        oldConnection.attachSource();
        oldConnection.attachTarget();
        parent.removeChild(diamond);
    }

    public void setSource(BaseInstanceModel source) {
        this.source = source;
    }

    public void setTarget(BaseInstanceModel target) {
        this.target = target;
    }

    public void setDiamond(DiamondModel diamond) {
        this.diamond = diamond;
    }

    public void setOldConnection(ConnectionModel oldConnection) {
        this.oldConnection = oldConnection;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }
}
