package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:00<br/>
 * 容器模型，可以往里面添加子模型。 Atomic Type、Compound Type、Connector Type应该实现这个接口。
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IContainer<Model extends IContainer, Parent extends IContainer, Child extends IModel>
                extends
                    IModel<Model, Parent> {

    /** @return 返回所有的子模型 */
    List<Child> getChildren();

    /**
     * 增加一个子模型
     * 
     * @param child 要添加的子模型
     */
    Model addChild(Child child);

    /**
     * 删除一个子模型
     * 
     * @param child 要删除的子模型
     * 
     * @return 如果要删除的模型不存在或者不能被删除，返回false；否则返回true。
     */
    boolean removeChild(Child child);

    /**
     * 在改变组件名字之前，判断新的名字是否在容器中存在
     * 
     * @param child 待改名字的组件
     * @param newName 新的名字
     * @return 如果名字在组件中已经存在，则返回true（表示修改动作不可进行）
     */
    boolean isNewNameAlreadyExistsInParent(Child child, String newName);
}
