package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class SaveTopLevelModelAction extends OpenDialogAction {
    public final static String id = SaveTopLevelModelAction.class.getCanonicalName();

    public SaveTopLevelModelAction(IWorkbenchWindow window) {
        super(window, id, "������Ŀ/������", "���浱ǰ�༭����Ŀ�򹹼���", null);
    }

    @Override
    public void run() {
        try {
            GlobalProperties.getInstance().getTopModel().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }
}
