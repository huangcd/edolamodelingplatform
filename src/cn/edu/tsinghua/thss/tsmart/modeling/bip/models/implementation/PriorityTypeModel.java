package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-29<br/>
 * Time: ÏÂÎç9:37<br/>
 */
@Root
@SuppressWarnings({"unchecked"})
public class PriorityTypeModel<Parent extends IContainer>
    extends BaseTypeModel<PriorityTypeModel, PriorityModel, Parent> {

    @Override
    public PriorityModel createInstance() {
        if (instance == null) {
            instance = (PriorityModel) new PriorityModel().setType(this);
        }
        return instance;
    }

    @Override
    public PriorityTypeModel copy() {
        return new PriorityTypeModel();
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
