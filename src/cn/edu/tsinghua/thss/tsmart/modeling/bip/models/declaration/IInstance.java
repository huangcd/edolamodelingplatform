package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:19<br/>
 * ʵ��ģ�ͣ���Atomic��Compound��Connector��Port��<br/>
 * IInstance ���汣��������Ϣ������ģ�͵�λ�ã�ģ�͵�ʵ����Ϣ
 * ����Atomic����������Compound��������֣�Component SyncScheduler scheduler �е� scheduler��
 * Port��Atomic�й�������Щ���ݡ�Connector��������ЩPort�ȣ�
 */
@SuppressWarnings("rawtypes")
public interface IInstance<M extends IInstance, T extends IType, P extends IContainer>
        extends IModel<M, P>
{
    /**
     * 
     * @return ����ʵ��������
     */
    T getType();

    /**
     * 
     * @return ��ȸ���һ��ʵ��
     */
    M copy();
}
