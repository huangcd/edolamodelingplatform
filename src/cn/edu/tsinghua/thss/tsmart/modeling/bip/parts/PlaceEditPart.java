package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.AbsoluteBendpoint;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ConnectionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.FigureLocator;


public class PlaceEditPart extends BaseEditableEditPart implements NodeEditPart {
    private Ellipse       figure;
    private Label         nameLabel;
    private FigureLocator labelLocator;
    private Label         tooltipLabel;

    protected void setAsInitialPlace() {}

    protected void setAsNormalPlace() {}

    public void deactivate() {
        super.deactivate();
        getParent().getFigure().remove(nameLabel);
    }

    @Override
    protected IFigure createFigure() {
        PlaceModel model = getModel();

        figure = new PlaceFigure();
        figure.setForegroundColor(ColorConstants.gray);
        if (model.isInitialPlace()) {
            setAsInitialPlace();
        } else {
            setAsNormalPlace();
        }

        figure.setOpaque(true);
        figure.setLineWidth(2);

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

    public PlaceModel getModel() {
        return (PlaceModel) super.getModel();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ConnectionEditPolicy());
    }

    private void relocateConnections(Rectangle oldValue, Rectangle newValue) {
        if (oldValue == null || newValue == null) return;
        int dx = newValue.x - oldValue.x;
        int dy = newValue.y - oldValue.y;
        for (TransitionModel transition : getModel().getSourceConnections()) {
            if (transition.getSource().equals(transition.getTarget())) {
                for (int i = 0, size = transition.getBendpoints().size(); i < size; i++) {
                    Bendpoint bendpoint = transition.getBendpoint(i);
                    Point point = bendpoint.getLocation();
                    point = new Point(point.x + dx, point.y + dy);
                    transition.setBendpoint(i, new AbsoluteBendpoint(point));
                }
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        getParent().refresh();
        // 位置变更，同时重定位标签的位置
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
            relocateConnections((Rectangle) evt.getOldValue(), (Rectangle) evt.getNewValue());
            labelLocator.relocate((Rectangle) evt.getNewValue());
            for (TransitionModel transition : getModel().getSourceConnections()) {
                transition.firePropertyChange(IModel.SOURCE);
            }
            for (TransitionModel transition : getModel().getTargetConnections()) {
                transition.firePropertyChange(IModel.TARGET);
            }

        }
        // 名字变更，先判断是否重名，同时修改标签和toolTip的名字,并重定位标签的位置
        else if (IModel.NAME.equals(evt.getPropertyName())) {
            if (getModel().getParent() != null) {
                if (!getModel().getParent().isNewNameAlreadyExistsInParent(getModel(),
                                getModel().getName())) {
                    nameLabel.setText(getModel().getName());
                    tooltipLabel.setText(getModel().getName());
                    labelLocator.relocate();
                    try {
                        MessageUtil.clearProblemMessage();
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                } else {
                    MessageUtil.ShowRenameErrorDialog(getModel().getName());
                    getModel().setName(getModel().getOldName());
                    try {
                        MessageUtil.addProblemWarningMessage(getModel().getName());
                    } catch (CoreException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                nameLabel.setText(getModel().getName());
                tooltipLabel.setText(getModel().getName());
                labelLocator.relocate();

            }

        } else if (IModel.ATOMIC_INIT_PLACE.equals(evt.getPropertyName())) {
            PlaceModel model = getModel();
            if (model.isInitialPlace()) {
                setAsInitialPlace();
            } else {
                setAsNormalPlace();
            }
            figure.repaint();
            refresh();
        } else if (IModel.SOURCE.equals(evt.getPropertyName())) {
            refreshSourceConnections();
        } else if (IModel.TARGET.equals(evt.getPropertyName())) {
            refreshTargetConnections();
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
        return getModel().getSourceConnections();
    }

    @Override
    protected List<TransitionModel> getModelTargetConnections() {
        return getModel().getTargetConnections();
    }

    @Override
    protected void performDoubleClick() {}

    /**
     * place的形状，根据place的状态不同而不一样。
     */
    class PlaceFigure extends Ellipse {
        public PlaceFigure() {
            super();
        }

        private Rectangle getInnerBound() {
            float lineInset = Math.max(1.0f, getLineWidthFloat()) / 2.0f;
            int inset1 = (int) Math.floor(lineInset);
            int inset2 = (int) Math.ceil(lineInset);

            Rectangle r = Rectangle.SINGLETON.setBounds(getBounds());
            r.x += 2 * (inset1 + inset2);
            r.y += 2 * (inset1 + inset2);
            r.width -= 4 * (inset1 + inset2);
            r.height -= 4 * (inset1 + inset2);
            return r;
        }

        protected void outlineShape(Graphics graphics) {
            graphics.setAntialias(SWT.ON);
            super.outlineShape(graphics);
            if (getModel().isInitialPlace()) {
                graphics.drawOval(getInnerBound());
            }
        }
    }
}
