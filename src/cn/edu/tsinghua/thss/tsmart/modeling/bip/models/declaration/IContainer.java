package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;
import java.util.regex.Pattern;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����7:00<br/>
 * ����ģ�ͣ����������������ģ�͡� Atomic Type��Compound Type��Connector TypeӦ��ʵ������ӿڡ�
 * 
 */
@SuppressWarnings({"unused", "rawtypes"})
public interface IContainer<Model extends IContainer, Instance extends IInstance, Parent extends IContainer, Child extends IModel>
                extends
                    IType<Model, Instance, Parent> {
    Pattern NAME_PREFIX = Pattern.compile("^(.*?)(\\d+)$");

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
     * �ڸı乹������֮ǰ���ж��µ������Ƿ��������д���
     * 
     * @param child �������ֵĹ���
     * @param newName �µ�����
     * @return ��������ڹ������Ѿ����ڣ��򷵻�true����ʾ�޸Ķ������ɽ��У�
     */
    boolean isNewNameAlreadyExistsInParent(Child child, String newName);

    /**
     * ��Ӻ���֮ǰ���ã���֤���Ӳ�����
     * 
     * @param child
     */
    void ensureUniqueName(Child child);
}
