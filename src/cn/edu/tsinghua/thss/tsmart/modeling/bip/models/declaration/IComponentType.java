package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: ����8:50<br/>
 */
@SuppressWarnings("rawtypes")
public interface IComponentType<Model extends IComponentType, Instance extends IComponentInstance, Parent extends IContainer, Child extends IModel>
    extends IContainer<Model, Parent, Child>, IType<Model, Instance, Parent> {

    /** @return ����export port�ļ��ϣ��������ɼ��Ķ˿ڼ��� */
    List<IPort> getExportPorts();
}
