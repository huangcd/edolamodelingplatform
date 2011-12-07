package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:19<br/>
 * 实例模型，如Atomic、Compound、Connector、Port等<br/>
 * IInstance 里面保存具体的信息，比如模型的位置，模型的实例信息
 * （如Atomic在其所属的Compound里面的名字：Component SyncScheduler scheduler 中的 scheduler、
 * Port在Atomic中关联了那些数据、Connector关联了那些Port等）
 */
@SuppressWarnings("rawtypes")
public interface IInstance<M extends IInstance, T extends IType, P extends IContainer>
        extends IModel<M, P>
{
    /**
     * 
     * @return 返回实例的类型
     */
    T getType();

    /**
     * 
     * @return 深度复制一个实例
     */
    M copy();
}
