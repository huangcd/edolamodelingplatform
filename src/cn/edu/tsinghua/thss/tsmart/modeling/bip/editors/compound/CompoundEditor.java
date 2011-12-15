package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BipContextMenuProvider;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;

@SuppressWarnings("rawtypes")
public class CompoundEditor extends BIPEditor {
    public final static String id = CompoundEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private IModel             model;

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        setEditorTitle(getModel().getName());
        viewer = getGraphicalViewer();
        putViewerEditorEntry(viewer, this);
        viewer.setContents(getModel());
        viewer.setContextMenu(new BipContextMenuProvider(viewer, getActionRegistry()));
        createCompoundPaletteDrawer();
    }

    private void createCompoundPaletteDrawer() {
        PaletteDrawer compoundPalette = new PaletteDrawer("复合组件");
        CreationToolEntry creationAtomicEntry =
                        new CreationToolEntry("原子组件", "新建一个原子组件", new SimpleFactory(
                                        AtomicTypeModel.class), getImage("icons/atomic_16.png"),
                                        getImage("icons/atomic_32.png"));
        CreationToolEntry creationCompoundEntry =
                        new CreationToolEntry("复合组件", "新建一个复合组件", new SimpleFactory(
                                        CompoundTypeModel.class),
                                        getImage("icons/compound_16.png"),
                                        getImage("icons/compound_32.png"));
        CreationToolEntry creationConnectorEntry =
                        new CreationToolEntry("连接子", "新建一个连接子", new SimpleFactory(
                                        ConnectorTypeModel.class),
                                        getImage("icons/connector_16.png"),
                                        getImage("icons/connector_32.png"));

        // ConnectionCreationToolEntry creationConnectorLineEntry =
        // new ConnectionCreationToolEntry("Connector line",
        // "create a new connector line", new SimpleFactory(
        // ConnectorPortModel.class), getImage("icons/connectorline_16.png"),
        // getImage("icons/connectorline_32.png"));
        compoundPalette.add(creationAtomicEntry);
        compoundPalette.add(creationCompoundEntry);
        compoundPalette.add(creationConnectorEntry);
        // drawer.add(creationConnectorLineEntry);
        getPaletteRoot().add(compoundPalette);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        saveProperties();
        new ExportAction(this).run();
    }

    public void doSaveAs() {}

    public boolean isSaveAsAllowed() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void createActions() {
        super.createActions();
        ActionRegistry registry = getActionRegistry();

        // IAction action = new CreateDataAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());
        //
        // action = new CreatePriorityAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());

        IAction action = new ExportAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        //
        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.LEFT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.CENTER);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.RIGHT);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.TOP);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.MIDDLE);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new AlignmentAction((IWorkbenchPart) this, PositionConstants.BOTTOM);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new MatchWidthAction((IWorkbenchPart) this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new MatchHeightAction((IWorkbenchPart) this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        // action = new CreatePortAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());

    }

    public void dispose() {
        removeViewerEditEntry(viewer);
        System.out.println("remove viewer from viewerEditorMap");
        super.dispose();
    }

    /** Save properties to model */
    protected void saveProperties() {}

    /** Load properties */
    protected void loadProperties() {}

    // 创建 ContentOutlinePage 类
    class BIPContentOutlinePage extends ContentOutlinePage {
        public BIPContentOutlinePage() {
            // 使用 GEF 的 TreeViewer
            super(new TreeViewer());
        }

        public BIPContentOutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        public void init(IPageSite pageSite) {
            super.init(pageSite);
            // 获得注册给 graphical editor 的 Action
            ActionRegistry registry = getActionRegistry();
            // 使这些 Action 在大纲视图中也有效
            IActionBars bars = pageSite.getActionBars();

            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            bars.updateActionBars();
        }

        // 重载
        @Override
        public void createControl(Composite parent) {
            getViewer().createControl(parent);
            // 设置 Edit Domain
            getViewer().setEditDomain(getEditDomain());
            // 设置 EditPartFactory
            // getViewer().setEditPartFactory(new TreeEditPartFactory());
            getViewer().setEditPartFactory(TreeEditPartFactory.getInstance());
            // 本视图中对应于 BIPModel的内容
            if (model != null) {
                getViewer().setContents(model);
            }
            // 选择同步：在 Graphical editor 中选择图形，则大纲视图选择对应的节点；反之亦然
            getSelectionSynchronizer().addViewer(getViewer());

        }

        @Override
        public Control getControl() {
            // 当大纲视图是当前（active）视图时，返回聚焦的控件
            // return sash;
            return getViewer().getControl();
        }

        public void dispose() {
            // 从 TreeViewer 中删除 SelectionSynchronizer
            getSelectionSynchronizer().removeViewer(getViewer());
            super.dispose();
        }
    }
}
