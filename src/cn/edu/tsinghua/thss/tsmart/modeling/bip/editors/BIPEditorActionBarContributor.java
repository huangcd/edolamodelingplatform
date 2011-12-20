package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportRetargetAction;

public class BIPEditorActionBarContributor extends ActionBarContributor {

    @Override
    protected void buildActions() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        addRetargetAction(new UndoRetargetAction());
        addRetargetAction(new RedoRetargetAction());
        addRetargetAction(new DeleteRetargetAction());
        addRetargetAction(new ZoomInRetargetAction());
        addRetargetAction(new ZoomOutRetargetAction());
        addRetargetAction((RetargetAction) ActionFactory.COPY.create(window));
        addRetargetAction((RetargetAction) ActionFactory.PASTE.create(window));

        addRetargetAction(new LabelRetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY,
                        "Toggle Grid", IAction.AS_CHECK_BOX));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
        addRetargetAction(new MatchWidthRetargetAction());
        addRetargetAction(new MatchHeightRetargetAction());
        addRetargetAction(new ExportRetargetAction());
    }

    @Override
    protected void declareGlobalActionKeys() {}

    // 工具栏
    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
        toolBarManager.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));

        toolBarManager.add(new Separator());
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
        toolBarManager.add(getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
        toolBarManager.add(new ZoomComboContributionItem(getPage()));
        toolBarManager.add(new Separator());

        toolBarManager.add(getAction(GEFActionConstants.ALIGN_LEFT));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_CENTER));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_TOP));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
        /*
         * toolBarManager.add(getAction(GEFActionConstants.MATCH_WIDTH));
         * toolBarManager.add(getAction(GEFActionConstants.MATCH_HEIGHT));
         */
        // toolBarManager.add(getAction(ActionFactory.COPY.getId()));
        // toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(ExportAction.ID));
    }

    // 菜单栏
    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        MenuManager editMenu = new MenuManager("编辑", "edit");
        menuManager.insertAfter(IWorkbenchActionConstants.M_FILE, editMenu);
        editMenu.add(getActionRegistry().getAction(ActionFactory.UNDO.getId()));
        editMenu.add(getActionRegistry().getAction(ActionFactory.REDO.getId()));
        editMenu.add(new Separator());
        editMenu.add(getActionRegistry().getAction(ActionFactory.DELETE.getId()));
        editMenu.add(new Separator());
        editMenu.add(getAction(GEFActionConstants.ALIGN_LEFT));
        editMenu.add(getAction(GEFActionConstants.ALIGN_CENTER));
        editMenu.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        editMenu.add(getAction(GEFActionConstants.ALIGN_TOP));
        editMenu.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        editMenu.add(getAction(GEFActionConstants.ALIGN_BOTTOM));

        IMenuManager viewMenu = new MenuManager("查看", "view");
        menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
        viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
        viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
        viewMenu.add(new Separator());
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
    }
}
