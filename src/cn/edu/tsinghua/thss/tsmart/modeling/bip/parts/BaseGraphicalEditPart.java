package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-28 ÏÂÎç10:35:19
 * @project CereusBip
 * @package cereusbip.parts
 * @class BaseGraphicalEditPart.java
 */
@SuppressWarnings("rawtypes")
public abstract class BaseGraphicalEditPart extends AbstractGraphicalEditPart
                implements
                    PropertyChangeListener {
    protected GlobalProperties properties = GlobalProperties.getInstance();

    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
    }

    public IModel getModel() {
        return (IModel) super.getModel();
    }

    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }
}
