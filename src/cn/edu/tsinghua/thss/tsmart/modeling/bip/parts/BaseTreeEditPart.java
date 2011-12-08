package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public abstract class BaseTreeEditPart extends AbstractTreeEditPart
                implements
                    PropertyChangeListener {

    // override
    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
    }

    public IModel getModel() {
        return (IModel) super.getModel();
    }

    // override
    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * if (evt.getPropertyName().equals(ContainerModel.CHILDREN)) refresh();
         * if(evt.getPropertyName().equals(BaseModel.NAME)) refreshVisuals();
         */
    }

    // 必须重写，否则refreshChildren时会出错
    @Override
    public void refresh() {
        /*
         * refreshVisuals(); refreshChildren();
         */
    }
}
