package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ContainerModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.ComponentFigure;


/**
 * Created by Huangcd Date: 11-9-19 Time: 上午8:58
 */
public abstract class ContainerEditPart extends BaseEditableEditPart {
    ComponentFigure figure = null;

    @Override
    protected IFigure createFigure() {
        figure = new ComponentFigure(((BaseModel) getModel()).getName(), this);
        return figure;
    }

    public Dimension getTitleSize() {
        return figure.getTitle().getPreferredSize();
    }

    @Override
    protected List<BaseModel> getModelChildren() {
        return ((ContainerModel) getModel()).getChildren();
    }

    @Override
    protected void refreshVisuals() {
        ContainerModel model = (ContainerModel) getModel();
        Rectangle rect = model.getPositionConstraint();
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        int height = rect.height;
        // 有改变的时候才刷新
        boolean needRefresh = false;
        for (BaseModel child : model.getChildren()) {
            if (child instanceof BulletModel) continue;
            Rectangle childPos = child.getPositionConstraint();
            int childX = childPos.x;
            int childY = childPos.y;
            // 有改变的时候才刷新
            boolean needRefreshChild = false;
            if (childX < 20) {
                x -= 20 - childX;
                childX = 20;
                width += 20 - childX;
                needRefreshChild = true;
                needRefresh = true;
            }
            if (childY < 20) {
                y -= 20 - childY;
                childY = 20;
                height += 20 - childY;
                needRefreshChild = true;
                needRefresh = true;
            }
            int newWidth = childPos.x + childPos.width + 20;
            int newHeight = childPos.y + childPos.height + 20;
            if (newWidth > width) {
                width = newWidth;
                needRefresh = true;
            }
            if (newHeight > height) {
                height = newHeight;
                needRefresh = true;
            }
            if (needRefreshChild)
                child.setPositionConstraint(new Rectangle(new Point(childX, childY), childPos
                                .getSize()));
        }
        if (getParent() instanceof BipEditPart) {
            if (x < 0) {
                x = 0;
                needRefresh = true;
            }
            if (y < 0) {
                y = 0;
                needRefresh = true;
            }
        }
        rect = new Rectangle(x, y, width, height);
        if (needRefresh) model.setPositionConstraint(rect);
        ((BaseGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), rect);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(ContainerModel.CHILDREN)) {
            refresh();
        } else if (BaseModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
            refreshVisuals();
        } else if (BaseModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (BaseModel.NAME.equals(evt.getPropertyName())) {
            ((ComponentFigure) this.getFigure()).setName(((BaseModel) getModel()).getName());
            refreshVisuals();
        } else {
            refresh();
        }
    }
}
