/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import java.beans.PropertyChangeEvent;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ActionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.GuardModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;

public class TransitionEditPart extends BaseConnectionEditPart {
    private Label toolTipLabel;

    protected void createEditPolicies() {
        // super.createEditPolicies();
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        PolylineConnection connection = new PolylineConnection();
        connection.setTargetDecoration(new PolygonDecoration());
        return connection;
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (ActionModel.ACTION.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (GuardModel.GUARD.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (PortModel.PORT.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (TransitionModel.BEND_POINTS.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
            refreshVisuals();
        }
    }

    // 双击弹出对话框
    protected void performDoubleClicked() {}
}
