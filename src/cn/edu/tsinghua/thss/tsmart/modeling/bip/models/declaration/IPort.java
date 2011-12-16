package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 18:48<br/>
 * 对外表现为 Port 形式的模型，包括 Port 和 Connector （不是Type）
 * 
 * TODO 将Connector和Port的关系改成has-a，而不是现在的is-a
 */
@SuppressWarnings("rawtypes")
public interface IPort<Model extends IPort, Type extends IPortType, Parent extends IComponentType, DataContainer extends IDataContainer>
                extends
                    IInstance<Model, Type, Parent> {

    /**
     * @return 返回 Port 的参数，对于Port，返回的应该是原来的 arguments<br/>
     *         对于Connector，返回的应该是 export port 的 arguments。
     */
    List<DataModel<DataContainer>> getPortArguments();

    boolean isExport();

    BulletModel getBullet();

    Model setExport(boolean export);
}
