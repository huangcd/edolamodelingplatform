package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public interface Validator {
    /**
     * ��֤һ��ģ�ͣ�������ģ�ͣ��Ƿ��������
     * 
     * @param model ������ģ��
     * @param rule ��Ҫ��֤�Ĺ���
     * @throws EdolaModelingException ����֤������ʱ�׳��쳣
     */
    boolean validate(IModel model, Rule rule) throws EdolaModelingException;

    /**
     * ����ʱ��֤һ��ģ�ͣ�������ģ�ͣ��Ƿ����������������֤ģʽ�£���һЩ��֤������Բ�����
     * 
     * @param model ������ģ��
     * @param rule ��Ҫ��֤�Ĺ���
     * @throws EdolaModelingException ����֤������ʱ�׳��쳣
     */
    boolean validateOnTheFly(IModel model, Rule rule) throws EdolaModelingException;
}
