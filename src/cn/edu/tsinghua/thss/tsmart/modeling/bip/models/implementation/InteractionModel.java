package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel.ArgumentEntry;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: ÏÂÎç3:01<br/>
 */
@Root
@SuppressWarnings("rawtypes")
public class InteractionModel
                extends BaseInstanceModel<InteractionModel, BaseTypeModel, ConnectorTypeModel> {

    private static final long   serialVersionUID = 6862102301754364886L;
    @Element
    private ActionModel         upAction;
    @Element
    private ActionModel         downAction;
    @ElementList
    private List<ArgumentEntry> interactionPorts;


    protected InteractionModel() {
        interactionPorts = new ArrayList<ArgumentEntry>();
    }

    protected InteractionModel(ActionModel upAction, ActionModel downAction,
                    List<ArgumentEntry> interactionPorts) {
        super();
        this.upAction = upAction;
        this.downAction = downAction;
        this.interactionPorts = interactionPorts;
    }

    public ActionModel getUpAction() {
        return upAction;
    }

    public void setUpAction(ActionModel upAction) {
        this.upAction = upAction;
    }

    public ActionModel getDownAction() {
        return downAction;
    }

    public void setDownAction(ActionModel downAction) {
        this.downAction = downAction;
    }

    public List<ArgumentEntry> getInteractionPorts() {
        return interactionPorts;
    }

    public ArrayList<String> getInteractionPortsAsStringArray() {
        ArrayList<String> list = new ArrayList<String>();
        for (ArgumentEntry entry : getInteractionPorts()) {
            list.add(entry.getName());
        }
        return list;
    }

    public void setInteractionPorts(List<ArgumentEntry> interactionPorts) {
        this.interactionPorts = interactionPorts;
    }

    public void setInteractionPort(ArgumentEntry interactionPort, int index) {
        if (index < 0 || index >= interactionPorts.size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        interactionPorts.set(index, interactionPort);
    }

    public void addInteractionPort(ArgumentEntry interactionPort) {
        interactionPorts.add(interactionPort);
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public String getInteractionString() {
        StringBuilder buffer = new StringBuilder();
        for (ArgumentEntry portType : interactionPorts) {
            buffer.append(portType.getName()).append(' ');
        }
        return buffer.toString().trim();
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder("on ");
        buffer.append(getInteractionString());
        buffer.append("\n\t\tup{\n\t\t\t").append(upAction.exportToBip()).append("\n\t\t}");
        buffer.append("\n\t\tdown{\n\t\t\t").append(downAction.exportToBip()).append("\n\t\t}\n");
        return buffer.toString();
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}
}
