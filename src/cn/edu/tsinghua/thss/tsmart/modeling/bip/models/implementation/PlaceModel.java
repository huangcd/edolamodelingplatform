package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Root;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: обнГ7:07<br/>
 */
@Root
public class PlaceModel extends BaseInstanceModel<PlaceModel, PlaceTypeModel, AtomicTypeModel> {

    @Override
    public String exportToBip() {
        return String.format("place %s", getName());
    }

    @Override
    public boolean exportable() {
        return true;
    }
}
