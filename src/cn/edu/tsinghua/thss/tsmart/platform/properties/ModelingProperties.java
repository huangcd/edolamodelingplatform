package cn.edu.tsinghua.thss.tsmart.platform.properties;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;

@SuppressWarnings("rawtypes")
public interface ModelingProperties {

    public static int NEWPROJECT  = 1;
    public static int OPENPROJECT = 2;
    public static int NEWLIBRARY  = 4;
    public static int OPENLIBRARY = 8;

    TopLevelModel getTopModel();

    /**
     * �����µĶ���ģ�ͣ������ϵĶ���ģ��
     * 
     * @param model
     * @return
     */
    TopLevelModel setTopModel(TopLevelModel model);

    /**
     * ��ģ��ʱ���Ƿ����������������
     * 
     * @return ���ֻ����bool���򷵻�false�����򷵻�true
     */
    public boolean isMultipleDataTypeAvailble();

    /**
     * Atomic�ڲ��Ƿ��������ȼ�
     * 
     * @return
     */
    public boolean isAtomicPriorityAllow();

    /**
     * �Ƿ��������ȼ�
     * 
     * @return
     */
    public boolean isPriorityAllow();

    /**
     * �Ƿ�����㲥���͵�Connector
     * 
     * @return
     */
    public boolean isBroadcastAllow();
}
