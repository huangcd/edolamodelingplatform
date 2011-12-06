/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.PortAreaChildEditPolicy;


/**
 * 
 * @author: Huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-9 ÉÏÎç8:22:46
 * @project: CereusBip
 * @package: cereusbip.parts
 * @class: PortAreaEditPart.java
 * 
 */
public class PortAreaEditPart extends BaseGraphicalEditPart {
    private TitleBarBorder innerBorder;

    @Override
    protected IFigure createFigure() {
        Panel panel = new Panel();
        innerBorder = new TitleBarBorder("PORTS");
        innerBorder.setTextAlignment(PositionConstants.CENTER);
        innerBorder.setBackgroundColor(ColorConstants.lightGray);
        LineBorder outerBorder = new LineBorder(ColorConstants.lightGray, 1);
        CompoundBorder border = new CompoundBorder(outerBorder, innerBorder);
        panel.setBorder(border);
        panel.setOpaque(true);
        panel.setLayoutManager(new GridLayout());
        return panel;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List getModelChildren() {
        return ((PortAreaModel) getModel()).getChildren();
    }

    @Override
    public void performRequest(Request req) {
        if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)
                        || req.getType().equals(RequestConstants.REQ_OPEN)) {
            PortAreaModel model = (PortAreaModel) getModel();
            model.setShow(!model.isShow());
        } else {
            super.performRequest(req);
        }
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        PortAreaModel model = (PortAreaModel) getModel();
        Rectangle constraint = model.getPositionConstraint();
        int x = constraint.x;
        int y = constraint.y;
        int width = 100;
        int height = 25;
        if (model.isShow()) {
            innerBorder.setLabel("PORTS <<");
            for (Object child : getChildren()) {
                if (child instanceof PortEditPart) {
                    PortEditPart item = (PortEditPart) child;
                    Dimension size = item.getFigure().getPreferredSize();
                    width = Math.max(width, size.width);
                    height += size.height + 6;
                }
            }
        } else {
            innerBorder.setLabel("PORTS >>");
        }
        ((BaseGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), new Rectangle(
                        x, y, width + 10, height));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new PortAreaChildEditPolicy());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans. PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (PortAreaModel.CHILDREN.equals(evt.getPropertyName())) {
            refresh();
        } else if (PortAreaModel.CONSTRAINT.equals(evt.getPropertyName())) {
            ((BaseGraphicalEditPart) getParent()).refresh();
            refreshVisuals();
        } else if (BaseModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        }
        refresh();
    }

    @Override
    protected void registerVisuals() {
        super.registerVisuals();
        Rectangle rect = ((PortAreaModel) getModel()).getPositionConstraint();
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rect);
    }
}
