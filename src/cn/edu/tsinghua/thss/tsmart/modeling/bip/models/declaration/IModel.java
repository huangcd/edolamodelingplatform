package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;

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

    String    CHILDREN           = "children";
    String    PARENT             = "parent";
    String    NAME               = "name";
    String    CONSTRAINT         = "constraint";
    String    ACTION             = "action";
    String    GUARD              = "guard";
    String    TYPE_NAME          = "typeName";
    String    CREATION           = "creation";
    String    ATOMIC_INIT_PLACE  = "initPlace";
    String    ATOMIC_INIT_ACTION = "initAction";
    String    PORT               = "port";
    String    EXPORT_PORT        = "export";
    String    SOURCE             = "source";
    String    TARGET             = "target";
    String    ENTITY             = "tag";
    String    BEND_POINTS        = "bendPoints";
    String    DATA_TYPE          = "dataType";
    String    DATA_VALUE         = "dataValue";
    String    LINE_COLOR         = "lineColor";
    String[]  TRUE_FALSE_ARRAY   = new String[] {"true", "false"};
    /**
     * TODO �ǲ���Ӧ�ð����ɾ����
     */
    String[]  TAGS               = new String[] {"$UNBOUND$", "sys", "sensor", "gate"};
    String[]  COMPONENT_TAGS     = new String[] {"$UNBOUND$", "SwC", "EC", "Main", "Constraints",
                    "Functional", "Dev", "User", "HW"};
    String[]  DATA_TAGS          = new String[] {"$UNBOUND$", "SL", "HL", "Vmin", "Vmax", "Smin",
                    "Smax"       };
    String[]  PORT_TAGS          = new String[] {"$UNBOUND$", "SC", "FC"};
    String[]  CONNECTOR_TAGS     = new String[] {"$UNBOUND$", "TickCon", "ReadCon", "WriteCon",
                    "StartCycleCon", "FinishCycleCon", "FunctionCallCon", "DataRequestCon",
                    "DataReplyCon"};
    /**
     * СԲ��İ뾶
     */
    int       BULLET_RADIUS      = 8;
    /**
     * �����Ĵ�С
     */
    Dimension COMPONENT_SIZE     = new Dimension(200, 124);
    /**
     * ״̬�Ĵ�С
     */
    Dimension PLACE_SIZE         = new Dimension(20, 20);
    /**
     * �����ӵĴ�С
     */
    Dimension CONNECTOR_SIZE     = new Dimension(30, 30);

    /**
     * @return ����ģ�͵ĸ�ģ��
     */
    Parent getParent();

    Model setParent(Parent parent);

    /**
     * @return ����ģ�͵�����
     */
    String getName();

    Model setName(String name);
    
    /**
     * @return ���ع���ģ�͵�ע��
     */
    String getComment();
    
    Model setComment(String comment);

    /**
     * @return �������ַ���
     */
    String exportToString();

    /**
     * ������byte����
     * 
     * @return ����ʵ�������byte��������
     */
    byte[] exportToBytes() throws IOException;

    /**
     * ָʾʵ���Ƿ�ɱ�������BIP����
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

    /**
     * 
     * ģ�ͱ�ǩ��ؽӿ�
     */
    ArrayList<String> getEntityNames();
//    Model addEntityName(String entityName);
    Model setEntityNames(ArrayList<String> entityNames);
//    Model deleteEntityName(String entityName);
    Model deleteAllEntityNames();
//    String getEntityDisplayName();

    /**
     * @return ����ģ�͵�ȫ��ΨһID
     */
    UUID getID();

    /**
     * ����ģ��ID
     * 
     * @return
     */
    Model resetID();

    /** @return ����ģ��ȫ��ΨһID���ַ�����ʾ */
    String getStringID();

    Rectangle getPositionConstraint();

    Model setPositionConstraint(Rectangle positionConstraint);

    Model addPropertyChangeListener(PropertyChangeListener listener);

    Model firePropertyChange(String propertyName);

    Model removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * ����ʱ��֤ģ���Ƿ��������й���
     * 
     * @return
     */
    boolean validateOnTheFly();

    boolean validateFull();

    /**
     * ���ɴ���֮ǰ��֤ģ���Ƿ��������й���
     */
    boolean validate();

    List<Validator> getValidators();

    /**
     * ��ȸ���ģ�ͣ�����������ģ��
     * 
     * @return ����ģ�͵���ȸ���
     */
    Model copy();
}
