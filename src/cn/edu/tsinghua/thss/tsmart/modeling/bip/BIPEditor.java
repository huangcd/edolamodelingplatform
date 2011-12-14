package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
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
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.PartFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

@SuppressWarnings("rawtypes")
public abstract class BIPEditor extends GraphicalEditorWithFlyoutPalette {

    protected static HashMap<EditPartViewer, BIPEditor> viewerMap;
    private IModel                                      model;
    private PaletteRoot                                 paletteRoot;

    public static BIPEditor getEditorFromViewer(EditPartViewer viewer) {
        if (viewerMap == null) {
            viewerMap = new HashMap<EditPartViewer, BIPEditor>();
        }
        return viewerMap.get(viewer);
    }

    public static void putViewerEditorEntry(EditPartViewer viewer, BIPEditor editor) {
        if (viewerMap == null) {
            viewerMap = new HashMap<EditPartViewer, BIPEditor>();
        }
        viewerMap.put(viewer, editor);
    }

<<<<<<< HEAD
    public static void removeViewerEditEntry(EditPartViewer viewer) {
        viewerMap.remove(viewer);
    }

=======
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0
    /**
     * 打开一个编辑模型的页面
     * 
     * @param container 待编辑模型
     */
    public static void openBIPEditor(IContainer container) {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        String editorID = "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor";
        if (container instanceof AtomicTypeModel) {
            editorID = "cn.edu.tsinghua.thss.tsmart.modeling.bip.AtomicEditor";
        } else if (container instanceof CompoundTypeModel) {
            editorID = "cn.edu.tsinghua.thss.tsmart.modeling.bip.CompoundEditor";
        }
        try {
            // 如果页面已经打开，则跳转到指定页面
            for (IEditorReference reference : page.getEditorReferences()) {
                if (reference.getEditorInput() instanceof BIPModuleEditorInput) {
                    BIPModuleEditorInput editorInput =
                                    (BIPModuleEditorInput) reference.getEditorInput();
                    if (editorInput.getModel().equals(container)) {
                        page.openEditor(editorInput, editorID);
                        return;
                    }
                }
            }
            page.openEditor(new BIPModuleEditorInput(container), editorID);
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

    public static ImageDescriptor getImage(String location) {
        return Activator.getImageDescriptor(location);
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        if (paletteRoot == null) {
            paletteRoot = new PaletteRoot();

            PaletteGroup toolGroup = new PaletteGroup("选择工具");
            ToolEntry tool = new SelectionToolEntry("选择");
            toolGroup.add(tool);
            tool = new MarqueeToolEntry("选择多个");
            toolGroup.add(tool);
            paletteRoot.add(toolGroup);
        }
        return paletteRoot;
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
    protected abstract void saveProperties();

    /** Load properties */
    protected abstract void loadProperties();

    // 公共的快捷键
    protected KeyHandler getCommonKeyHandler() {
        KeyHandler sharedKeyHandler = null;
        if (sharedKeyHandler == null) {
            sharedKeyHandler = new KeyHandler();
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0),
                            getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
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
