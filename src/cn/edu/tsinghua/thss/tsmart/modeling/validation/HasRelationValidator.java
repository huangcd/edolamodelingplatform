package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HasRelationValidator implements Validator {
    private final static String               FORMAT_STRING =
                                                                            "Component %s (of entity %s) should have [%d, %d] children of entity %s";

    private final static HasRelationValidator instance      = new HasRelationValidator();

    public final static HasRelationValidator getInstance() {
        return instance;
    }

    private HasRelationValidator() {}

    private boolean validateHas(IContainer<IContainer, IContainer, IModel> container, Rule rule,
                    boolean justSelf) throws VerificationError {
        if (container.getEntityNames().contains(rule.getFirstEntity())) {
            int count = 0;
            for (IModel model : container.getChildren()) {
                if (model.getEntityNames().contains(rule.getSecondEntity())) {
                    count++;
                }
                // 递归验证子容器
                // 无需递归检查
                /*
                 * if (model instanceof IContainer) { validateHas((IContainer<IContainer,
                 * IContainer, IModel>) model, firstEntity, secondEntity); } else if (model
                 * instanceof IInstance && ((IInstance) model).getType() instanceof IContainer) {
                 * validateHas((IContainer<IContainer, IContainer, IModel>) ((IInstance) model)
                 * .getType(), firstEntity, secondEntity); }
                 */
            }
            if (count < rule.getMin() || count > rule.getMax()) {
                throw new VerificationError(String.format(FORMAT_STRING, container.getName(),
                                rule.getFirstEntity(), rule.getMin(), rule.getMax(),
                                rule.getSecondEntity()));
            }
        }

        if (!justSelf) {
            IContainer father = ((IType) container).getInstance().getParent();
            if (father != null) validateHas(father, rule, true);
        }

        return true;
    }

    @Override
    public boolean validate(IModel model, Rule rule) throws BIPModelingException {
        // 如果不是has关系，这里不做验证
        if (!Relation.HAS.equals(rule.getRelation())) {
            return true;
        }
        // 如果model不是IContainer类型，不需要验证has关系
        if (model instanceof IContainer) {
            return validateHas((IContainer<IContainer, IContainer, IModel>) model, rule, true);
        } else if (model instanceof IInstance
                        && ((IInstance) model).getType() instanceof IContainer) {
            validateHas((IContainer<IContainer, IContainer, IModel>) ((IInstance) model).getType(),
                            rule, true);
        }
        return true;
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws BIPModelingException {
        if (rule.getNeedCheckOnline()) {
            if (!Relation.HAS.equals(rule.getRelation())) {
                return true;
            }
            // 如果model不是IContainer类型，不需要验证has关系
            if (model instanceof IContainer) {
                return validateHas((IContainer<IContainer, IContainer, IModel>) model, rule, false);
            } else if (model instanceof IInstance
                            && ((IInstance) model).getType() instanceof IContainer) {
                validateHas((IContainer<IContainer, IContainer, IModel>) ((IInstance) model)
                                .getType(),
                                rule, false);
            }

            return true;
        } else
            return true;
    }
}
