package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard.NewLibraryWizard;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard.CreateWizardData;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class NewLibraryAction extends OpenDialogAction {

    public static final String ID = NewLibraryAction.class.getCanonicalName();
    private CreateWizardData         data;

    public NewLibraryAction(IWorkbenchWindow window) {
        super(window, ID, Messages.NewLibraryAction_0, Messages.NewLibraryAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        data = new CreateWizardData();
        return new WizardDialog(getShell(), new NewLibraryWizard(data));
    }

    @Override
    protected void handleFinish(int returnCode) {
        GlobalProperties properties = GlobalProperties.getInstance();
        if (returnCode == Dialog.OK) {
            TopLevelModel<LibraryModel> library =
                            new LibraryModel(data.getName(), data.getBaseline(), data.getLocation());
            properties.setTopModel(library);
            BIPEditor.closeAllEditor();
            // TODO 切换到库模式（初始化库视图）
        }
    }
}
