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
     * 设置新的顶级模型，返回老的顶级模型
     * 
     * @param model
     * @return
     */
    TopLevelModel setTopModel(TopLevelModel model);

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
