/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.BIPConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionBendpointEditPolicy;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ConnectionEditPart extends BaseConnectionEditPart {
    private PolylineConnection connection;
    private Label              tooltipLabel;

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new BIPConnectionEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
                        new ConnectionBendpointEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        connection = new PolylineConnection();
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setSize(100, 100);
        connection.setTargetDecoration(decoration);
        connection.setRoutingConstraint(getModel().getBendpoints());

        tooltipLabel = new Label(getModel().getFriendlyString());
        connection.setToolTip(tooltipLabel);
        return connection;
    }

    private IFigure getGraphLayerFigure() {
        if (getSource() != null) {
            return ((ConnectorEditPart) getSource()).getParent().getFigure();
        } else if (getTarget() != null) {
            return ((BulletEditPart) getTarget()).getParent().getFigure();
        }
        return null;
    }

    private void relocateLabels(ArrayList<Bendpoint> bendpoints) {
    }

    private void setTooltip() {
        tooltipLabel.setText(getModel().getFriendlyString());
    }

    private ArrayList<Bendpoint> getBendpoints() {
        IConnection conn = getModel();
        ArrayList<Bendpoint> bendpoints = conn.getBendpoints();
        return bendpoints;
    }

    protected void refreshBendpoints() {
        ArrayList<Bendpoint> bendpoints = getBendpoints();
        if (bendpoints.isEmpty() && getModel().isLoop()) {
            getConnectionFigure().setRoutingConstraint(bendpoints);
        } else {
            getConnectionFigure().setRoutingConstraint(bendpoints);
        }
        relocateLabels(bendpoints);
    }

    public TransitionModel getModel() {
        return (TransitionModel) super.getModel();
    }

    /**
     * 删除Transition的时候移除相应标签
     */
    public void deactivate() {
        super.deactivate();
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        refreshVisuals();
    }

    @Override
    protected void refreshVisuals() {
        refreshBendpoints();
        super.refreshVisuals();
    }

    // 双击弹出对话框
    protected void performDoubleClicked() {}
}
