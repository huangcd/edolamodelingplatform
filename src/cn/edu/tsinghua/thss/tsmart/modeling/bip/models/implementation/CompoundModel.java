package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: обнГ9:15<br/>
 */
@Root
public class CompoundModel extends BaseInstanceModel<CompoundModel, CompoundTypeModel, IContainer>
    implements IComponentInstance<CompoundModel, CompoundTypeModel, IContainer> {

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return String.format("component %s %s", getType().getName(), getName());
    }
}
