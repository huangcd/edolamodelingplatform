package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ContainerModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.AtomicChildEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CustomDirectEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteAtomicEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteConnectorEditPolicy;


public class ConnectorTypeTreeEditPart extends BaseTreeEditPart {

    private ConnectorTypeModel getCastedModel() {
        return (ConnectorTypeModel) getModel();
    }

    protected String getText() {
        if (getCastedModel().getName() == null) return "";
        return getCastedModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // if (evt.getPropertyName().equals(BaseModel.NAME))
        refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        // refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
    }

    @Override
    // °²×°edit policy
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteConnectorEditPolicy());
    }

}
