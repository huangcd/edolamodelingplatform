package cn.edu.tsinghua.thss.tsmart.platform;

public interface Properties {

    /**
     * 建模的时候是否允许多种数据类型
     * 
     * @return 如果只允许bool，则返回false；否则返回true
     */
    public boolean isMultipleDataTypeAvailble();

    /**
     * Atomic内部是否允许优先级
     * 
     * @return
     */
    public boolean isAtomicPriorityAllow();

    /**
     * 是否允许优先级
     * 
     * @return
     */
    public boolean isPriorityAllow();

    /**
     * 是否允许广播类型的Connector
     * 
     * @return
     */
    public boolean isBroadcastAllow();
}
