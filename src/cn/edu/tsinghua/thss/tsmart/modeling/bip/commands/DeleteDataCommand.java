package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-3 ÏÂÎç11:21:08
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeleteDataCommand.java
 * 
 */
public class DeleteDataCommand extends Command {
    private DataModel     child;
    private DataAreaModel parent;

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

    public DataModel getChild() {
        return child;
    }

    public void setChild(DataModel child) {
        this.child = child;
    }

    public DataAreaModel getParent() {
        return parent;
    }

    public void setParent(DataAreaModel parent) {
        this.parent = parent;
    }
}
