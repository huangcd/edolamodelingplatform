package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-3 ÏÂÎç11:21:08
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeletePriorityCommand.java
 * 
 */
public class DeletePriorityCommand extends Command {
    private PriorityModel     child;
    private PriorityAreaModel parent;

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

    public PriorityModel getChild() {
        return child;
    }

    public void setChild(PriorityModel child) {
        this.child = child;
    }

    public PriorityAreaModel getParent() {
        return parent;
    }

    public void setParent(PriorityAreaModel parent) {
        this.parent = parent;
    }
}
