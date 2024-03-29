package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureLocator;

@SuppressWarnings("rawtypes")
public class BulletEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse       figure;
    private Label         portName;
    private FigureLocator labelLocator;

    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
        ((GraphicalEditPart) getParent().getParent()).getFigure().remove(portName);
    }

    @Override
    protected IFigure createFigure() {
        if (figure == null) {
            figure = new Ellipse();
        }
        figure.setFill(true);
        figure.setForegroundColor(ColorConstants.black);
        figure.setBackgroundColor(ColorConstants.black);
        figure.setAntialias(SWT.ON);

        if (portName == null) {
            portName = new Label(getModel().getPortDescription());
        }
        portName.setFont(properties.getDefaultEditorFont());
        portName.setForegroundColor(ColorConstants.blue);
        addFigureMouseEvent(portName);

        ((GraphicalEditPart) getParent().getParent()).getFigure().add(portName);
        labelLocator = new FigureLocator(figure, portName, PositionConstants.NORTH, 3);
        return figure;
    }

    public BulletModel getModel() {
        return (BulletModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        String propertyName = evt.getPropertyName();
        if (IModel.CONSTRAINT.equals(propertyName)) {} else if (IModel.NAME.equals(propertyName)) {
            portName.setText(getModel().getPortDescription());
        } else if (IModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        }
    }

    @Override
    protected void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
        Point location = constraint.getLocation();
        Point parentLocation =
                        ((IModel) getParent().getModel()).getPositionConstraint().getLocation();
        location.translate(parentLocation);
        labelLocator.resetDirection(getModel().getDirection()).relocate(
                        new Rectangle(location, constraint.getSize()));
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
        return null;
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    public ConnectionAnchor getSourceConnectionAnchor(Request request) {
        return null;
    }

    @Override
    public ConnectionAnchor getTargetConnectionAnchor(Request request) {
        return new ChopboxAnchor(getFigure());
    }

    @Override
    protected List<ConnectionModel> getModelTargetConnections() {
        return getModel().getTargetConnections();
    }

    @Override
    protected void performDoubleClick() {}

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionEditPolicy());
    }
}
