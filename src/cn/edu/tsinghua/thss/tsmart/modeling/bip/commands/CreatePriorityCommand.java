/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditPriorityDialog;


/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 ÉÏÎç09:43:45
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: CreateDataCommand.java
 * 
 */
public class CreatePriorityCommand extends Command {
    public final static String CREATE_PRIORITY_COMMAND = "CreatePriorityCommand";
    private PriorityModel      child;
    private PriorityAreaModel  parent;

    @Override
    public void execute() {
        super.execute();
        child.setParent(parent);
        parent.addChild(child);
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditPriorityDialog dialog =
                            new EditPriorityDialog(shell, child, parent.getParent()
                                            .getPortAreaModel().getChildren(), parent);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
        parent.setShow(true);
    }

    @Override
    public void undo() {
        super.undo();
        child.setParent(null);
        parent.removeChild(child);
    }

    public void setChild(PriorityModel child) {
        this.child = child;
    }

    public void setParent(PriorityAreaModel parent) {
        this.parent = parent;
    }
}
