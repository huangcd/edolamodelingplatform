package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CreateModelCommand extends Command {
    private IContainer parent;
    private IInstance  child;

    @Override
    public void execute() {
        child.setParent(parent);
        parent.addChild(child);
    }

    @Override
    public void redo() {
        child.setParent(parent);
        parent.addChild(child);
    }

    @Override
    public void undo() {
        parent.removeChild(child);
        child.setParent(parent);
    }

    public void setParent(IContainer parent) {
        this.parent = parent;
    }

    public void setChild(IInstance child) {
        this.child = child;
    }
}
