package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;

public class SimulationAction extends OpenDialogAction {

    public static final String ID = SimulationAction.class.getCanonicalName();

    public SimulationAction(IWorkbenchWindow window) {
        super(window, ID, "执行模型仿真", "执行模型仿真", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 执行模型仿真
    }
}
