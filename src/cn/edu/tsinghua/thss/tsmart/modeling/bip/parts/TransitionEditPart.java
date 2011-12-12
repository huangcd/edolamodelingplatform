/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RelativeBendpoint;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ActionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.GuardModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.RelativeBendpointModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionBendpointEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.handles.FigureLocator;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TransitionEditPart extends BaseConnectionEditPart {
    private PolylineConnection connection;
    private Label              tooltipLabel;
    private Label              actionLabel;
    private Label              portLabel;
    private Label              guardLabel;
    private FigureLocator      actionLocator;
    private FigureLocator      guardLocator;

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new ConnectionEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE,
                        new ConnectionBendpointEditPolicy());
    }

    private void handleLoop() {}

    @Override
    protected IFigure createFigure() {
        connection = new PolylineConnection();
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setSize(100, 100);
        connection.setTargetDecoration(decoration);

        if (getModel().getSource().equals(getModel().getTarget())) {
            handleLoop();
        }

        tooltipLabel = new Label(getModel().getFriendlyString());
        connection.setToolTip(tooltipLabel);

        portLabel = new Label(getModel().getPortString());
        portLabel.setFont(properties.getDefaultEditorFont());
        portLabel.setForegroundColor(properties.getPortLabelColor());
        getParent().getFigure().add(portLabel);
        addFigureMouseEvent(portLabel);

        actionLabel = new Label(getModel().getActionString());
        actionLabel.setFont(properties.getDefaultEditorFont());
        actionLabel.setForegroundColor(properties.getActionLabelColor());
        getParent().getFigure().add(actionLabel);
        actionLocator = new FigureLocator(portLabel, actionLabel, PositionConstants.EAST, 5);
        addFigureMouseEvent(actionLabel);

        guardLabel = new Label(getModel().getGuardString());
        guardLabel.setFont(properties.getDefaultEditorFont());
        guardLabel.setForegroundColor(properties.getGuardLabelColor());
        getParent().getFigure().add(guardLabel);
        guardLocator = new FigureLocator(portLabel, guardLabel, PositionConstants.WEST, 5);
        addFigureMouseEvent(guardLabel);

        relocateLabels(getBendpoints());

        return connection;
    }

    private void relocateLabels(ArrayList<RelativeBendpoint> bendpoints) {
        if (bendpoints.isEmpty()) {
            Point ref1 = getModel().getSource().getPositionConstraint().getLocation();
            Point ref2 = getModel().getTarget().getPositionConstraint().getLocation();
            Point location = new Point((ref1.x + ref2.x) / 2, (ref1.y + ref2.y) / 2);
            Dimension size = portLabel.getPreferredSize();
            portLabel.setBounds(new Rectangle(location, size));
        } else {
            MidpointLocator locator = new MidpointLocator(connection, bendpoints.size() / 2);
            locator.relocate(portLabel);
        }
        guardLocator.relocate();
        actionLocator.relocate();
    }

    private void setTooltip() {
        tooltipLabel.setText(getModel().getFriendlyString());
    }

    private ArrayList<RelativeBendpoint> getBendpoints() {
        IConnection conn = getModel();
        ArrayList<RelativeBendpointModel> bendpointModels = conn.getBendpoints();
        ArrayList<RelativeBendpoint> bendpoints = new ArrayList<RelativeBendpoint>();
        float i = 1;
        int constraint = bendpointModels.size() + 1;
        for (RelativeBendpointModel model : bendpointModels) {
            RelativeBendpoint bendpoint = new RelativeBendpoint(getConnectionFigure());
            bendpoint.setRelativeDimensions(model.getFirstRelativeDimension(),
                            model.getSecondRelativeDimension());
            bendpoint.setWeight(i / constraint);
            bendpoints.add(bendpoint);
        }
        return bendpoints;
    }

    protected void refreshBendpoints() {
        ArrayList<RelativeBendpoint> bendpoints = getBendpoints();
        if (bendpoints.isEmpty() && getModel().isLoop()) {
            System.out.println(connection.getPoints().size());
            Point center = getModel().getSource().getPositionConstraint().getCenter();
            RelativeBendpointModel bendpoint = new RelativeBendpointModel();
            Point point = new Point(center.x - 30, center.y + 30);
            connection.insertPoint(point, 1);
            // bendpoint.setRelativeDimensions(point.getDifference(center),
            // point.getDifference(center));
            // getModel().getBendpoints().add(bendpoint);

            center = getModel().getTarget().getPositionConstraint().getCenter();
            bendpoint = new RelativeBendpointModel();
            point = new Point(center.x + 30, center.y + 30);
            connection.insertPoint(point, 2);
            connection.repaint();
            // bendpoint.setRelativeDimensions(point.getDifference(center),
            // point.getDifference(center));
            // getModel().getBendpoints().add(bendpoint);
        } else {
            getConnectionFigure().setRoutingConstraint(bendpoints);
        }
        relocateLabels(bendpoints);
    }

    public TransitionModel getModel() {
        return (TransitionModel) super.getModel();
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (ActionModel.ACTION.equals(evt.getPropertyName())) {
            setTooltip();
            actionLabel.setText(getModel().getActionString());
            actionLocator.relocate();
        } else if (GuardModel.GUARD.equals(evt.getPropertyName())) {
            setTooltip();
            guardLabel.setText(getModel().getGuardString());
            guardLocator.relocate();
        } else if (PortModel.PORT.equals(evt.getPropertyName())) {
            setTooltip();
            portLabel.setText(getModel().getPortString());
        } else if (TransitionModel.BEND_POINTS.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        }
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
