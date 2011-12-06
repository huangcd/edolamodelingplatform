package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.TransitionModel;


public class DirectEditCommand extends Command {
    private String           oldText, newText;
    private PlaceModel       placeModel;
    private TransitionModel  transitonModel;
    private AtomicTypeModel  atomicTypeModel;
    private DataModel        dataModel;
    private PortModel        portModel;
    private PriorityModel    priorityModel;
    private int              choice     = 0;
    private static final int PLACE      = 1;
    private static final int TRANSITION = 2;
    private static final int ATOMIC     = 3;
    private static final int DATA       = 4;
    private static final int PORT       = 5;
    private static final int PRIORITY   = 6;

    // override
    @Override
    public void execute() {
        switch (choice) {
            case PLACE:
                placeModel.setName(newText);
                oldText = placeModel.getName();
                break;
            case TRANSITION:
                transitonModel.setProperties(newText);
                oldText = transitonModel.toString();
                break;
            case ATOMIC:
                atomicTypeModel.setName(newText);
                oldText = atomicTypeModel.getName();
                break;
            case DATA:
                dataModel.setProperties(newText);
                oldText = dataModel.toString();
                break;
            case PORT:
                portModel.setProperties(newText);
                oldText = portModel.toString();
                break;
            case PRIORITY:
                priorityModel.setProperties(newText);
                oldText = priorityModel.toString();
                break;

        }
    }

    // override
    @Override
    public void undo() {
        switch (choice) {
            case PLACE:
                placeModel.setName(oldText);
                oldText = placeModel.getName();
                break;
            case TRANSITION:
                transitonModel.setProperties(oldText);
                oldText = transitonModel.toString();
                break;
            case ATOMIC:
                atomicTypeModel.setName(oldText);
                oldText = atomicTypeModel.getName();
                break;
            case DATA:
                dataModel.setProperties(oldText);
                oldText = dataModel.toString();
                break;
            case PORT:
                portModel.setProperties(oldText);
                oldText = portModel.toString();
                break;
            case PRIORITY:
                priorityModel.setProperties(oldText);
                oldText = priorityModel.toString();
                break;

        }
    }

    public void setPlaceModel(Object model) {
        placeModel = (PlaceModel) model;
        choice = PLACE;
    }

    public void setTransitionModel(Object model) {
        transitonModel = (TransitionModel) model;
        choice = TRANSITION;
    }

    public void setAtomicTypeModel(Object model) {
        atomicTypeModel = (AtomicTypeModel) model;
        choice = ATOMIC;
    }

    public void setDataModel(Object model) {
        dataModel = (DataModel) model;
        choice = DATA;
    }

    public void setPortModel(Object model) {
        portModel = (PortModel) model;
        choice = PORT;
    }

    public void setPriorityModel(Object model) {
        priorityModel = (PriorityModel) model;
        choice = PRIORITY;
    }

    public void setText(String text) {
        newText = text;
    }
}
