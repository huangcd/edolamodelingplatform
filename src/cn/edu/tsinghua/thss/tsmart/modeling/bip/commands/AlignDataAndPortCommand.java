package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;

@SuppressWarnings("all")
public class AlignDataAndPortCommand extends Command {
    private AtomicTypeModel            parent;
    private DataModel                  reference;
    private HashMap<IModel, Rectangle> positionsMap;

    @Override
    public void execute() {
        positionsMap = new HashMap<IModel, Rectangle>();
        for (DataModel model : parent.getDatas()) {
            positionsMap.put(model, model.getPositionConstraint());
        }
        for (PortModel model : parent.getPorts()) {
            positionsMap.put(model, model.getPositionConstraint());
        }
        parent.alignDatasAndPorts(reference);
    }

    @Override
    public void undo() {
        for (Map.Entry<IModel, Rectangle> entry : positionsMap.entrySet()) {
            entry.getKey().setPositionConstraint(entry.getValue());
        }
    }

    public void setParent(AtomicTypeModel parent) {
        this.parent = parent;
    }

    public void setReference(DataModel reference) {
        this.reference = reference;
    }
}
