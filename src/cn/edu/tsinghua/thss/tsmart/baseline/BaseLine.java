package cn.edu.tsinghua.thss.tsmart.baseline;

import java.util.Iterator;

public interface BaseLine {
    /**
     * 通过用户选择的方式得到规则的迭代器
     * 
     * @return
     */
    Iterator<Rule> getRules();

    /**
     * 给定基准线库名称和检索条件，返回相应的实体名
     * 
     * @param baseLineLib
     * @param searchCondition
     * @return
     */
    String getEntity(String baseLineLib, String searchCondition);
}
