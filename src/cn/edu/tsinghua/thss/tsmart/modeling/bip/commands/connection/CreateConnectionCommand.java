package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CreateConnectionCommand extends Command {
    private IInstance   source;
    private IInstance   target;
    private IConnection connection;

    @Override
    public boolean canExecute() {
        if (source == null || target == null) return false;
        return true;
    }

    @Override
    public void execute() {
        connection.attachSource();
        connection.attachTarget();
    }

    @Override
    public void undo() {
        connection.detachSource();
        connection.detachTarget();
    }

    public void setSource(IInstance source) {
        this.source = source;
        connection.setSource(source);
    }

    public void setTarget(IInstance target) {
        this.target = target;
        connection.setTarget(target);
    }

    public void setConnection(IConnection connection) {
        this.connection = connection;
    }

    public void setParent(IContainer parent) {
        connection.setParent(parent);
    }
}
