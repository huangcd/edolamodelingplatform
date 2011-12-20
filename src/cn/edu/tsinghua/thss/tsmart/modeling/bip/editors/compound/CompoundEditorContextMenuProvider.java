package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.PasteComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BipContextMenuProvider;

public class CompoundEditorContextMenuProvider extends BipContextMenuProvider {
    public CompoundEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }

    @Override
    public void buildContextMenu(IMenuManager menu) {
        super.buildContextMenu(menu);

        IAction action = getActionRegistry().getAction(PasteComponentAction.id);
        menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);
    }
    
}
