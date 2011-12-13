package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:00<br/>
 * 类型模型，Atomic Type、Connector Type、Compound Type、Port Type 等应该实现这个接口。<br/>
 * 每一个 IInstance 实例应该对应着唯一一个 IType 实例。 IType 实例是可复制的。
 * 这样从上层用户的角度来看，模型（或者说模块）是可复制、而且复制后可修改的。
 * 对于下层而言，在生成代码的时候每一个实例对应着一个类型，也不会显得混乱。
 */
@SuppressWarnings("rawtypes")
public interface IType<Type extends IType, Instance extends IInstance, Parent extends IContainer>
    extends IModel<Type, Parent> {
    public final static String INSTANCE_NAME = "instanceName";

    /**
     * 创建一个类型实例，在创建之前，必须先复制 IType自身，然后将新的IInstance实例和IType实例关联起来。
     *
     * @return 类型的一个新实例
     */
    Instance createInstance();

    /** @return 返回 IType 模型对应的 IInstance 模型 */
    Instance getInstance();
}
