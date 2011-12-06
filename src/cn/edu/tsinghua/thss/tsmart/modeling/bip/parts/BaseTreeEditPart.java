package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;


public abstract class BaseTreeEditPart extends AbstractTreeEditPart
                implements
                    PropertyChangeListener {

    // override
    public void activate() {
        super.activate();
        if (getModel() instanceof BaseModel)
            ((BaseModel) getModel()).addPropertyChangeListener(this);
        else
            System.err.println("model + " + getModel() + " is not a sub class of AbstractModel");
    }

    // override
    public void deactivate() {
        super.deactivate();
        if (getModel() instanceof BaseModel)
            ((BaseModel) getModel()).removePropertyChangeListener(this);
        else
            System.err.println("model + " + getModel() + " is not a sub class of AbstractModel");
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
