package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings("rawtypes")
public abstract class ComponentTypeModel<Model extends ComponentTypeModel, Instance extends ComponentModel>
                extends BaseTypeModel<Model, Instance, IContainer>
                implements
                    IContainer<Model, IContainer, IInstance>,
                    IType<Model, Instance, IContainer> {

    private static final long serialVersionUID = -3172665284154222983L;

    /**
     * @return ����export port�ļ��ϣ���������ɼ��Ķ˿ڼ���
     */
    public abstract List<PortModel> getExportPorts();

}
