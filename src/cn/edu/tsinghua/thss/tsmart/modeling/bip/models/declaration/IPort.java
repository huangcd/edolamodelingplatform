package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: 18:48<br/>
 * �������Ϊ Port ��ʽ��ģ�ͣ����� Port �� Connector ������Type��
 * 
 * TODO ��Connector��Port�Ĺ�ϵ�ĳ�has-a�����������ڵ�is-a
 */
@SuppressWarnings("rawtypes")
public interface IPort<Model extends IPort, Type extends IPortType, Parent extends IComponentType, DataContainer extends IDataContainer>
                extends
                    IInstance<Model, Type, Parent> {

    /**
     * @return ���� Port �Ĳ���������Port�����ص�Ӧ����ԭ���� arguments<br/>
     *         ����Connector�����ص�Ӧ���� export port �� arguments��
     */
    List<DataModel<DataContainer>> getPortArguments();

    boolean isExport();

    BulletModel getBullet();

    Model setExport(boolean export);
}
