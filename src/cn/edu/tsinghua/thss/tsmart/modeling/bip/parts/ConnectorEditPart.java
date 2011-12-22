package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
            labelLocator.relocate((Rectangle) evt.getNewValue());
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            nameLabel.setText(getModel().getName());
            tooltipLabel.setText(getModel().getName());
            labelLocator.relocate();
        } else if (IModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        } else if (IModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refresh();
        }
    }

    @Override
    protected void performDoubleClick() {
        // TODO Auto-generated method stub

    }

    @Override
    public ConnectorModel getModel() {
        return (ConnectorModel) super.getModel();
    }

    @Override
    protected IFigure createFigure() {
        figure = new ConnectorFigure();
        figure.setForegroundColor(properties.getConnectorColor());

        figure.setOpaque(true);
        figure.setLineWidth(3);

        tooltipLabel = new Label(getModel().getName());
        tooltipLabel.setFont(properties.getDefaultEditorFont());
        figure.setToolTip(tooltipLabel);

        nameLabel = new Label(getModel().getName());
        nameLabel.setFont(properties.getDefaultEditorFont());
        nameLabel.setForegroundColor(properties.getPlaceLabelColor());
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
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }


    /**
     * connectorµÄÐÎ×´
     */
    class ConnectorFigure extends Ellipse {
        public ConnectorFigure() {
            super();
        }

        private Rectangle getInnerBound() {
            float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
            int inset1 = (int) Math.floor(lineInset);
            int inset2 = (int) Math.ceil(lineInset);

            Rectangle rect = Rectangle.SINGLETON.setBounds(getBounds());
            rect.x += 3 * (inset1 + inset2);
            rect.y += 3 * (inset1 + inset2);
            rect.width -= 6 * (inset1 + inset2);
            rect.height -= 6 * (inset1 + inset2);
            return rect;
        }

        protected void outlineShape(Graphics graphics) {
            graphics.setAntialias(SWT.ON);
            super.outlineShape(graphics);
            Rectangle innerBound = getInnerBound();
            graphics.drawOval(innerBound);
        }
    }
}
