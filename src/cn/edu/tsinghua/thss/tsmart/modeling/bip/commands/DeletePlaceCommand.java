package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.TransitionModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-25 ÏÂÎç11:08:51
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeletePlaceCommand.java
 * 
 */
public class DeletePlaceCommand extends Command {
    private AtomicTypeModel       parent;
    private PlaceModel            child;
    private List<TransitionModel> sourceConnections = new ArrayList<TransitionModel>();
    private List<TransitionModel> targetConnections = new ArrayList<TransitionModel>();

    public void execute() {
        sourceConnections.addAll(child.getSourceConnections());
        targetConnections.addAll(child.getTargetConnections());
        for (TransitionModel pcm : sourceConnections) {
            pcm.detachSource();
            pcm.detachTarget();
        }
        for (TransitionModel pcm : targetConnections) {
            pcm.detachSource();
            pcm.detachTarget();
        }
        parent.removeChild(child);
    }

    public void setParent(AtomicTypeModel parent) {
        this.parent = parent;
    }

    public void setChild(PlaceModel child) {
        this.child = child;
    }

    public void undo() {
        parent.addChild(child);
        for (TransitionModel pcm : sourceConnections) {
            pcm.attachSource();
            pcm.attachTarget();
            System.out.println(pcm);
        }
        for (TransitionModel pcm : targetConnections) {
            pcm.attachSource();
            pcm.attachTarget();
            System.out.println(pcm);
        }
        sourceConnections.clear();
        targetConnections.clear();
    }
}
