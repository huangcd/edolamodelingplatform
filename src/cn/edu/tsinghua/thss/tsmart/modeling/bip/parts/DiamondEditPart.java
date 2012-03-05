package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;

@SuppressWarnings("rawtypes")
public class DiamondEditPart extends BaseEditableEditPart implements NodeEditPart {
    private static PointList list = new PointList();
    static {
        int x = IModel.DIAMOND_SIZE >> 1;
        list.addPoint(x, 0);
        list.addPoint(x * 2, x);
        list.addPoint(x, x * 2);
        list.addPoint(0, x);
        list.addPoint(x, 0);
    }
    
    private PolygonShape            figure;

    @Override
    protected IFigure createFigure() {
        if (figure == null) {
            figure = new PolygonShape();
            figure.setPoints(list);
        }
        figure.setFill(true);
        figure.setForegroundColor(ColorConstants.black);
        figure.setBackgroundColor(ColorConstants.black);
        figure.setAntialias(SWT.ON);

        return figure;
    }

    public DiamondModel getModel() {
        return (DiamondModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        String propertyName = evt.getPropertyName();
        if (IModel.CONSTRAINT.equals(propertyName)) {
            refreshSourceConnections();
            refreshTargetConnections();
            getModel().getSourceConnection().firePropertyChange(IModel.SOURCE);
            getModel().getTargetConnection().firePropertyChange(IModel.TARGET);
        } else if (IModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
        } else if (IModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        }
    }

    @Override
    protected void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
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
    protected List<ConnectionModel> getModelTargetConnections() {
        return getModel().getTargetConnections();
    }

    @Override
    protected List<ConnectionModel> getModelSourceConnections() {
        return getModel().getSourceConnections();
    }

    @Override
    protected void performDoubleClick() {}

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionEditPolicy());
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
    }
}
