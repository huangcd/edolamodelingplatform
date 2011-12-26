package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;

public class InvisibleBulletEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse figure;

    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }

    @Override
    protected IFigure createFigure() {
        if (figure == null) {
            figure = new Ellipse();
        }
        figure.setFill(false);
        figure.setForegroundColor(ColorConstants.white);
        figure.setBackgroundColor(ColorConstants.white);
        return figure;
    }

    public BulletModel getModel() {
        return (BulletModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        if (IModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        }
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
