package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;


public class PlaceEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse figure;
    private Label   label;

    @Override
    protected IFigure createFigure() {
        PlaceModel model = getModel();

        figure = new Ellipse();
        figure.setSize(10, 10);
        figure.setOpaque(true);

        if (model.isInitialPlace()) {
            figure.setForegroundColor(ColorConstants.lightBlue);
            figure.setBackgroundColor(ColorConstants.lightBlue);
        } else {
            figure.setForegroundColor(ColorConstants.lightGreen);
            figure.setBackgroundColor(ColorConstants.lightGreen);
        }
        label = new Label(getModel().getName());
        figure.setToolTip(label);
        return figure;
    }

    public PlaceModel getModel() {
        return (PlaceModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {
        // installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeletePlaceEditPolicy());
        // installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TransitionEditPolicy());
        // installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CustomDirectEditPolicy()); // ֱ�ӱ༭
    }

    @Override
    protected void refreshVisuals() {
        Rectangle constraint = ((PlaceModel) getModel()).getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            ((BaseGraphicalEditPart) getParent()).refresh();
            refreshVisuals();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            label.setText((getModel()).getName());
        } else if (PlaceModel.INITIAL.equals(evt.getPropertyName())) {
            PlaceModel model = getModel();
            if (model.isInitialPlace()) {
                label.setBackgroundColor(ColorConstants.lightBlue);
            } else {
                label.setBackgroundColor(ColorConstants.lightGreen);
            }
            refresh();
        } else if (PlaceModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        } else if (PlaceModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        }
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

    @Override
    protected List<TransitionModel> getModelSourceConnections() {
        return (getModel()).getSourceConnections();
    }

    @Override
    protected List<TransitionModel> getModelTargetConnections() {
        return (getModel()).getSourceConnections();
    }

    @Override
    protected void performDoubleClick() {
        performDirectEdit();
    }
}
