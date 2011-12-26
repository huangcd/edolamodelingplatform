package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ReconnectConnectionCommand extends Command {
    private IInstance   oldSource;
    private IInstance   oldTarget;
    private IInstance   newSource;
    private IInstance   newTarget;
    private IConnection connection;

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

    @Override
    public boolean canUndo() {
        if (oldTarget != null && oldTarget instanceof InvisibleBulletModel) {
            return false;
        }
        return super.canUndo();
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

    public void setNewSource(IInstance source) {
        this.newSource = source;
        this.oldSource = connection.getSource();
    }

    public void setNewTarget(IInstance target) {
        this.newTarget = target;
        this.oldTarget = connection.getTarget();
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }
}
