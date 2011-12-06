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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.DataAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DataAreaChildEditPolicy;


/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-7-3 ÉÏÎç10:38:08
 * @project: CereusBip
 * @package: cereusbip.parts
 * @class: DataAreaEditPart.java
 * 
 */
public class DataAreaEditPart extends BaseGraphicalEditPart {
    private TitleBarBorder innerBorder;

    @Override
    protected IFigure createFigure() {
        Panel panel = new Panel();
        innerBorder = new TitleBarBorder("DATAS");
        innerBorder.setTextAlignment(PositionConstants.CENTER);
        innerBorder.setBackgroundColor(ColorConstants.lightGray);
        LineBorder outerBorder = new LineBorder(ColorConstants.lightGray, 1);
        CompoundBorder border = new CompoundBorder(outerBorder, innerBorder);
        panel.setBorder(border);
        panel.setOpaque(true);
        panel.setLayoutManager(new GridLayout());
        return panel;
    }

    @Override
    public void performRequest(Request req) {
        if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)
                        || req.getType().equals(RequestConstants.REQ_OPEN)) {
            DataAreaModel model = (DataAreaModel) getModel();
            model.setShow(!model.isShow());
        } else {
            super.performRequest(req);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected List getModelChildren() {
        return ((DataAreaModel) getModel()).getChildren();
    }

    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        DataAreaModel model = (DataAreaModel) getModel();
        Rectangle constraint = model.getPositionConstraint();
        int x = constraint.x;
        int y = constraint.y;
        int width = 100;
        int height = 25;
        if (model.isShow()) {
            innerBorder.setLabel("DATA <<");
            for (Object child : getChildren()) {
                if (child instanceof DataEditPart) {
                    DataEditPart item = (DataEditPart) child;
                    Dimension size = item.getFigure().getPreferredSize();
                    width = Math.max(width, size.width);
                    height += size.height + 6;
                }
            }
        } else {
            innerBorder.setLabel("DATA >>");
        }
        ((BaseGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), new Rectangle(
                        x, y, width + 10, height));
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new DataAreaChildEditPolicy());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (DataAreaModel.CHILDREN.equals(evt.getPropertyName())) {
            refresh();
        } else if (DataAreaModel.CONSTRAINT.equals(evt.getPropertyName())) {
            ((BaseGraphicalEditPart) getParent()).refresh();
            refreshVisuals();
        } else if (DataAreaModel.SHOW.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (BaseModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        }
    }

    @Override
    protected void registerVisuals() {
        super.registerVisuals();
        Rectangle rect = ((DataAreaModel) getModel()).getPositionConstraint();
        ((GraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rect);
    }
}
