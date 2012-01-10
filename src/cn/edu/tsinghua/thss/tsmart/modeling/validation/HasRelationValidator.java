package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import org.eclipse.core.runtime.CoreException;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class HasRelationValidator implements Validator {
    private final static String               FORMAT_STRING =
                                                                            "��� %s (��עΪ %s) ��Ҫ���� [%d, %s] ����עΪ  %s ����Ԫ��";

    private final static HasRelationValidator instance      = new HasRelationValidator();

    public final static HasRelationValidator getInstance() {
        return instance;
    }

    private HasRelationValidator() {}

    private boolean validateHas(IContainer<IContainer, IInstance, IContainer, IModel> container,
                    Rule rule, boolean justSelf) throws VerificationError {
        if (container.getEntityNames().contains(rule.getFirstEntity())) {
            int count = 0;
            for (IModel model : container.getChildren()) {
                if (model.getEntityNames().contains(rule.getSecondEntity())) {
                    count++;
                }
            }
            if (count < rule.getMin() || count > rule.getMax()) {
                String errMessage =
                                String.format(FORMAT_STRING,
                                                container.getName(),
                                                rule.getFirstEntity(),
                                                rule.getMin(),
                                                rule.getMax() == Integer.MAX_VALUE ? "+" : String
                                                                .format("%d", rule.getMax()), rule
                                                                .getSecondEntity());
                try {
                    MessageUtil.addProblemWarningMessage(errMessage);
                } catch (CoreException e) {
                    e.printStackTrace();
                }

                return false;
            }
        }

        if (!justSelf) {
            IContainer father = ((IType) container).getInstance().getParent();
            if (father != null) return validateHas(father, rule, true);
        }

        return true;
    }

    @Override
    public boolean validate(IModel model, Rule rule) throws EdolaModelingException {
        // �������has��ϵ�����ﲻ����֤
        if (!Relation.HAS.equals(rule.getRelation())) {
            return true;
        }
        // ���model����IContainer���ͣ�����Ҫ��֤has��ϵ
        if (model instanceof IContainer) {
            return validateHas((IContainer<IContainer, IInstance, IContainer, IModel>) model, rule,
                            true);
        } else if (model instanceof IInstance
                        && ((IInstance) model).getType() instanceof IContainer) {
            return validateHas(
                            (IContainer<IContainer, IInstance, IContainer, IModel>) ((IInstance) model)
                                            .getType(), rule, true);
        }
        return true;
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws EdolaModelingException {
        if (rule.getNeedCheckOnline()) {
            if (!Relation.HAS.equals(rule.getRelation())) {
                return true;
            }

            // ���model����IContainer���ͣ�����Ҫ��֤has��ϵ
            if (model instanceof IContainer) {
                return validateHas((IContainer<IContainer, IInstance, IContainer, IModel>) model,
                                rule, false);
            } else if (model instanceof IInstance
                            && ((IInstance) model).getType() instanceof IContainer) {
                return validateHas(
                                (IContainer<IContainer, IInstance, IContainer, IModel>) ((IInstance) model)
                                                .getType(), rule, false);
            }
        }

        return true;
    }
}
