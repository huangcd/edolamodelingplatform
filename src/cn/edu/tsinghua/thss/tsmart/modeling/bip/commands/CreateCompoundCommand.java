package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BipModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditCompoundTypeDialog;


public class CreateCompoundCommand extends Command {
    private BaseModel         parent;
    private CompoundTypeModel child;

    @Override
    public void execute() {
        redo();

        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditCompoundTypeDialog dialog = new EditCompoundTypeDialog(shell, child, parent);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }

    @Override
    public void redo() {
        child.setParent(parent);
        if (parent instanceof BipModel)
            ((BipModel) parent).addChild(child);
        else if (parent instanceof CompoundTypeModel) ((CompoundTypeModel) parent).addChild(child);
    }

    @Override
    public void undo() {
        if (parent instanceof BipModel)
            ((BipModel) parent).removeChild(child);
        else if (parent instanceof CompoundTypeModel)
            ((CompoundTypeModel) parent).removeChild(child);
        child.setParent(null);
    }

    public void setParent(BaseModel parent) {
        this.parent = parent;
    }

    public void setChild(CompoundTypeModel child) {
        this.child = child;
    }
}
