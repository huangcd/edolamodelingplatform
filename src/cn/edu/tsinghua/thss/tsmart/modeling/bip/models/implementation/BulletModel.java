package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

/**
 * СԲ�㣬���ڱ�ʾexport port
 * 
 * @author huangcd
 * 
 */
@SuppressWarnings("rawtypes")
public class BulletModel extends BaseInstanceModel<BulletModel, IType, IContainer> {
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
    public void setPropertyValue(Object id, Object value) {}
}
