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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ActionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.GuardModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureLocator;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TransitionEditPart extends BaseConnectionEditPart {
    private PolylineConnection connection;
    private Label              tooltipLabel;
    private Label              actionLabel;
    private Label              portLabel;
    private Label              guardLabel;
    // 根据portLabel的位置定位actionLabel
    private FigureLocator      actionLocator;
    // 根据portLabel的位置定位guardLabel
    private FigureLocator      guardLocator;

    private void handleLoop() {}

    @Override
    protected IFigure createFigure() {
        connection = new PolylineConnection();
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setSize(100, 100);
        connection.setTargetDecoration(decoration);
        connection.setRoutingConstraint(getModel().getBendpoints());

        if (getModel().getSource().equals(getModel().getTarget())) {
            handleLoop();
        }

        tooltipLabel = new Label(getModel().getFriendlyString());
        connection.setToolTip(tooltipLabel);

        portLabel = new Label(getModel().getPortString());
        portLabel.setFont(properties.getDefaultEditorFont());
        portLabel.setForegroundColor(properties.getPortLabelColor());
        getGraphLayerFigure().add(portLabel);
        addFigureMouseEvent(portLabel);

        actionLabel = new Label(getModel().getActionString());
        actionLabel.setFont(properties.getDefaultEditorFont());
        actionLabel.setForegroundColor(properties.getActionLabelColor());
        getGraphLayerFigure().add(actionLabel);
        actionLocator = new FigureLocator(portLabel, actionLabel, PositionConstants.EAST, 5);
        addFigureMouseEvent(actionLabel);

        guardLabel = new Label(getModel().getGuardString());
        guardLabel.setFont(properties.getDefaultEditorFont());
        guardLabel.setForegroundColor(properties.getGuardLabelColor());
        getGraphLayerFigure().add(guardLabel);
        guardLocator = new FigureLocator(portLabel, guardLabel, PositionConstants.WEST, 5);
        addFigureMouseEvent(guardLabel);
        relocateLabels(getBendpoints());
        return connection;
    }

    private IFigure getGraphLayerFigure() {
        if (getSource() != null) {
            return ((PlaceEditPart) getSource()).getParent().getFigure();
        } else if (getTarget() != null) {
            return ((PlaceEditPart) getTarget()).getParent().getFigure();
        }
        return null;
    }

    protected Point getSourceLocation() {
        return getModel().getSource().getPositionConstraint().getLocation();
    }

    protected Point getTargetLocation() {
        return getModel().getTarget().getPositionConstraint().getLocation();
    }

    private void relocateLabels(ArrayList<Bendpoint> bendpoints) {
        Point location = getRelocateLocation(bendpoints);
        Dimension size = portLabel.getPreferredSize();
        location.translate(-size.width / 2, 0);
        portLabel.setBounds(new Rectangle(location, size));
        guardLocator.relocate();
        actionLocator.relocate();
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
        getGraphLayerFigure().remove(actionLabel);
        getGraphLayerFigure().remove(guardLabel);
        getGraphLayerFigure().remove(portLabel);
        super.deactivate();
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        if (ActionModel.ACTION.equals(propertyName)) {
            setTooltip();
            actionLabel.setText(getModel().getActionString());
            actionLocator.relocate();
        } else if (GuardModel.GUARD.equals(propertyName)) {
            setTooltip();
            guardLabel.setText(getModel().getGuardString());
            guardLocator.relocate();
        } else if (IModel.PORT.equals(propertyName)) {
            setTooltip();
            portLabel.setText(getModel().getPortString());
        } else if (TransitionModel.BEND_POINTS.equals(propertyName)) {
            refreshVisuals();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (PlaceModel.SOURCE.equals(propertyName) || PlaceModel.TARGET.equals(propertyName)) {
            refreshVisuals();
        }
        setTooltip();
        actionLabel.setText(getModel().getActionString());
        guardLabel.setText(getModel().getGuardString());
        portLabel.setText(getModel().getPortString());
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
