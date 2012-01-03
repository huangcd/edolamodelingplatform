package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataModel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class CompareRelationValidator implements Validator {
    private final static String                   FORMAT_STRING =
                                                                                "Variable %s [of entity %s] should be %s variable %s [of entity %s]";

    private final static CompareRelationValidator instance      = new CompareRelationValidator();

    public final static CompareRelationValidator getInstance() {
        return instance;
    }

    private CompareRelationValidator() {}

    @Override
    public boolean validate(IModel model, Rule rule) throws BIPModelingException {
        // 如果不是比较关系，这里不做验证
        if (!Relation.EQUAL.equals(rule.getRelation())
                        && !Relation.GREAT_EQUAL.equals(rule.getRelation())
                        && !Relation.GREAT_THAN.equals(rule.getRelation())
                        && !Relation.LESS_EQUAL.equals(rule.getRelation())
                        && !Relation.LESS_THAN.equals(rule.getRelation())) {
            return true;
        }

        if (model instanceof DataModel) {
            IContainer<IContainer, IContainer, IModel> father = model.getParent();
            StringBuilder errString = new StringBuilder("");

            if (model.getEntityNames() != null
                            && model.getEntityNames().contains(rule.getFirstEntity())) {
                for (IModel data : father.getChildren()) {
                    if (data instanceof DataModel && !model.equals(data)) {

                        if (data.getEntityNames() != null
                                        && data.getEntityNames().contains(rule.getSecondEntity())) {

                            int a, b;
                            a = Integer.parseInt(((DataModel) model).getValue());
                            b = Integer.parseInt(((DataModel) data).getValue());

                            if (rule.getRelation().equals(Relation.EQUAL) && a != b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getFirstEntity(), "equal to",
                                                                data.getName(),
                                                                rule.getSecondEntity())).append(
                                                '\n');

                            if (rule.getRelation().equals(Relation.GREAT_EQUAL) && a < b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getFirstEntity(),
                                                                "great than or equal to",
                                                                data.getName(),
                                                                rule.getSecondEntity())).append(
                                                '\n');

                            if (rule.getRelation().equals(Relation.GREAT_THAN) && a <= b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getFirstEntity(),
                                                                "great than", data.getName(),
                                                                rule.getSecondEntity())).append(
                                                '\n');

                            if (rule.getRelation().equals(Relation.LESS_EQUAL) && a > b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getFirstEntity(),
                                                                "less than or equal to",
                                                                data.getName(),
                                                                rule.getSecondEntity())).append(
                                                '\n');

                            if (rule.getRelation().equals(Relation.LESS_THAN) && a >= b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getFirstEntity(), "less than",
                                                                data.getName(),
                                                                rule.getSecondEntity())).append(
                                                '\n');

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

                            if (rule.getRelation().equals(Relation.EQUAL) && a != b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getSecondEntity(), "equal to",
                                                                data.getName(),
                                                                rule.getFirstEntity()))
                                                .append('\n');

                            if (rule.getRelation().equals(Relation.GREAT_EQUAL) && a < b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getSecondEntity(),
                                                                "less than or equal to",
                                                                data.getName(),
                                                                rule.getFirstEntity()))
                                                .append('\n');

                            if (rule.getRelation().equals(Relation.GREAT_THAN) && a <= b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getSecondEntity(),
                                                                "less than", data.getName(),
                                                                rule.getFirstEntity()))
                                                .append('\n');

                            if (rule.getRelation().equals(Relation.LESS_EQUAL) && a > b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getSecondEntity(),
                                                                "great than or equal to",
                                                                data.getName(),
                                                                rule.getFirstEntity()))
                                                .append('\n');

                            if (rule.getRelation().equals(Relation.LESS_THAN) && a >= b)
                                errString.append(
                                                String.format(FORMAT_STRING, model.getName(),
                                                                rule.getSecondEntity(),
                                                                "great than", data.getName(),
                                                                rule.getFirstEntity()))
                                                .append('\n');

                        }

                    }

                }
            }

            if (!errString.toString().equals(""))
                throw new VerificationError(errString.toString());
        }

        return true;
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws BIPModelingException {
        if (rule.getNeedCheckOnline())
            return validate(model, rule);
        else
            return false;
    }
}
