package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public interface Validator {
    /**
     * ��֤һ��ģ�ͣ�������ģ�ͣ��Ƿ��������
     * 
     * @param model
     * @param rule
     * @return
     * @throws BIPModelingException ����֤���������׳��쳣
     */
    boolean validate(IModel model, Rule rule) throws BIPModelingException;

    /**
     * ����ʱ��֤һ��ģ�ͣ�������ģ�ͣ��Ƿ����������������֤ģʽ�£���һЩ��֤������Բ�����
     * 
     * @param model
     * @param rule
     * @return
     * @throws BIPModelingException ����֤���������׳��쳣
     */
    boolean validateOnTheFly(IModel model, Rule rule) throws BIPModelingException;
}
