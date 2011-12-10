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
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.TransitionEditPolicy;


public class PlaceEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse figure;
    private Label   tooltipLabel;

    protected void setAsInitialPlace() {
        figure.setForegroundColor(ColorConstants.green);
    }

    protected void setAsNormalPlace() {
        figure.setForegroundColor(ColorConstants.gray);
    }

    @Override
    protected IFigure createFigure() {
        PlaceModel model = getModel();

        figure = new Ellipse();
        figure.setOpaque(true);
        figure.setLineWidth(2);

        if (model.isInitialPlace()) {
            setAsInitialPlace();
        } else {
            setAsNormalPlace();
        }
        tooltipLabel = new Label(getModel().getName());
        figure.setToolTip(tooltipLabel);
        return figure;
    }

    public PlaceModel getModel() {
        return (PlaceModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new TransitionEditPolicy());
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
            tooltipLabel.setText((getModel()).getName());
        } else if (AtomicTypeModel.INIT_PLACE.equals(evt.getPropertyName())) {
            PlaceModel model = getModel();
            if (model.isInitialPlace()) {
                setAsInitialPlace();
            } else {
                setAsNormalPlace();
            }
            refresh();
        } else if (PlaceModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        } else if (PlaceModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        }
        refreshVisuals();
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
        return (getModel()).getTargetConnections();
    }

    @Override
    protected void performDoubleClick() {
        performDirectEdit();
    }
}
