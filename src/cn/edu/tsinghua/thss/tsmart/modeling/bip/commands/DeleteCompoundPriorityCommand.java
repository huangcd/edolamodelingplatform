package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundPriorityModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-3 ÏÂÎç11:21:08
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeletePriorityCommand.java
 * 
 */
public class DeleteCompoundPriorityCommand extends Command {
    private CompoundPriorityModel     child;
    private CompoundPriorityAreaModel parent;

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

    public CompoundPriorityModel getChild() {
        return child;
    }

    public void setChild(CompoundPriorityModel child) {
        this.child = child;
    }

    public CompoundPriorityAreaModel getParent() {
        return parent;
    }

    public void setParent(CompoundPriorityAreaModel parent) {
        this.parent = parent;
    }
}
