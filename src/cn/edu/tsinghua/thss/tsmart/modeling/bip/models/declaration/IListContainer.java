package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:54<br/>
 * 列表式容器，子模型通过列表的形式在界面上展现出来。有别于其它模型，是一个纯粹用于展现的模型。
 * 主要是用于存放相同类型的元素（如Atomic Type里面的Data、Port、Priority等）
 */
@SuppressWarnings("rawtypes")
public interface IListContainer<M extends IListContainer, P extends IContainer, C extends IModel>
        extends IContainer<M, P, C>
{
    public enum Display
    {
        SHOW, HIDE
    }

    Display getDisplayType();

    void setDisplayType(Display type);
}
