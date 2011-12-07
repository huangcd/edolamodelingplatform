package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-5<br/>
 * Time: ÏÂÎç7:27<br/>
 */
@Root
public class ActionTypeModel extends BaseTypeModel<ActionTypeModel, ActionModel, IContainer> {

    @Override
    public ActionModel createInstance() {
        if (instance == null) {
            instance = new ActionModel().setType(this);
        }
        return instance;
    }

    @Override
    public ActionTypeModel copy() {
        return new ActionTypeModel().setName("CopyOf" + getName());
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }
}
