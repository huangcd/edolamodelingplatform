package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

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
        return (IComponentType<IComponentType, IComponentInstance, IContainer, IInstance>) super.getModel();
    }

    @Override
    // TODO 新的Container没有框框这种东西。。。
    protected void refreshVisuals() {
        IComponentType<IComponentType, IComponentInstance, IContainer, IInstance> model = getModel();
        // 新的
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
