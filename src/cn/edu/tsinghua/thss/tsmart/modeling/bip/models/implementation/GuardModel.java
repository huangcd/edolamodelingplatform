package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-7<br/>
 * Time: ÏÂÎç7:26<br/>
 */
@Root
public class GuardModel extends BaseInstanceModel<GuardModel, GuardTypeModel, IContainer> {

    @Element
    private String guard;

    public String getGuard() {
        return guard;
    }

    public GuardModel setGuard(String guard) {
        this.guard = guard;
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return getGuard();
    }
}
