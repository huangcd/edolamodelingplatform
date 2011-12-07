package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:11<br/>
 * ����������ģ�����������ģ�ʹ����ģ����Ӱ�졣��Connector Type�Ĳ�����Port Type�Ĳ����ȡ�
 */
@SuppressWarnings({"unchecked", "unused"})
public interface IOrderContainer<Child extends IModel> {

    /**
     * ���õ�index������
     *
     * @param child
     *     Ҫ���õĲ���
     * @param index
     *     λ��
     */
    void setOrderModelChild(Child child, int index);

    /**
     * �Ƿ����в�������Ϊnull
     *
     * @return ������в�������Ϊnull������true�����򷵻�false
     */
    boolean allSets();

    /**
     * ɾ����index��λ�õĲ�����ɾ��֮������ĸ��������µ���
     *
     * @param index
     *     λ��
     *
     * @return ɾ���Ĳ���
     */
    Child removeOrderModelChild(int index);

    List<Child> getOrderList();
}
