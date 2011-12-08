package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����6:55<br/>
 * ģ�͵Ļ��ࡣ��ÿһ��ģ�Ͷ��ԣ�Ӧ�þ���������Ϣ���ܣ�
 * <ul>
 * <li>һ���̳���IContainer�ĸ�ģ��</li>
 * <li>ģ�͵�����</li>
 * <li>һ��ȫ��Ψһ��ID</li>
 * <li>ģ�͸ı��ʱ�����֪ͨ�������ģ�͵�֪ͨ����</li>
 * <li>���������빦��</li>
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

    /** @return ����ģ�͵ĸ�ģ�� */
    Parent getParent();

    Model setParent(Parent parent);

    String getName();

    Model setName(String newName);

    /** @return �������ַ��� */
    String exportToString();

    /**
     * ������byte����
     *
     * @return ����ʵ�������byte��������
     */
    byte[] exportToBytes() throws IOException;

    /**
     * ָʾʵ���Ƿ�ɱ�����
     *
     * @return ���������BIP�ļ�ʱ��ǰʵ���ᱻ�������򷵻�true�����򷵻�false
     */
    boolean exportable();

    /** @return ������BIP���ı� */
    String exportToBip();

    /** @return ����ģ�͵�ȫ��ΨһID */
    UUID getID();

    /** @return ����ģ��ȫ��ΨһID���ַ�����ʾ */
    String getStringID();

}
