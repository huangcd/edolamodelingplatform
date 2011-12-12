package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:00<br/>
 * ����ģ�ͣ����������������ģ�͡� Atomic Type��Compound Type��Connector TypeӦ��ʵ������ӿڡ�
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IContainer<Model extends IContainer, Parent extends IContainer, Child extends IModel>
                extends
                    IModel<Model, Parent> {

    /** @return �������е���ģ�� */
    List<Child> getChildren();

    /**
     * ����һ����ģ��
     * 
     * @param child Ҫ��ӵ���ģ��
     */
    Model addChild(Child child);

    /**
     * ɾ��һ����ģ��
     * 
     * @param child Ҫɾ������ģ��
     * 
     * @return ���Ҫɾ����ģ�Ͳ����ڻ��߲��ܱ�ɾ��������false�����򷵻�true��
     */
    boolean removeChild(Child child);

    /**
     * �ڸı��������֮ǰ���ж��µ������Ƿ��������д���
     * 
     * @param child �������ֵ����
     * @param newName �µ�����
     * @return ���������������Ѿ����ڣ��򷵻�true����ʾ�޸Ķ������ɽ��У�
     */
    boolean isNewNameAlreadyExistsInParent(Child child, String newName);
}
