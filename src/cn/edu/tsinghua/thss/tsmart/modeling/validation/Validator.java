package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public interface Validator {
    /**
     * 验证一个模型（及其子模型）是否满足规则
     * 
     * @param model
     * @param rule
     * @return
     * @throws BIPModelingException 当验证不满足是抛出异常
     */
    boolean validate(IModel model, Rule rule) throws BIPModelingException;

    /**
     * 运行时验证一个模型（及其子模型）是否满足规则。在这种验证模式下，有一些验证错误可以不报错
     * 
     * @param model
     * @param rule
     * @return
     * @throws BIPModelingException 当验证不满足是抛出异常
     */
    boolean validateOnTheFly(IModel model, Rule rule) throws BIPModelingException;
}
