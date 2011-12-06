package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorTypeModel;


/**
 * 
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-25 ÏÂÎç11:08:51
 * @project: CereusBip
 * @package: cereusbip.commands
 * @class: DeletePlaceCommand.java
 * 
 */
public class DeleteConnectorCommand extends Command {
    private CompoundTypeModel        parent;
    private ConnectorTypeModel       child;
    private List<ConnectorPortModel> sourceConnections = new ArrayList<ConnectorPortModel>();

    public void execute() {
        sourceConnections.addAll(child.getSourceConnections());
        for (ConnectorPortModel model : sourceConnections) {
            model.detachSource();
            model.detachTarget();
        }
        parent.removeChild(child.getShape());
        parent.removeChild(child);
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public void setChild(ConnectorTypeModel child) {
        this.child = child;
    }

    public void undo() {
        parent.addChild(child);
        parent.addChild(child.getShape());
        for (ConnectorPortModel model : sourceConnections) {
            model.attachSource();
            model.attachTarget();
        }
        sourceConnections.clear();
    }
}
