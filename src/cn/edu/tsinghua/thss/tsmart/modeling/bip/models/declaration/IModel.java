package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;

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
@SuppressWarnings({"unused", "rawtypes"})
public interface IModel<Model extends IModel, Parent extends IContainer>
                extends
                    Serializable,
                    PropertyChangeListener {

    String    CHILDREN           = "children";
    String    PARENT             = "parent";
    String    NAME               = "name";
    String    CONSTRAINT         = "constraint";
    String    ACTION             = "action";
    String    GUARD              = "guard";
    String    TYPE_NAME          = "typeName";
    String    CREATION           = "creation";
    String    ATOMIC_INIT_PLACE  = "initPlace";
    String    ATOMIC_INIT_ACTION = "initAction";
    String    PORT               = "port";
    String    EXPORT_PORT        = "export";
    String    SOURCE             = "source";
    String    TARGET             = "target";
    String    ENTITY             = "tag";
    String    BEND_POINTS        = "bendPoints";
    String    DATA_TYPE          = "dataType";
    String    DATA_VALUE         = "dataValue";
    String    LINE_COLOR         = "lineColor";
    String[]  TRUE_FALSE_ARRAY   = new String[] {"true", "false"};
    /**
     * 小圆点的半径
     */
    int       BULLET_RADIUS      = 8;
    /**
     * 构件的大小
     */
    Dimension COMPONENT_SIZE     = new Dimension(200, 124);
    /**
     * 状态的大小
     */
    Dimension PLACE_SIZE         = new Dimension(20, 20);
    /**
     * 连接子的大小
     */
    Dimension CONNECTOR_SIZE     = new Dimension(30, 30);

    /**
     * @return 返回模型的父模型
     */
    Parent getParent();

    Model setParent(Parent parent);

    /**
     * @return 返回模型的名字
     */
    String getName();

    Model setName(String name);

    /**
     * @return 返回关于模型的注释
     */
    String getComment();

    Model setComment(String comment);

    /**
     * @return 导出成字符串
     */
    String exportToString();

    /**
     * 导出成byte数组
     * 
     * @return 返回实例对象的byte数组描述
     */
    byte[] exportToBytes() throws IOException;

    /**
     * 指示实例是否可被导出成BIP代码
     * 
     * @return 如果导出成BIP文件时当前实例会被导出，则返回true；否则返回false
     */
    boolean exportable();

    /**
     * 指示实例是否可被编辑，内置的类型应该是不可编辑的，比如bool、int的typeName之类的
     * 
     * @return
     */
    boolean editable();

    /** @return 导出成BIP的文本 */
    String exportToBip();

    /**
     * 
     * 模型标签相关接口
     */
    ArrayList<String> getEntityNames();

    Model setEntityNames(ArrayList<String> entityNames);

    Model deleteAllEntityNames();

    /**
     * @return 返回模型的全局唯一ID
     */
    UUID getID();

    /**
     * 重设模型ID
     * 
     * @return
     */
    Model resetID();

    /** @return 返回模型全局唯一ID的字符串表示 */
    String getStringID();

    Rectangle getPositionConstraint();

    Model setPositionConstraint(Rectangle positionConstraint);

    Model addPropertyChangeListener(PropertyChangeListener listener);

    Model firePropertyChange(String propertyName);

    Model removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * 运行时验证模型是否满足所有规则
     * 
     * @return
     */
    boolean validateOnTheFly();

    boolean validateFull();

    /**
     * 生成代码之前验证模型是否满足所有规则
     */
    boolean validate();

    List<Validator> getValidators();

    /**
     * 深度复制模型，包括所有子模型
     * 
     * @return 返回模型的深度复制
     */
    Model copy();
}
