package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: 上午8:50<br/>
 */
@SuppressWarnings("rawtypes")
public interface IComponentType<Model extends IComponentType, Instance extends IComponentInstance, Parent extends IContainer, Child extends IModel>
    extends IContainer<Model, Parent, Child>, IType<Model, Instance, Parent> {

    /** @return 返回export port的集合（组件对外可见的端口集） */
    List<IPort> getExportPorts();
}
