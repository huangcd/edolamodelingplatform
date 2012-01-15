package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.modelsimulation.ModelSimulationManager;

public class SimulationAction extends OpenDialogAction {

    public static final String ID = SimulationAction.class.getCanonicalName();

    public SimulationAction(IWorkbenchWindow window) {
        super(window, ID, Messages.SimulationAction_0, Messages.SimulationAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 执行模型仿真
    	//by 张华枫
    	ModelSimulationManager msm = new ModelSimulationManager();
    	msm.doSimulation(true, ""); //$NON-NLS-1$
    }
}
