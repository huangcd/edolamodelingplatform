package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;

@SuppressWarnings("rawtypes")
public class PortTreeEditPart extends BaseTreeEditPart {

    private PortModel getCastedModel() {
        return (PortModel) getModel();
    }

    protected String getText() {
        return getCastedModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        // refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(BIPEditor.getImage("icons/port_16.png").createImage());
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }
}
