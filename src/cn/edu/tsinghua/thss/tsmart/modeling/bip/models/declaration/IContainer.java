package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;
import java.util.regex.Pattern;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午7:00<br/>
 * 容器模型，可以往里面添加子模型。 Atomic Type、Compound Type、Connector Type应该实现这个接口。
 * 
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IContainer<Model extends IContainer, Instance extends IInstance, Parent extends IContainer, Child extends IModel>
                extends
                    IType<Model, Instance, Parent> {
    Pattern NAME_PREFIX = Pattern.compile("^(.*?)(\\d+)$");

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
     * 在改变构件名字之前，判断新的名字是否在容器中存在
     * 
     * @param child 待改名字的构件
     * @param newName 新的名字
     * @return 如果名字在构件中已经存在，则返回true（表示修改动作不可进行）
     */
    boolean isNewNameAlreadyExistsInParent(Child child, String newName);

    /**
     * 添加孩子之前调用，保证孩子不重名
     * 
     * @param child
     */
    void ensureUniqueName(Child child);
}
