package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking.ModelCheckingRunningDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingManager;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class ValidateModelCheckingAction extends OpenDialogAction {

    public static final String ID = ValidateModelCheckingAction.class.getCanonicalName();

    public ValidateModelCheckingAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ValidateModelCheckingAction_0,
                        Messages.ValidateModelCheckingAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
        CodeGenProjectModel cgpm = (CodeGenProjectModel) topModel;

        cgpm.getStartupModel().validateFull();

        Shell shell = Display.getCurrent().getActiveShell();
        final ModelCheckingRunningDialog mcrd = new ModelCheckingRunningDialog(shell);
        mcrd.notifyStarted();
        
        new Thread() {
            public void run() {
                ModelCheckingManager mcm = new ModelCheckingManager();
                mcm.doChecking(true, "");//$NON-NLS-1$
                mcrd.notifyFinished();
            }
        }.start();

        if (!mcrd.isFinished()) {
            mcrd.open();
        }

    }
}
