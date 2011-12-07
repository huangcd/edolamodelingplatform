package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-7<br/>
 * Time: ÏÂÎç7:23<br/>
 */
@Element
public class TransitionTypeModel
    extends BaseTypeModel<TransitionTypeModel, TransitionModel, IContainer> {

    @Override
    public TransitionModel createInstance() {
        if (instance == null) {
            instance = new TransitionModel().setType(this);
        }
        return instance;
    }

    @Override
    public TransitionTypeModel copy() {
        return new TransitionTypeModel();
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
