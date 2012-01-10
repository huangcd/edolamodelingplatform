package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureLocator;

public class ConnectorEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse       figure;
    private Label         nameLabel;
    private FigureLocator labelLocator;
    private Label         tooltipLabel;

    public void deactivate() {
        super.deactivate();
        getParent().getFigure().remove(nameLabel);
    }

    private String getTooltipText() {
        return getModel().getType().exportToBip().replaceAll("[\r\n]+\t", "\n");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
            labelLocator.relocate((Rectangle) evt.getNewValue());
            relocateInvisibleBullet((Rectangle) evt.getOldValue(), (Rectangle) evt.getNewValue());
            for (ConnectionModel transition : getModel().getSourceConnections()) {
                transition.firePropertyChange(IModel.SOURCE);
            }
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            if (!getModel().getParent().isNewNameAlreadyExistsInParent(getModel(),
                            getModel().getName())) {
                nameLabel.setText(getModel().getName());
                labelLocator.relocate();
            } else {
                MessageUtil.ShowRenameErrorDialog(getModel().getName());
                getModel().setName(getModel().getOldName());
            }

        } else if (IModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        } else if (ConnectorModel.LINE_COLOR.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        }
    }

    @SuppressWarnings("unchecked")
    private void relocateInvisibleBullet(Rectangle oldBounds, Rectangle newBounds) {
        int dx = newBounds.x - oldBounds.x;
        int dy = newBounds.y - oldBounds.y;
        List<ConnectionEditPart> connections = getSourceConnections();
        for (ConnectionEditPart part : connections) {
            EditPart target = part.getTarget();
            if (target instanceof InvisibleBulletEditPart) {
                InvisibleBulletModel bullet = (InvisibleBulletModel) target.getModel();
                Rectangle rect = bullet.getPositionConstraint();
                rect.setX(rect.x + dx);
                rect.setY(rect.y + dy);
                bullet.setPositionConstraint(rect);
                ((InvisibleBulletEditPart) target).refreshVisuals();
            }
        }

    }

    @Override
    protected void performDoubleClick() {}

    @Override
    public ConnectorModel getModel() {
        return (ConnectorModel) super.getModel();
    }

    @Override
    protected IFigure createFigure() {
        // figure = new ConnectorFigure();
        figure = new Ellipse();
        figure.setForegroundColor(properties.getConnectorOutlineColor());
        figure.setBackgroundColor(properties.getConnectorFillColor());
        figure.setFill(true);
        figure.setOpaque(true);
        figure.setLineWidth(3);
        figure.setAntialias(SWT.ON);

        tooltipLabel = new Label(getTooltipText());
        tooltipLabel.setFont(properties.getDefaultEditorFont());
        figure.setToolTip(tooltipLabel);

        nameLabel = new Label(getModel().getName());
        nameLabel.setFont(properties.getDefaultEditorFont());
        nameLabel.setForegroundColor(properties.getConnectorColor());
        addFigureMouseEvent(nameLabel);

        labelLocator = new FigureLocator(figure, nameLabel, PositionConstants.NORTH);
        labelLocator.relocate(getModel().getPositionConstraint());

        getParent().getFigure().add(nameLabel);
        return figure;
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionEditPolicy());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return null;
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return null;
    }

    @Override
    protected List<ConnectionModel> getModelSourceConnections() {
        return getModel().getSourceConnections();
    }

    /**
     * connectorµÄÐÎ×´
     */
    class ConnectorFigure extends Ellipse {
        public ConnectorFigure() {
            super();
        }

        protected void outlineShape(Graphics graphics) {
            graphics.setAntialias(SWT.ON);
            super.outlineShape(graphics);
        }
    }
}
