package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-7<br/>
 * Time: ÏÂÎç7:27<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class GuardTypeModel extends BaseTypeModel<GuardTypeModel, GuardModel, IContainer> {

    @Override
    public GuardModel createInstance() {
        if (instance == null) {
            instance = new GuardModel().setType(this);
        }
        return instance;
    }

    @Override
    public GuardTypeModel copy() {
        return new GuardTypeModel();
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
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
    public void setPropertyValue(Object id, Object value) {
    }
}
