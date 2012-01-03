package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-7<br/>
 * Time: ÏÂÎç7:26<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class GuardModel extends BaseInstanceModel<GuardModel, BaseTypeModel, IContainer> {

    private static final long serialVersionUID = -5027766483277950642L;
    @Element
    private String            guard            = "true";

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

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}
}
