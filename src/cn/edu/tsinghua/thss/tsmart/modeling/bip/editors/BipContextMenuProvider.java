/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.SaveComponentTypeAction;

public class BipContextMenuProvider extends ContextMenuProvider {
    private ActionRegistry actionRegistry;

    public ActionRegistry getActionRegistry() {
        return actionRegistry;
    }

    public void setActionRegistry(ActionRegistry actionRegistry) {
        this.actionRegistry = actionRegistry;
    }

    /**
     * @param viewer
     */
    public BipContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer);
        setActionRegistry(registry);
    }

    @Override
    public void buildContextMenu(IMenuManager menu) {
        IAction action;
        GEFActionConstants.addStandardActionGroups(menu);
        action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

        action = getActionRegistry().getAction(ActionFactory.REDO.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

        action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
        menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

        //action = getActionRegistry().getAction(ActionFactory.COPY.getId());
        //menu.appendToGroup(GEFActionConstants.GROUP_COPY, action);

        action = getActionRegistry().getAction(SaveComponentTypeAction.id);
        menu.appendToGroup(GEFActionConstants.GROUP_SAVE, action);
    }
}
