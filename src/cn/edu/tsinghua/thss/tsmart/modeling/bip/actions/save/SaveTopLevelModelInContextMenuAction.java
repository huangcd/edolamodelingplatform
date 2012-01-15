package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import java.io.IOException;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class SaveTopLevelModelInContextMenuAction extends SelectionAction {
    public final static String id = SaveTopLevelModelInContextMenuAction.class.getCanonicalName();

    public SaveTopLevelModelInContextMenuAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
        setText(Messages.SaveTopLevelModelInContextMenuAction_0);
        setToolTipText(Messages.SaveTopLevelModelInContextMenuAction_1);
    }

    @Override
    protected void init() {
        super.init();
        setId(id);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        try {
            GlobalProperties.getInstance().getTopModel().save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
