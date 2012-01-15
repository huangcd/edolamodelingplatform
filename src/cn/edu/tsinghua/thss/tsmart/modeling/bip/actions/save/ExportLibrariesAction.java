package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ExportLibrariesDialog;

@SuppressWarnings("rawtypes")
/**
 * 导出构件库到zip文件
 * 
 * @author Huangcd
 */
public class ExportLibrariesAction extends OpenDialogAction {
    public final static String id = ExportLibrariesAction.class.getCanonicalName();

    public ExportLibrariesAction(IWorkbenchWindow window) {
        super(window, id, Messages.ExportLibrariesAction_0, Messages.ExportLibrariesAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return new ExportLibrariesDialog(getShell());
    }
}
