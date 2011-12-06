package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditConnectorDialog;


public class CreateConnectorCommand extends Command {
    public CompoundTypeModel  parent;
    public ConnectorTypeModel child;

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public void setChild(ConnectorTypeModel child) {
        this.child = child;
    }

    @Override
    public void execute() {
        super.execute();
        this.parent.addChild(child);
        this.child.setParent(parent);
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditConnectorDialog dialog = new EditConnectorDialog(shell, child, parent);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }

    @Override
    public void undo() {
        super.undo();
        this.child.setParent(null);
        this.parent.removeChild(child);
    }

}
