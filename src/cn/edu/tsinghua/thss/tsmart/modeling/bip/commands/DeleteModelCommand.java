package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings({"unchecked", "rawtypes"})
public class DeleteModelCommand extends Command {
    private IModel  child;
    private IContainer parent;

    @Override
    public void execute() {
        child.setParent(null);
        parent.removeChild(child);
    }

    @Override
    public void undo() {
        parent.addChild(child);
        child.setParent(parent);
    }

    public void setChild(IModel child) {
        this.child = child;
    }

    public void setParent(IContainer parent) {
        this.parent = parent;
    }
}
