/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityModel;


/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 上午09:43:45
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: CreateDataCommand.java
 * 
 */
public class CreateCompoundPriorityCommand extends Command {
    public final static String        CREATE_PRIORITY_COMMAND = "CreatePriorityCommand";
    private CompoundPriorityModel     child;
    private CompoundPriorityAreaModel parent;

    @Override
    public void execute() {
        super.execute();
        child.setParent(parent);
        parent.addChild(child);
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            // TODO 属性对话框
        }
        parent.setShow(true);
    }

    @Override
    public void undo() {
        super.undo();
        child.setParent(null);
        parent.removeChild(child);
    }

    public void setChild(CompoundPriorityModel child) {
        this.child = child;
    }

    public void setParent(CompoundPriorityAreaModel parent) {
        this.parent = parent;
    }
}
