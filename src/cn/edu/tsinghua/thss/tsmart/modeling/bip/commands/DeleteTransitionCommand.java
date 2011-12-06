package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.TransitionModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-28 ÏÂÎç02:23:46
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeleteTransitionCommand.java
 * 
 */
public class DeleteTransitionCommand extends Command {
    private TransitionModel transition;

    public void setTransition(TransitionModel transition) {
        this.transition = transition;
    }

    @Override
    public void execute() {
        super.execute();
        transition.detachSource();
        transition.detachTarget();
    }

    @Override
    public void undo() {
        transition.attachSource();
        transition.attachTarget();
        super.undo();
    }
}
