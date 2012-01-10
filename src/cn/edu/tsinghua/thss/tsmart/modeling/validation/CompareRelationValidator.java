package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import org.eclipse.core.runtime.CoreException;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CompareRelationValidator implements Validator {
    private final static String                   FORMAT_STRING =
                                                                                "组件 %s 中的变量  %s [标注为 %s ] 必须  %s 变量 %s [标注为  %s]";

    private final static CompareRelationValidator instance      = new CompareRelationValidator();

    public final static CompareRelationValidator getInstance() {
        return instance;
    }

    private CompareRelationValidator() {}

    @Override
    public boolean validate(IModel model, Rule rule) throws EdolaModelingException {
        // 如果不是比较关系，这里不做验证
        if (!Relation.EQUAL.equals(rule.getRelation())
                        && !Relation.GREAT_EQUAL.equals(rule.getRelation())
                        && !Relation.GREAT_THAN.equals(rule.getRelation())
                        && !Relation.LESS_EQUAL.equals(rule.getRelation())
                        && !Relation.LESS_THAN.equals(rule.getRelation())) {
            return true;
        }
        if (model instanceof DataModel) {
            IContainer<IContainer, IInstance, IContainer, IModel> father = model.getParent();
            if (model.getEntityNames() != null
                            && model.getEntityNames().contains(rule.getFirstEntity())) {
                for (IModel data : father.getChildren()) {
                    if (data instanceof DataModel && !model.equals(data)) {
                        if (data.getEntityNames() != null
                                        && data.getEntityNames().contains(rule.getSecondEntity())) {

                            int a, b;
                            a = Integer.parseInt(((DataModel) model).getValue());
                            b = Integer.parseInt(((DataModel) data).getValue());

                            if (rule.getRelation().equals(Relation.EQUAL) && a != b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getFirstEntity(), "等于", data
                                                                .getName(), rule.getSecondEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }

                                return false;
                            }

                            if (rule.getRelation().equals(Relation.GREAT_EQUAL) && a < b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getFirstEntity(), "大于或等于", data
                                                                .getName(), rule.getSecondEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }

                                return false;
                            }

                            if (rule.getRelation().equals(Relation.GREAT_THAN) && a <= b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getFirstEntity(), "大于", data
                                                                .getName(), rule.getSecondEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.LESS_EQUAL) && a > b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getFirstEntity(), "小于或等于", data
                                                                .getName(), rule.getSecondEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.LESS_THAN) && a >= b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getFirstEntity(), "小于", data
                                                                .getName(), rule.getSecondEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                        }

                    }
                }
            }

            if (model.getEntityNames() != null
                            && model.getEntityNames().contains(rule.getSecondEntity())) {
                for (IModel data : father.getChildren()) {
                    if (data instanceof DataModel && !model.equals(data)) {

                        if (data.getEntityNames() != null
                                        && data.getEntityNames().contains(rule.getFirstEntity())) {

                            int a, b;
                            b = Integer.parseInt(((DataModel) model).getValue());
                            a = Integer.parseInt(((DataModel) data).getValue());

                            if (rule.getRelation().equals(Relation.EQUAL) && a != b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getSecondEntity(), "等于", data
                                                                .getName(), rule.getFirstEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.GREAT_EQUAL) && a < b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getSecondEntity(), "小于或等于", data
                                                                .getName(), rule.getFirstEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.GREAT_THAN) && a <= b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getSecondEntity(), "小于", data
                                                                .getName(), rule.getFirstEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.LESS_EQUAL) && a > b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getSecondEntity(), "大于或等于", data
                                                                .getName(), rule.getFirstEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }

                            if (rule.getRelation().equals(Relation.LESS_THAN) && a >= b) {
                                String errMessage =
                                                String.format(FORMAT_STRING, model.getParent()
                                                                .getName(), model.getName(), rule
                                                                .getSecondEntity(), "大于", data
                                                                .getName(), rule.getFirstEntity());
                                try {
                                    MessageUtil.addProblemWarningMessage(errMessage);
                                } catch (CoreException e) {
                                    e.printStackTrace();
                                }
                                return false;
                            }
                        }

                    }

                }
            }

        }

        return true;
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws EdolaModelingException {
        if (rule.getNeedCheckOnline()) {
            return validate(model, rule);
        } else
            return true;
    }
}
