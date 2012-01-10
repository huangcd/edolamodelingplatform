package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;
import java.util.Map;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: обнГ4:15<br/>
 */
@SuppressWarnings("rawtypes")
public interface IDataContainer<Model extends IDataContainer, Instance extends IInstance, Parent extends IContainer, Child extends IModel>
    extends IContainer<Model, Instance, Parent, Child> {

    List<DataModel<Model>> getDatas();
    
    Map<String, List<DataModel>> getDatasGroupByType();
}
