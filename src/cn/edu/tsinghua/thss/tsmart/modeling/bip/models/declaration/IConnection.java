package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ÏÂÎç9:40<br/>
 * Connection Ä£ÐÍ
 */
public interface IConnection<M extends IConnection, P extends IContainer, S extends IInstance, T extends IInstance>
        extends IModel<M, P>
{
    S getSource();

    T getTarget();

    void attachSource(S source);

    void attachTarget(T target);
}
