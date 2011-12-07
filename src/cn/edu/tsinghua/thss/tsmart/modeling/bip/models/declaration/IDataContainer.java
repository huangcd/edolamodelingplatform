package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: обнГ4:15<br/>
 */
public interface IDataContainer<Model extends IDataContainer, Parent extends IContainer, Child extends IModel>
    extends IContainer<Model, Parent, Child> {

    List<DataModel<Model>> getDatas();
}
