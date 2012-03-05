package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;

public class DeleteConnectorCommand extends Command {
    private ConnectorModel                                        child;
    private CompoundTypeModel                                     parent;
    private List<ConnectionModel>                                 sourceConnections;
    private HashMap<ConnectionModel, Stack<DeleteDiamondCommand>> commandStacks;

    @Override
    public void execute() {
        sourceConnections = new ArrayList<ConnectionModel>();
        commandStacks = new HashMap<ConnectionModel, Stack<DeleteDiamondCommand>>();
        sourceConnections.addAll(child.getSourceConnections());
        for (ConnectionModel connection : sourceConnections) {
            Stack<DeleteDiamondCommand> commands = new Stack<DeleteDiamondCommand>();
            commandStacks.put(connection, commands);
            // 逐个删除DiamondModel
            while (connection.getTarget() instanceof DiamondModel) {
                DiamondModel diamond = (DiamondModel) connection.getTarget();
                DeleteDiamondCommand command = new DeleteDiamondCommand();
                command.setCreateNewConnection(false);
                command.setDiamond(diamond);
                command.setParent(parent);
                commands.push(command);
                connection = diamond.getSourceConnection();
                command.execute();
            }
            // 如果最后一个是InvisibleBulletModel，直接将其删除
            if (connection.getTarget() instanceof InvisibleBulletModel) {
                parent.removeBullet((InvisibleBulletModel) connection.getTarget());
            }
            connection.detachSource();
            connection.detachTarget();
        }
        parent.removeChild(child);
        // 删除与该Connector相关的所有Priorities
        parent.deleteRelatedPrioritiesWhenDeleteConnector(child);
    }

    @Override
    public void undo() {
        parent.addChild(child);
        for (ConnectionModel connection : sourceConnections) {
            Stack<DeleteDiamondCommand> commands = commandStacks.get(connection);
            while (!commands.isEmpty()) {
                DeleteDiamondCommand command = commands.pop();
                command.undo();
            }
            if (connection.getTarget() instanceof InvisibleBulletModel) {
                parent.addChild((InvisibleBulletModel) connection.getTarget());
            }
            connection.attachSource();
            connection.attachTarget();
        }
        sourceConnections.clear();
        // TODO 还原与该Connector相关的所有Priorities
    }

    public void setChild(ConnectorModel child) {
        this.child = child;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }
}
