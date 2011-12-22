package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;

@SuppressWarnings("rawtypes")
public class DeleteConnectionCommand extends Command {
    private IConnection transition;

    public void setTransition(IConnection transition) {
        this.transition = transition;
    }

    @Override
    public void execute() {
        super.execute();
        transition.detachSource();
        transition.detachTarget();
    }

    @Override
    public void undo() {
        transition.attachSource();
        transition.attachTarget();
        super.undo();
    }
}
