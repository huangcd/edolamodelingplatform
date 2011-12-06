package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditAtomicTypeDialog;

public class CreateAtomicCommand extends Command {
    private CompoundTypeModel parent;
    private AtomicTypeModel   child;

    @Override
    public void execute() {
        child.setParent(parent);
        parent.addChild(child);
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditAtomicTypeDialog dialog = new EditAtomicTypeDialog(shell, child, parent);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
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

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public void setChild(AtomicTypeModel child) {
        this.child = child;
    }
}
