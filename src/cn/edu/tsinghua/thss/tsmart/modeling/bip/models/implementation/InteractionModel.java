package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPortType;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: ÏÂÎç3:01<br/>
 */
@Root
@SuppressWarnings("rawtypes")
public class InteractionModel
    extends BaseInstanceModel<InteractionModel, InteractionTypeModel, ConnectorTypeModel> {

    @Element
    private ActionModel upAction;
    @Element
    private ActionModel downAction;
    @ElementList
    private List<IPortType> interactionPorts;


    public InteractionModel() {
        interactionPorts = new ArrayList<IPortType>();
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

    public List<IPortType> getInteractionPorts() {
        return interactionPorts;
    }

    public void setInteractionPorts(List<IPortType> interactionPorts) {
        this.interactionPorts = interactionPorts;
    }

    public void setInteractionPort(IPortType interactionPort, int index) {
        if (index < 0 || index >= interactionPorts.size()) {
            throw new ArrayIndexOutOfBoundsException(index);
        }
        interactionPorts.set(index, interactionPort);
    }

    public void addInteractionPort(IPortType interactionPort) {
        interactionPorts.add(interactionPort);
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder("on ");
        for (IPortType portType : interactionPorts) {
            buffer.append(portType.getName()).append(' ');
        }
        buffer.append("\n\t\tup{").append(upAction).append("}");
        buffer.append("\n\t\tdown{").append(downAction).append("}\n");
        return buffer.toString();
    }

    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
        
    }
}
