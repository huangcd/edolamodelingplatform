package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class ValidateCodeGenerationAction extends OpenDialogAction {

    public static final String ID = ValidateCodeGenerationAction.class.getCanonicalName();

    public ValidateCodeGenerationAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ValidateCodeGenerationAction_0, Messages.ValidateCodeGenerationAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 验证模型是否满足要求
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.ValidateCodeGenerationAction_2);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        CodeGenProjectModel cgpm = (CodeGenProjectModel) topModel;
        CompoundTypeModel model = cgpm.getStartupModel();
        // 如果不通过验证，不能导出模型

        try {
            model.validateFull();
            model.checkCodeGenValid();
        } catch (EdolaModelingException e) {
            MessageUtil.ShowErrorDialog(e.getMessage(), Messages.ValidateCodeGenerationAction_3);
        }

    }

}
