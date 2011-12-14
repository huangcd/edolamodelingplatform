/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

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

        // action = getActionRegistry().getAction(CreateDataCommand.CREATE_DATA_COMMAND);
        // menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
        //
        // action = getActionRegistry().getAction(CreatePriorityCommand.CREATE_PRIORITY_COMMAND);
        // menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
        // action = getActionRegistry().getAction(CreatePortCommand.CREATE_PORT_COMMAND);
        // menu.appendToGroup(GEFActionConstants.GROUP_ADD, action);
    }
}
