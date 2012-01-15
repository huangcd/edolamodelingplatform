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
        for (ConnectionModel connection : sourceConnections) {
            connection.detachSource();
            connection.detachTarget();
            if (connection.getTarget() instanceof InvisibleBulletModel) {
                parent.removeBullet((InvisibleBulletModel) connection.getTarget());
            }
        }
        // XXX 删除连接子相关的PriorityModel
        parent.deleteRelatedPrioritiesWhenDeleteConnector(child);
    }

    @Override
    public void undo() {
        parent.addChild(child);
        for (ConnectionModel connection : sourceConnections) {
            connection.setSource(child);
            connection.attachSource();
            connection.attachTarget();
            if (connection.getTarget() instanceof InvisibleBulletModel) {
                parent.addChild((InvisibleBulletModel) connection.getTarget());
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
