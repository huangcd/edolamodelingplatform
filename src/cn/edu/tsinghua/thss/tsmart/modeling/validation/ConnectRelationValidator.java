package cn.edu.tsinghua.thss.tsmart.modeling.validation;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.VerificationError;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConnectRelationValidator implements Validator {
    private final static String                   FORMAT_STRING1 =
                                                                                 "组件 %s [标注为 %s] 最多只能连接上 %d 个标注为 %s 的组件：该组件已经有 %d 个连接。";
    private final static String                   FORMAT_STRING2 =
                                                                                 "组件 %s [标注为 %s] 最少需要连接 %d 个标注为 %s 的组件：该组件已经有 %d 个连接。";

    private final static ConnectRelationValidator instance       = new ConnectRelationValidator();

    public final static ConnectRelationValidator getInstance() {
        return instance;
    }

    private ConnectRelationValidator() {}

    public boolean validateConnect(IModel model, Rule rule, boolean testFather)
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

        CompoundTypeModel father = null;
        boolean result = true;
        boolean consider_tick = true;

        if (!(model instanceof ConnectorModel || model instanceof ComponentModel || model instanceof CompoundTypeModel))
            return true;

        if (testFather && (model instanceof ConnectorModel || model instanceof ComponentModel)) {

            father = model.getParent() != null ? (CompoundTypeModel) model.getParent() : null;
        }

        if (!testFather && (model instanceof CompoundTypeModel)) {
            father = (CompoundTypeModel) model;
        }

        if (father == null) return true;

        for (IInstance child1 : father.getChildren()) {
            if (!(child1 instanceof CompoundModel || child1 instanceof AtomicModel)) continue;
            if (child1.getEntityNames().contains(rule.getFirstEntity())) {// 第一个实体
                int count = 0;

                for (IInstance child2 : father.getChildren()) {
                    if (!(child2 instanceof CompoundModel || child2 instanceof AtomicModel))
                        continue;

                    if (child2.getEntityNames().contains(rule.getSecondEntity())) { // 第二个实体
                        if (child1.equals(child2)) continue;

                        boolean connected = false;

                        // 采用连接子计算连接关系
                        for (IInstance con : father.getChildren()) {
                            if (!(con instanceof ConnectorModel)) continue;

                            PortTypeModel ptm = null;
                            IInstance component1 = null;
                            IInstance component2 = null;


                            for (int i = 0; i < ((ConnectorModel) con).getType().getArgumentSize(); i++) {
                                if (connected) break;

                                ptm = ((ConnectorModel) con).getType().getPortArgument(i);
                                if (consider_tick
                                                && (ptm == null || ptm.getEntityNames().contains(
                                                                projectModel.getTickEntity())))
                                    continue;


                                component1 =
                                                ptm != null ? ptm.getInstance().getParent()
                                                                .getInstance() : null;

                                for (int j = 0; j < ((ConnectorModel) con).getType()
                                                .getArgumentSize(); j++) {

                                    ptm = ((ConnectorModel) con).getType().getPortArgument(j);
                                    if (consider_tick
                                                    && (ptm == null || ptm
                                                                    .getEntityNames()
                                                                    .contains(projectModel
                                                                                    .getTickEntity())))
                                        continue;

                                    component2 =
                                                    ptm != null ? ptm.getInstance().getParent()
                                                                    .getInstance() : null;

                                    if (component1 != null && component2 != null
                                                    && component1.equals(child1)
                                                    && component2.equals(child2)) {
                                        connected = true;
                                        break;
                                    }
                                }
                            }
                            if (connected) {
                                count++;
                                break;
                            }
                        }

                    }
                }

                if (count < rule.getMin()) {
                    String errMessage =
                                    String.format(FORMAT_STRING2, child1.getName(),
                                                    rule.getFirstEntity(), rule.getMin(),
                                                    rule.getSecondEntity(), count);
                    MessageUtil.addProblemWarningMessage(errMessage);
                    result = false;
                }

                if (count > rule.getMax()) {
                    String errMessage =
                                    String.format(FORMAT_STRING1, child1.getName(),
                                                    rule.getFirstEntity(), rule.getMax(),
                                                    rule.getSecondEntity(), count);
                    MessageUtil.addProblemWarningMessage(errMessage);
                    result = false;
                }
            }
        }
        return result;
    }

    @Override
    public boolean validate(IModel model, Rule rule) throws EdolaModelingException {
        // 如果不是连接关系，这里不做验证
        return validateConnect(model, rule, false);
    }

    @Override
    public boolean validateOnTheFly(IModel model, Rule rule) throws EdolaModelingException {
        if (rule.getNeedCheckOnline()) {
            return validateConnect(model, rule, true);
        } else
            return true;
    }
}
