package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.baseline.BaselineDataAccessor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.BaselineShowDialog;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public class ViewBaselineAction extends OpenDialogAction {

    public static final String ID = ViewBaselineAction.class.getCanonicalName();

    public ViewBaselineAction(IWorkbenchWindow window) {
        super(window, ID, Messages.ViewBaselineAction_0, Messages.ViewBaselineAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        BaselineDataAccessor bda = new BaselineDataAccessor();
        return new BaselineShowDialog(getShell(),  bda.getBaselinePath(GlobalProperties.getInstance().getTopModel().getBaseline()));
    }
}
