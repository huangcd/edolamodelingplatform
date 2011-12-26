package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DeleteConnectorCommand extends Command {
    private ConnectorModel        child;
    private CompoundTypeModel     parent;
    private List<ConnectionModel> sourceConnections = new ArrayList<ConnectionModel>();

    @Override
    public void execute() {
        parent.removeChild(child);
        sourceConnections.addAll(child.getSourceConnections());
        for (ConnectionModel transition : sourceConnections) {
            transition.detachSource();
            transition.detachTarget();
            if (transition.getTarget() instanceof InvisibleBulletModel) {
                parent.removeBullet((InvisibleBulletModel) transition.getTarget());
            }
        }
        child.getSourceConnections().clear();
    }

    @Override
    public void undo() {
        parent.addChild(child);
        child.getSourceConnections().addAll(sourceConnections);
        for (ConnectionModel transition : sourceConnections) {
            transition.attachSource();
            transition.attachTarget();
            if (transition.getTarget() instanceof InvisibleBulletModel) {
                parent.addChild((InvisibleBulletModel) transition.getTarget());
            }
        }
        sourceConnections.clear();
    }

    public void setChild(ConnectorModel child) {
        this.child = child;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }
}
