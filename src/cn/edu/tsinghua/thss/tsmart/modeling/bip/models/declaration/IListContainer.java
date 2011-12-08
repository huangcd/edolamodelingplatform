package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:54<br/>
 * �б�ʽ��������ģ��ͨ���б����ʽ�ڽ�����չ�ֳ������б�������ģ�ͣ���һ����������չ�ֵ�ģ�͡�
 * ��Ҫ�����ڴ����ͬ���͵�Ԫ�أ���Atomic Type�����Data��Port��Priority�ȣ�
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
