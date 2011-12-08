package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.ComponentFigure;


/**
 * Created by Huangcd</br> Date: 11-9-19</br> Time: 上午8:58</br>
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ContainerEditPart extends BaseEditableEditPart {
    ComponentFigure figure = null;

    @Override
    protected IFigure createFigure() {
        figure = new ComponentFigure(getModel().getName(), this);
        return figure;
    }

    public Dimension getTitleSize() {
        return figure.getTitle().getPreferredSize();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        return getModel().getModelChildren();
    }

    public IComponentType<IComponentType, IComponentInstance, IContainer, IInstance> getModel() {
        return (IComponentType<IComponentType, IComponentInstance, IContainer, IInstance>) super
                        .getModel();
    }

    @Override
    // TODO 新的Container没有框框这种东西。。。
    protected void refreshVisuals() {
        IComponentType<IComponentType, IComponentInstance, IContainer, IInstance> model =
                        getModel();
        Rectangle rect = model.getPositionConstraint();
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        int height = rect.height;
        // 有改变的时候才刷新
        boolean needRefresh = false;
        for (IInstance child : model.getModelChildren()) {
                child.setPositionConstraint(new Rectangle(10, 10, -1, -1));
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
        if (evt.getPropertyName().equals(IModel.CHILDREN)) {
            refresh();
        } else if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            getParent().refresh();
            refreshVisuals();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            ((ComponentFigure) this.getFigure()).setName(getModel().getName());
            refreshVisuals();
        } else {
            refresh();
        }
    }
}
