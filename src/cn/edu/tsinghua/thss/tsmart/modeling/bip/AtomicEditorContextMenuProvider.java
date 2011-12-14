/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.actions.ActionFactory;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.CreateDataTypeAction;

public class AtomicEditorContextMenuProvider extends BipContextMenuProvider {
    public AtomicEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
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

        action = getActionRegistry().getAction(CreateDataTypeAction.id);
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
