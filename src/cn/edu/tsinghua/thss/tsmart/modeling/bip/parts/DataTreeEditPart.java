package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;

@SuppressWarnings("rawtypes")
public class DataTreeEditPart extends BaseTreeEditPart {

    private DataModel getCastedModel() {
        return (DataModel) getModel();
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
        String dataType=getCastedModel().getType().getName();
        if(dataType.equals("int"))
            setWidgetImage(BIPEditor.getImage("icons/int_16.png").createImage());
        else if (dataType.equals("bool"))
            setWidgetImage(BIPEditor.getImage("icons/bool_16.png").createImage());
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }

}
