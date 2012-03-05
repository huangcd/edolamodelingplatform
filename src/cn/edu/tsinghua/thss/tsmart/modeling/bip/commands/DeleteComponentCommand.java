package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BaseInstanceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;

@SuppressWarnings({"unchecked", "rawtypes"})
/**
 * @author Huangcd
 * 从ComponentType编辑页面中删除Component模型，
 * 删除的内容包括Component模型本身以及Component包含的Bullet和连线
 */
public class DeleteComponentCommand extends Command {
    private ComponentModel                              child;
    private CompoundTypeModel                           parent;
    private HashMap<BulletModel, List<ConnectionModel>> bulletConnections;

    @Override
    public void execute() {
        parent.removeChild(child);
        for (Map.Entry<BulletModel, List<ConnectionModel>> entry : bulletConnections.entrySet()) {
            List<ConnectionModel> connections = entry.getValue();
            for (ConnectionModel connection : connections) {
                connection.detachTarget();
                BaseInstanceModel source = connection.getSource();
                if (source instanceof ConnectorModel) {
                    if (((ConnectorModel) source).getSourceConnections().contains(this)) {
                        ((ConnectorModel) source).reverseConnections(connection);
                        while (!connection.getBendpoints().isEmpty()) {
                            connection.removeBendpoint(0);
                        }
                    }
                } else if (source instanceof DiamondModel) {
                    if (((DiamondModel) connection.getSource()).getSourceConnections()
                                    .contains(this)) {
                        // TODO 对于DiamondModel应该如何处理？
                    }
                }
            }
        }
    }

    @Override
    public void undo() {
        parent.addChild(child);
        for (Map.Entry<BulletModel, List<ConnectionModel>> entry : bulletConnections.entrySet()) {
            List<ConnectionModel> connections = entry.getValue();
            for (ConnectionModel connection : connections) {
                connection.detachTarget();
                connection.setTarget(entry.getKey());
                connection.attachTarget();
            }
        }
    }

    public void setChild(ComponentModel child) {
        this.child = child;
        bulletConnections = new HashMap<BulletModel, List<ConnectionModel>>();
        List<PortModel> ports = ((ComponentTypeModel) child.getType()).getExportPorts();
        for (PortModel port : ports) {
            BulletModel bullet = port.getBullet();
            bulletConnections.put(bullet,
                            new ArrayList<ConnectionModel>(bullet.getTargetConnections()));
        }
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }
}
