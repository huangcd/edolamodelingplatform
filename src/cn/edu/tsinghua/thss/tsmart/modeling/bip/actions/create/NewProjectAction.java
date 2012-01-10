package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard.CreateWizardData;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard.NewProjectWizard;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class NewProjectAction extends OpenDialogAction {

    public static final String ID = NewProjectAction.class.getCanonicalName();
    private CreateWizardData   data;

    public NewProjectAction(IWorkbenchWindow window) {
        super(window, ID, "项目", "新建一个Edola项目", null);
    }

    @Override
    protected Dialog getDialog() {
        data = new CreateWizardData();
        return new WizardDialog(getShell(), new NewProjectWizard(data));
    }

    @Override
    protected void handleFinish(int returnCode) {
        GlobalProperties properties = GlobalProperties.getInstance();
        if (returnCode == Dialog.OK) {
            BIPEditor.closeAllEditor();
            CodeGenProjectModel project =
                            new CodeGenProjectModel(data.getName(), data.getBaseline(),
                                            data.getLocation());
            properties.setTopModel(project);
            project.initLibraries(data.getLibraries());
            project.openStartupModel();
            // TODO 切换到项目模式（初始化项目视图）
        }
    }
}
