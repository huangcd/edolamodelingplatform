package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.connection;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;

/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 ÏÂÎç09:37:06
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: CreateTransitionCommand.java
 * 
 */
public class CreateTransitionCommand extends Command {
    private PlaceModel      source;
    private PlaceModel      target;
    private TransitionModel connection;

    @Override
    public boolean canExecute() {
        if (source == null || target == null) return false;
        return true;
    }

    @Override
    public void execute() {
        connection.attachSource();
        connection.attachTarget();
    }

    @Override
    public void undo() {
        connection.detachSource();
        connection.detachTarget();
    }

    public void setSource(PlaceModel source) {
        this.source = source;
        connection.setSource(source);
    }

    public void setTarget(PlaceModel target) {
        this.target = target;
        connection.setTarget(target);
    }

    public void setConnection(TransitionModel connection) {
        this.connection = connection;
    }

    public void setParent(AtomicTypeModel parent) {
        connection.setParent(parent);
    }
}
