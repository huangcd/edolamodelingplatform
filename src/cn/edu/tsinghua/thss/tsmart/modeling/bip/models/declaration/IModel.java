package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelException;

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
@SuppressWarnings({"unused", "rawtypes"})
public interface IModel<Model extends IModel, Parent extends IContainer>
                extends
                    Serializable,
                    PropertyChangeListener {

    public final static String CHILDREN           = "children";
    public final static String PARENT             = "parent";
    public final static String NAME               = "name";
    public final static String CREATION           = "creation";
    public final static String REFRESH            = "refresh";
    public final static String CONSTRAINT         = "constraint";
    public final static String ATOMIC_INIT_PLACE  = "initPlace";
    public final static String ATOMIC_INIT_ACTION = "initAction";
    public final static String PORT               = "port";
    public final static String EXPORT_PORT        = "export";
    /**
     * СԲ��İ뾶
     */
    public static final int    BULLET_RADIUS      = 8;
    public final static int    COMPONENT_WIDTH    = 200;
    public final static int    COMPONENT_HEIGHT   = 124;

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

    /**
     * ָʾʵ���Ƿ�ɱ��༭�����õ�����Ӧ���ǲ��ɱ༭�ģ�����bool��int��typeName֮���
     * 
     * @return
     */
    boolean editable();

    /** @return ������BIP���ı� */
    String exportToBip();

    /** @return ����ģ�͵�ȫ��ΨһID */
    UUID getID();

    Model resetID();

    /** @return ����ģ��ȫ��ΨһID���ַ�����ʾ */
    String getStringID();

    Rectangle getPositionConstraint();

    Model setPositionConstraint(Rectangle positionConstraint);

    Model addPropertyChangeListener(PropertyChangeListener listener);

    Model firePropertyChange(String propertyName);

    Model removePropertyChangeListener(PropertyChangeListener listener);

    // void validateOnTheFly() throws ValidationError;
    // void validate() throws ValidationError;

    /**
     * ��ȸ���ģ�ͣ�����������ģ��
     * 
     * @return ����ģ�͵���ȸ���
     */
    Model copy();
}
