package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;

import java.beans.PropertyChangeListener;

/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-28 ÏÂÎç10:35:19
 * @project CereusBip
 * @package cereusbip.parts
 * @class BaseGraphicalEditPart.java
 */
public abstract class BaseGraphicalEditPart extends AbstractGraphicalEditPart
                implements
                    PropertyChangeListener {
    public void activate() {
        super.activate();
        if (getModel() instanceof BaseModel)
            ((BaseModel) getModel()).addPropertyChangeListener(this);
    }

    public void deactivate() {
        super.deactivate();
        if (getModel() instanceof BaseModel)
            ((BaseModel) getModel()).removePropertyChangeListener(this);
    }
}
