package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.BIPConnectionEditPolicy;

@SuppressWarnings({"unchecked", "rawtypes"})
public class ConnectionEditPart extends BaseConnectionEditPart {
    private PolylineConnection connection;
    private Label              tooltipLabel;
    private Label              infoLabel;

    @Override
    protected IFigure createFigure() {
        connection = new PolylineConnection();
        if (getSource() != null) {
            if (getSource() instanceof ConnectorEditPart) {
                connection.setForegroundColor(((ConnectorModel) getSource().getModel())
                                .getLineColor());
            } else {
                connection.setForegroundColor(ColorConstants.black);
            }
        }
        PolygonDecoration decoration = new PolygonDecoration();
        decoration.setSize(100, 100);
        connection.setTargetDecoration(decoration);
        connection.setAntialias(SWT.ON);
        infoLabel = new Label(getModel().getFriendlyString());
        infoLabel.setFont(properties.getDefaultEditorFont());
        infoLabel.setForegroundColor(properties.getConnectionLabelColor());
        getGraphLayerFigure().add(infoLabel);
        addFigureMouseEvent(infoLabel);

        tooltipLabel = new Label(getModel().getFriendlyString());
        connection.setToolTip(tooltipLabel);

        return connection;
    }

    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new BIPConnectionEditPolicy());
    }

    private IFigure getGraphLayerFigure() {
        EditPart source = getSource();
        EditPart target = getTarget();
        if (source != null) {
            if (source instanceof ConnectorEditPart) {
                return ((ConnectorEditPart) source).getParent().getFigure();
            } else {
                return ((DiamondEditPart) source).getParent().getFigure();
            }
        } else if (target != null) {
            if (target instanceof BulletEditPart) {
                return ((GraphicalEditPart) ((BulletEditPart) target).getParent().getParent())
                                .getFigure();
            } else if (target instanceof InvisibleBulletEditPart) {
                return ((InvisibleBulletEditPart) target).getParent().getFigure();
            } else if (target instanceof DiamondEditPart) {
                return ((DiamondEditPart) target).getParent().getFigure();
            }
        }
        return null;
    }

    protected Point getSourceLocation() {
        return getModel().getSource().getPositionConstraint().getLocation();
    }

    protected Point getTargetLocation() {
        if (getTarget() instanceof BulletEditPart) {
            Point location = getModel().getTarget().getPositionConstraint().getLocation();
            ComponentModel containerModel = (ComponentModel) getTarget().getParent().getModel();
            Point refPoint = containerModel.getPositionConstraint().getLeft();
            return new Point(location.x + refPoint.x, location.y + refPoint.y);
        }
        return getModel().getTarget().getPositionConstraint().getLocation();
    }

    private void setTooltip() {
        tooltipLabel.setText(getModel().getFriendlyString());
    }

    public ConnectionModel getModel() {
        return (ConnectionModel) super.getModel();
    }

    /**
     * 删除Connection的时候移除相应标签
     */
    public void deactivate() {
        if (getGraphLayerFigure().getChildren().contains(infoLabel)) {
            getGraphLayerFigure().remove(infoLabel);
        }
        super.deactivate();
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        refreshVisuals();
        if (TransitionModel.BEND_POINTS.equals(propertyName)) {
            refreshVisuals();
        } else if (IModel.SOURCE.equals(propertyName) || IModel.TARGET.equals(propertyName)) {
            refreshVisuals();
        }
        setTooltip();
    }

    @Override
    protected void refreshVisuals() {
        if (getSource() != null) {
            if (getSource() instanceof ConnectorEditPart) {
                connection.setForegroundColor(((ConnectorModel) getSource().getModel())
                                .getLineColor());
            } else if (getSource() instanceof DiamondEditPart) {
                connection.setForegroundColor(ColorConstants.black);
            }
        }
        relocateLabels();
        super.refreshVisuals();
    }

    private void relocateLabels() {
        Point source = getSourceLocation();
        Point target = getTargetLocation();
        Point location = new Point((source.x + target.x) >> 1, (source.y + target.y) >> 1);
        Dimension size = infoLabel.getPreferredSize();
        location.translate(-size.width / 2, 0);
        infoLabel.setBounds(new Rectangle(location, size));
    }

    // 双击弹出对话框
    protected void performDoubleClicked() {}
}
