package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-3 ÏÂÎç11:21:08
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeletePortCommand.java
 * 
 */
public class DeletePortCommand extends Command {
    private PortModel                child;
    private PortAreaModel            parent;
    private List<ConnectorPortModel> targetConnections = new ArrayList<ConnectorPortModel>();

    @Override
    public void execute() {
        targetConnections.addAll(child.getShape().getTargetConnections());
        for (ConnectorPortModel model : targetConnections) {
            model.detachSource();
            model.detachTarget();
        }
        child.setParent(null);
        parent.getParent().removeChild(child.getShape());
        parent.removeChild(child);
    }

    @Override
    public void undo() {
        parent.addChild(child);
        child.setParent(parent);
        if (child.isExport()) {
            parent.getParent().addChild(child.getShape());
        }
        for (ConnectorPortModel model : targetConnections) {
            model.attachSource();
            model.attachTarget();
        }
        targetConnections.clear();
    }

    public PortModel getChild() {
        return child;
    }

    public void setChild(PortModel child) {
        this.child = child;
    }

    public PortAreaModel getParent() {
        return parent;
    }

    public void setParent(PortAreaModel parent) {
        this.parent = parent;
    }
}
