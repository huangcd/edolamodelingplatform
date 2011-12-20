package cn.edu.tsinghua.thss.tsmart.baseline;

import java.util.Iterator;

public interface BaseLine {
    Iterator<Rule> getRules();

    String getEntity(String baseLineLib, String searchCondition);
}
