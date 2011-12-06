/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditDataDialog;


/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 ÉÏÎç09:43:45
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: CreateDataCommand.java
 * 
 */
public class CreateDataCommand extends Command {
    public final static String CREATE_DATA_COMMAND = "CreateDataCommand";
    private DataModel          child;
    private DataAreaModel      parent;

    @Override
    public void execute() {
        super.execute();
        child.setParent(parent);
        parent.addChild(child);
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditDataDialog dialog = new EditDataDialog(shell, parent, child);
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

    public void setChild(DataModel child) {
        this.child = child;
    }

    public void setParent(DataAreaModel parent) {
        this.parent = parent;
    }
}
