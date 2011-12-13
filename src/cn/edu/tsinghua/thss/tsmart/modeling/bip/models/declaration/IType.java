package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:00<br/>
 * ����ģ�ͣ�Atomic Type��Connector Type��Compound Type��Port Type ��Ӧ��ʵ������ӿڡ�<br/>
 * ÿһ�� IInstance ʵ��Ӧ�ö�Ӧ��Ψһһ�� IType ʵ���� IType ʵ���ǿɸ��Ƶġ�
 * �������ϲ��û��ĽǶ�������ģ�ͣ�����˵ģ�飩�ǿɸ��ơ����Ҹ��ƺ���޸ĵġ�
 * �����²���ԣ������ɴ����ʱ��ÿһ��ʵ����Ӧ��һ�����ͣ�Ҳ�����Եû��ҡ�
 */
@SuppressWarnings("rawtypes")
public interface IType<Type extends IType, Instance extends IInstance, Parent extends IContainer>
    extends IModel<Type, Parent> {
    public final static String INSTANCE_NAME = "instanceName";

    /**
     * ����һ������ʵ�����ڴ���֮ǰ�������ȸ��� IType����Ȼ���µ�IInstanceʵ����ITypeʵ������������
     *
     * @return ���͵�һ����ʵ��
     */
    Instance createInstance();

    /** @return ���� IType ģ�Ͷ�Ӧ�� IInstance ģ�� */
    Instance getInstance();
}
