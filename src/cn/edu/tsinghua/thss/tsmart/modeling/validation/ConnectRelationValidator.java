package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.*;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.*;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConnectRelationValidator implements Validator {
    private final static String                   FORMAT_STRING1 =
                                                                                 "Component %s [of entity %s] should connect at most %d components [of entity %s]";
    private final static String                   FORMAT_STRING2 =
                                                                                 "Component %s [of entity %s] should connect at least %d compnents [of entity %s]";

    private final static ConnectRelationValidator instance       = new ConnectRelationValidator();

    public final static ConnectRelationValidator getInstance() {
        return instance;
    }

    private ConnectRelationValidator() {}

    public boolean validateConnect(IModel model, Rule rule, boolean onlyConnector)
                    throws VerificationError {
        if (!Relation.CONNECT.equals(rule.getRelation())) {
            return true;
        }

        if (model instanceof ConnectorModel) {

            for (int i = 0; i < ((ConnectorModel) model).getType().getArgumentSize(); i++) {

                PortTypeModel ptm = ((ConnectorModel) model).getType().getPortArgument(i);
                IContainer component1 = ptm != null ? ptm.getInstance().getParent() : null;

                if (component1 != null && component1.getEntityNames() != null
                                && component1.getEntityNames().contains(rule.getFirstEntity())) {

                    int count = 0;
                    StringBuilder errString = new StringBuilder("");

                    for (int j = 0; j < ((ConnectorModel) model).getType().getArgumentSize(); j++) {

                        ptm = ((ConnectorModel) model).getType().getPortArgument(j);
                        IContainer component2 = ptm != null ? ptm.getInstance().getParent() : null;

                        if (component2 != null
                                        && !component1.equals(component2)
                                        && component2.getEntityNames() != null
                                        && component2.getEntityNames().contains(rule.getSecondEntity())) {
                            count++;
                        }

                    }
                    if (count < rule.getMin()) {
                        errString.append(
                                        String.format(FORMAT_STRING2, component1.getName(),
                                                        rule.getFirstEntity(), rule.getMin(),
                                                        rule.getSecondEntity())).append('\n');
                    }
                    if (count > rule.getMax()) {
                        errString.append(
                                        String.format(FORMAT_STRING1, component1.getName(),
                                                        rule.getFirstEntity(), rule.getMax(),
                                                        rule.getSecondEntity())).append('\n');
                    }

                    if (!errString.toString().equals(""))
                        throw new VerificationError(errString.toString());
                }

            }

        }

        if (!onlyConnector
                        && (model instanceof ComponentModel || model instanceof ComponentTypeModel)) {
            CompoundTypeModel father = null;

            if (model instanceof ComponentModel) father = (CompoundTypeModel) model.getParent();
            if (model instanceof ComponentTypeModel)
                father = (CompoundTypeModel) ((ComponentTypeModel) model).getInstance().getParent();

            if (father != null) {
                for (IInstance child : father.getChildren()) {
                    if (child instanceof ConnectorModel) {
                        for (ConnectionModel con : ((ConnectorModel) child).getSourceConnections()) {
                            ComponentModel com =
                                            con.getTarget().getPort() != null
                                                            ? (ComponentModel) ((ComponentTypeModel) con
                                                                            .getTarget().getPort()
                                                                            .getParent())
                                                                            .getInstance() : null;
                            if ((model instanceof ComponentModel && com != null && com
                                            .equals(model))
                                            || (model instanceof ComponentTypeModel && com != null && com
                                                            .equals(((ComponentTypeModel) model)
                                                                            .getInstance()))) {
                                validate(child, rule);
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean validate(IModel model, Rule rule) throws BIPModelingException {
        // 如果不是连接关系，这里不做验证
        return validateConnect(model, rule, true);
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws BIPModelingException {
        if (rule.getNeedCheckOnline())
            return validateConnect(model, rule, false);
        else
            return false;
    }
}
