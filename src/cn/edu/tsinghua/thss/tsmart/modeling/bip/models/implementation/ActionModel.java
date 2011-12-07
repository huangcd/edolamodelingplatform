package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ÏÂÎç3:14<br/>
 */
@Root
public class ActionModel extends BaseInstanceModel<ActionModel, ActionTypeModel, IContainer> {

    @Element(required = false)
    private String action;

    public String getAction() {
        return action;
    }

    public ActionModel setAction(String action) {
        this.action = action;
        firePropertyChange(CHILDREN);
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return getAction();
    }
}
