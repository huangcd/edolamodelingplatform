package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.PartFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

@SuppressWarnings("rawtypes")
public class BIPEditor extends GraphicalEditorWithFlyoutPalette {

    private static HashMap<EditPartViewer, BIPEditor> viewerMap =
                                                                                new HashMap<EditPartViewer, BIPEditor>();
    private GraphicalViewer                           viewer;
    private IModel                                    model;
    private PaletteRoot                               paletteRoot;

    public static BIPEditor getEditorFromViewer(EditPartViewer viewer) {
        return viewerMap.get(viewer);
    }

    /**
     * 打开一个编辑模型的页面
     * 
     * @param container 待编辑模型
     */
    public static void openBIPEditor(IContainer container) {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        try {
            // 如果页面已经打开，则跳转到指定页面
            for (IEditorReference reference : page.getEditorReferences()) {
                if (reference.getEditorInput() instanceof BIPModuleEditorInput) {
                    BIPModuleEditorInput editorInput =
                                    (BIPModuleEditorInput) reference.getEditorInput();
                    if (editorInput.getModel().equals(container)) {
                        page.openEditor(editorInput,
                                        "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor");
                        return;
                    }
                }
            }
            page.openEditor(new BIPModuleEditorInput(container),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public BIPEditor() {
        setEditDomain(new DefaultEditDomain(this));
    }

    public void setEditorTitle(String title) {
        setPartName(title);
    }

    protected IModel getModel() {
        if (model == null) {
            IEditorInput editorInput = getEditorInput();
            if (editorInput instanceof BIPModuleEditorInput) {
                BIPModuleEditorInput bipModuleEditorInput = (BIPModuleEditorInput) editorInput;
                model = bipModuleEditorInput.getModel();
            }
        }
        return model;
    }

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        setEditorTitle(getModel().getName());
        viewer = getGraphicalViewer();
        viewerMap.put(viewer, this);
        viewer.setContents(getModel());
        viewer.setContextMenu(new BipContextMenuProvider(viewer, getActionRegistry()));
        if (getModel() instanceof AtomicTypeModel) {
            createAtomicPaletteDrawer(paletteRoot);
        }
        if (getModel() instanceof CompoundTypeModel) {
            createCompoundPaletteDrawer(paletteRoot);
        }
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        paletteRoot = new PaletteRoot();

        PaletteGroup toolGroup = new PaletteGroup("选择工具");
        ToolEntry tool = new SelectionToolEntry("选择");
        toolGroup.add(tool);
        tool = new MarqueeToolEntry("选择多个");
        toolGroup.add(tool);
        paletteRoot.add(toolGroup);
        return paletteRoot;
    }

    private void createCompoundPaletteDrawer(PaletteRoot root) {
        PaletteDrawer drawer;
        ImageDescriptor descriptor;
        drawer = new PaletteDrawer("复合组件");
        descriptor = Activator.getImageDescriptor("icons/atomic_16.png");
        CreationToolEntry creationAtomicEntry =
                        new CreationToolEntry("原子组件", "新建一个原子组件", new SimpleFactory(
                                        AtomicTypeModel.class), descriptor, descriptor);
        descriptor = Activator.getImageDescriptor("icons/compound_16.png");
        CreationToolEntry creationCompoundEntry =
                        new CreationToolEntry("复合组件", "新建一个复合组件", new SimpleFactory(
                                        CompoundTypeModel.class), descriptor, descriptor);
        descriptor = Activator.getImageDescriptor("icons/connector_16.png");
        CreationToolEntry creationConnectorEntry =
                        new CreationToolEntry("连接子", "新建一个连接子", new SimpleFactory(
                                        ConnectorTypeModel.class), descriptor, descriptor);
        descriptor = Activator.getImageDescriptor("icons/connectorline_16.png");
        // ConnectionCreationToolEntry creationConnectorLineEntry =
        // new ConnectionCreationToolEntry("Connector line",
        // "create a new connector line", new SimpleFactory(
        // ConnectorPortModel.class), descriptor,
        // descriptor);
        drawer.add(creationAtomicEntry);
        drawer.add(creationCompoundEntry);
        drawer.add(creationConnectorEntry);
        // drawer.add(creationConnectorLineEntry);
        root.add(drawer);
    }

    private void createAtomicPaletteDrawer(PaletteRoot root) {
        PaletteDrawer drawer = new PaletteDrawer("原子组件");
        ImageDescriptor descriptor = Activator.getImageDescriptor("icons/place_16.png");
        CreationToolEntry placeCreationEntry =
                        new CreationToolEntry("状态", "新建一个状态", new SimpleFactory(PlaceModel.class),
                                        descriptor, descriptor);
        descriptor = Activator.getImageDescriptor("icons/transition_16.png");
        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry("迁移", "新建一个迁移", new SimpleFactory(
                                        TransitionModel.class), descriptor, descriptor);
        drawer.add(placeCreationEntry);
        drawer.add(connectionCreationEntry);
        root.add(drawer);
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

    // 缩放
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart())
                            .getZoomManager();
        }
        // 如果是 IContentOutlinePage 类型，则返回该 ContentOutlinePage
        // if (type == IContentOutlinePage.class) {
        // return new BIPContentOutlinePage(new TreeViewer());
        // }
        return super.getAdapter(type);
    }

    /** Save properties to model */
    protected void saveProperties() {

    }

    /** Load properties */
    protected void loadProperties() {

    }

    // 公共的快捷键
    protected KeyHandler getCommonKeyHandler() {

        KeyHandler sharedKeyHandler = null;
        if (sharedKeyHandler == null) {

            sharedKeyHandler = new KeyHandler();

            sharedKeyHandler.put(

            KeyStroke.getPressed(SWT.F2, 0),

            getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));

            /*
             * sharedKeyHandler.put(KeyStroke.getReleased(SWT.CTRL, SWT.ARROW_LEFT
             * ),getActionRegistry().getAction(GEFActionConstants.ALIGN_LEFT));
             */

        }

        return sharedKeyHandler;

    }

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

    @Override
    protected void configureGraphicalViewer() {
        super.configureGraphicalViewer();
        ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
        getGraphicalViewer().setRootEditPart(rootEditPart);
        getGraphicalViewer().setEditPartFactory(PartFactory.getInstance());

        ZoomManager manager = rootEditPart.getZoomManager();

        // 放大比例数组
        double[] zoomLevels = new double[] {
                        // 缩放比例是从 25％－2000％
                        0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0};
        manager.setZoomLevels(zoomLevels); // 添加放大比例

        // 设置非百分比缩放
        ArrayList<String> zoomContributions = new ArrayList<String>();
        zoomContributions.add(ZoomManager.FIT_ALL);
        zoomContributions.add(ZoomManager.FIT_HEIGHT);
        zoomContributions.add(ZoomManager.FIT_WIDTH);
        manager.setZoomLevelContributions(zoomContributions);

        IAction action = new ZoomInAction(manager);
        getActionRegistry().registerAction(action);
        action = new ZoomOutAction(manager);
        getActionRegistry().registerAction(action);

        // 网格
        action = new ToggleGridAction(getGraphicalViewer());
        getActionRegistry().registerAction(action);

        // CTRL+鼠标滑轮实现缩放
        getGraphicalViewer().setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CTRL),
                        MouseWheelZoomHandler.SINGLETON);
        // 采用公共的快捷键
        getGraphicalViewer().setKeyHandler(
                        new GraphicalViewerKeyHandler(getGraphicalViewer())
                                        .setParent(getCommonKeyHandler()));
        loadProperties();
    }
}
