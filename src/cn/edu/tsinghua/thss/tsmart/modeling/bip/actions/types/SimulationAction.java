package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;

public class SimulationAction extends OpenDialogAction {

    public static final String ID = SimulationAction.class.getCanonicalName();

    public SimulationAction(IWorkbenchWindow window) {
        super(window, ID, "ִ��ģ�ͷ���", "ִ��ģ�ͷ���", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO ִ��ģ�ͷ���
    }
}
