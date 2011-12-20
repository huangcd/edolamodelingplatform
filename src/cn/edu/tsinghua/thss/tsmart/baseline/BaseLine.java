package cn.edu.tsinghua.thss.tsmart.baseline;

import java.util.Iterator;

public interface BaseLine {
    /**
     * ͨ���û�ѡ��ķ�ʽ�õ�����ĵ�����
     * 
     * @return
     */
    Iterator<Rule> getRules();

    /**
     * ������׼�߿����ƺͼ���������������Ӧ��ʵ����
     * 
     * @param baseLineLib
     * @param searchCondition
     * @return
     */
    String getEntity(String baseLineLib, String searchCondition);
}
