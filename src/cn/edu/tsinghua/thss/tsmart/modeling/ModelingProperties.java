package cn.edu.tsinghua.thss.tsmart.modeling;

public interface ModelingProperties {

    /**
     * ��ģ��ʱ���Ƿ�����������������
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
     * �Ƿ������㲥���͵�Connector
     * 
     * @return
     */
    public boolean isBroadcastAllow();
}