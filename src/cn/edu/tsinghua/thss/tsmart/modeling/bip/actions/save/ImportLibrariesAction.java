package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.ImportLibrariesDialog;

@SuppressWarnings("rawtypes")

/**
 * 从zip文件导入构件库
 * 
 * @author Huangcd
 */
public class ImportLibrariesAction extends OpenDialogAction {
    public final static String id = ImportLibrariesAction.class.getCanonicalName();

    public ImportLibrariesAction(IWorkbenchWindow window) {
        super(window, id, Messages.ImportLibrariesAction_0, Messages.ImportLibrariesAction_1, null);
    }

    @Override
    protected Dialog getDialog() {
        return new ImportLibrariesDialog(getShell());
    }
}
