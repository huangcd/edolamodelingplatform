package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 下午6:55<br/>
 * 模型的基类。对每一个模型而言，应该具有如下信息或功能：
 * <ul>
 * <li>一个继承自IContainer的父模型</li>
 * <li>模型的名字</li>
 * <li>一个全局唯一的ID</li>
 * <li>模型改变的时候可以通知其它相关模型的通知机制</li>
 * <li>导出、导入功能</li>
 * </ul>
 */
@SuppressWarnings({"unused","rawtypes"})
public interface IModel<Model extends IModel, Parent extends IContainer> extends Serializable {

    public final static String POSITION = "position";
    public final static String CHILDREN = "children";
    public final static String PARENT = "parent";
    public final static String NAME = "name";
    public final static String CREATE = "create";
    public final static String REFRESH = "refresh";
    public static final String CONSTRAINT = "constraint";

    /** @return 返回模型的父模型 */
    Parent getParent();

    Model setParent(Parent parent);

    String getName();

    Model setName(String newName);

    /** @return 导出成字符串 */
    String exportToString();

    /**
     * 导出成byte数组
     *
     * @return 返回实例对象的byte数组描述
     */
    byte[] exportToBytes() throws IOException;

    /**
     * 指示实例是否可被导出
     *
     * @return 如果导出成BIP文件时当前实例会被导出，则返回true；否则返回false
     */
    boolean exportable();

    /** @return 导出成BIP的文本 */
    String exportToBip();

    /** @return 返回模型的全局唯一ID */
    UUID getID();

    /** @return 返回模型全局唯一ID的字符串表示 */
    String getStringID();

}
