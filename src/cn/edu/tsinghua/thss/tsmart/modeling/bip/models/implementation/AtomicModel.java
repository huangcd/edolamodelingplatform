package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: обнГ9:13<br/>
 */
@SuppressWarnings({"unchecked", "unused"})
@Root
public class AtomicModel extends BaseInstanceModel<AtomicModel, AtomicTypeModel, CompoundTypeModel>
    implements IComponentInstance<AtomicModel, AtomicTypeModel, CompoundTypeModel> {

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return String.format("component %s %s", getType().getName(), getName());
    }
}
