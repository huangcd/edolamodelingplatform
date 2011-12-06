/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;


/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 ÉÏÎç09:43:45
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: CreatePlaceCommand.java
 * 
 */
public class CreatePlaceCommand extends Command {
    private PlaceModel      child;
    private AtomicTypeModel parent;

    @Override
    public void execute() {
        super.execute();
        parent.addChild(child);
        child.setParent(parent);
    }

    @Override
    public void undo() {
        super.undo();
        child.setParent(null);
        parent.removeChild(child);
    }

    public void setChild(PlaceModel child) {
        this.child = child;
    }

    public void setParent(AtomicTypeModel parent) {
        this.parent = parent;
    }
}
