package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import org.eclipse.core.runtime.CoreException;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConnectRelationValidator implements Validator {
    private final static String                   FORMAT_STRING1 =
                                                                                 "组件 %s [标注为 %s] 最多只能连接上 %d 个标注为 %s 的组件。";
    private final static String                   FORMAT_STRING2 =
                                                                                 "组件 %s [标注为 %s] 最少需要连接 %d 个标注为 %s 的组件。]";

    private final static ConnectRelationValidator instance       = new ConnectRelationValidator();

    public final static ConnectRelationValidator getInstance() {
        return instance;
    }

    private ConnectRelationValidator() {}

    public boolean validateConnect(IModel model, Rule rule, boolean onlyConnector)
                    throws VerificationError {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return true;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;
        if (!Relation.CONNECT.equals(rule.getRelation())) {
            return true;
        }
        if (model instanceof ConnectorModel) {
            for (int i = 0; i < ((ConnectorModel) model).getType().getArgumentSize(); i++) {

                PortTypeModel ptm = ((ConnectorModel) model).getType().getPortArgument(i);
                IContainer component1 = ptm != null ? ptm.getInstance().getParent() : null;

                // tick 和 io port 不考虑连接约束
                if (ptm.getEntityNames().contains(projectModel.getIoEntity())
                                || ptm.getEntityNames().contains(projectModel.getTickEntity())) {
                    continue;
                }

                if (component1 != null && component1.getEntityNames() != null
                                && component1.getEntityNames().contains(rule.getFirstEntity())) {
                    int count = 0;

                    for (int j = 0; j < ((ConnectorModel) model).getType().getArgumentSize(); j++) {

                        ptm = ((ConnectorModel) model).getType().getPortArgument(j);

                        // tick 和 io port 不考虑连接约束
                        if (ptm.getEntityNames().contains(projectModel.getIoEntity())
                                        || ptm.getEntityNames().contains(
                                                        projectModel.getTickEntity())) {
                            continue;
                        }

                        IContainer component2 = ptm != null ? ptm.getInstance().getParent() : null;

                        if (component2 != null
                                        && !component1.equals(component2)
                                        && component2.getEntityNames() != null
                                        && component2.getEntityNames().contains(
                                                        rule.getSecondEntity())) {
                            count++;
                        }

                    }
                    if (count < rule.getMin()) {
                        String errMessage =
                                        String.format(FORMAT_STRING2, component1.getName(),
                                                        rule.getFirstEntity(), rule.getMin(),
                                                        rule.getSecondEntity());
                        try {
                            MessageUtil.addProblemWarningMessage(errMessage);
                        } catch (CoreException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }
                    if (count > rule.getMax()) {
                        String errMessage =
                                        String.format(FORMAT_STRING1, component1.getName(),
                                                        rule.getFirstEntity(), rule.getMax(),
                                                        rule.getSecondEntity());
                        try {
                            MessageUtil.addProblemWarningMessage(errMessage);
                        } catch (CoreException e) {
                            e.printStackTrace();
                        }

                        return false;
                    }

                    // if (!errString.toString().equals(""))
                    // throw new VerificationError(errString.toString());
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
                                if (!validate(child, rule)) return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean validate(IModel model, Rule rule) throws EdolaModelingException {
        // 如果不是连接关系，这里不做验证
        return validateConnect(model, rule, true);
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws EdolaModelingException {
        if (rule.getNeedCheckOnline()) {
            return validateConnect(model, rule, false);
        } else
            return true;
    }
}
