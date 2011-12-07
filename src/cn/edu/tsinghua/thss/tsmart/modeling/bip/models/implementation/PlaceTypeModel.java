package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-5<br/>
 * Time: ÏÂÎç10:08<br/>
 */
@Root
public class PlaceTypeModel extends BaseTypeModel<PlaceTypeModel, PlaceModel, IContainer> {

    @Override
    public PlaceModel createInstance() {
        if (instance == null) {
            instance = new PlaceModel().setType(this).setName(getName());
        }
        return instance;
    }

    @Override
    public PlaceTypeModel copy() {
        return new PlaceTypeModel().setName("CopyOf" + getName());
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
