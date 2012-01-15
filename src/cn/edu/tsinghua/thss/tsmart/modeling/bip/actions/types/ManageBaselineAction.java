package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import java.io.File;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;

public class ManageBaselineAction extends OpenDialogAction {
    public static final String ID = ManageBaselineAction.class.getCanonicalName();

    public ManageBaselineAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ManageBaselineAction_0, Messages.ManageBaselineAction_1, null); //$NON-NLS-3$
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        Location configArea = Platform.getInstallLocation();
        final String path = configArea.getURL().toExternalForm();
        // jump file:/
        final String cmd = "java -jar \"" + path.substring(6) + "/BaselineTool.jar\""; //$NON-NLS-1$ //$NON-NLS-2$
        new Thread() {
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec(cmd, null, new File(path.substring(6)));
                    process.waitFor();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        
        
    }
}
