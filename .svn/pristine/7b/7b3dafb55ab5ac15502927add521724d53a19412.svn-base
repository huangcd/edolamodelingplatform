package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:11<br/>
 * 有序容器，模型里面各个子模型次序对模型有影响。如Connector Type的参数、Port Type的参数等。
 */
@SuppressWarnings({"unchecked", "unused"})
public interface IOrderContainer<Child extends IModel> {

    /**
     * 设置第index个参数
     *
     * @param child
     *     要设置的参数
     * @param index
     *     位置
     */
    void setOrderModelChild(Child child, int index);

    /**
     * 是否所有参数都不为null
     *
     * @return 如果所有参数都不为null，返回true；否则返回false
     */
    boolean allSets();

    /**
     * 删除第index个位置的参数，删除之后参数的个数会重新调整
     *
     * @param index
     *     位置
     *
     * @return 删除的参数
     */
    Child removeOrderModelChild(int index);

    List<Child> getOrderList();
}
