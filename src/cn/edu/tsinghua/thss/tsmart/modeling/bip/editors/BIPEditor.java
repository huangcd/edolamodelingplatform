package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetSorter;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.CopyComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.PasteComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveComponentTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveTopLevelModelInContextMenuAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.PartFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class BIPEditor extends GraphicalEditorWithFlyoutPalette {
    protected static HashMap<EditPartViewer, BIPEditor> viewerMap =
                                                                                  new HashMap<EditPartViewer, BIPEditor>();
    private static IInstance                            copyObject;

    public static IInstance getCopyObject() {
        if (copyObject == null) {
            return null;
        }
        IInstance newObject = (IInstance) copyObject.setName(copyObject.getName());
        newObject.getType().setName(copyObject.getType().getName());
        return newObject;
    }

    public static void setCopyObject(IModel obj) {
        if (obj == null) {
            copyObject = null;
            return;
        }
        ComponentTypeModel model = null;
        if (obj instanceof IType) {
            copyObject = (IInstance) ((IType) obj).getInstance().copy();
            copyObject.setName(((IType) obj).getInstance().getName());
            if (obj instanceof ComponentTypeModel) {
                model = (ComponentTypeModel) copyObject.getType();
                model.setName(obj.getName());
            }
        } else {
            copyObject = (IInstance) obj.copy();
            copyObject.setName(obj.getName());
            if (obj instanceof ComponentModel) {
                model = (ComponentTypeModel) ((ComponentModel) copyObject).getType();
                model.setName(((ComponentModel) obj).getType().getName());
            }
        }
        if (model != null) {
            List<PortModel> ports = model.getExportPorts();
            for (PortModel port : ports) {
                port.getBullet().getTargetConnections().clear();
            }
        }
    }

    public static boolean isCopyObject(CompoundTypeModel model) {
        return copyObject.equals(model);
    }

    public static BIPEditor getEditorFromViewer(EditPartViewer viewer) {
        return viewerMap.get(viewer);
    }

    public static void putViewerEditorEntry(EditPartViewer viewer, BIPEditor editor) {
        viewerMap.put(viewer, editor);
    }

    public static void removeViewerEditEntry(EditPartViewer viewer) {
        viewerMap.remove(viewer);
    }

    public static void closeEditor(ComponentTypeModel model) {
        // TODO 关闭页面
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        try {
            // 如果页面已经打开，则跳转到指定页面
            for (IEditorReference reference : page.getEditorReferences()) {
                if (reference.getEditorInput() instanceof BIPModelEditorInput) {
                    BIPModelEditorInput editorInput =
                                    (BIPModelEditorInput) reference.getEditorInput();
                    if (editorInput.getModel().equals(model)) {
                        page.closeEditor(reference.getEditor(true), false);
                        return;
                    }
                }
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    public static void closeAllEditor() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        page.closeEditors(page.getEditorReferences(), true);
    }

    /**
     * 打开一个编辑模型的页面
     * 
     * @param container 待编辑模型
     */
    public static void openBIPEditor(ComponentTypeModel container) {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        String editorID = "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor";
        if (container instanceof AtomicTypeModel) {
            editorID = AtomicEditor.id;
        } else if (container instanceof CompoundTypeModel) {
            editorID = CompoundEditor.id;
        }
        try {
            // 如果页面已经打开，则跳转到指定页面
            for (IEditorReference reference : page.getEditorReferences()) {
                if (reference.getEditorInput() instanceof BIPModelEditorInput) {
                    BIPModelEditorInput editorInput =
                                    (BIPModelEditorInput) reference.getEditorInput();
                    if (editorInput.getModel().equals(container)) {
                        page.openEditor(editorInput, editorID);
                        return;
                    }
                }
            }
            page.openEditor(new BIPModelEditorInput(container), editorID);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回当前所有的AtomicEditor
     */
    public static ArrayList<AtomicEditor> getAtomicEditors() {
        ArrayList<AtomicEditor> list = new ArrayList<AtomicEditor>();
        for (BIPEditor part : viewerMap.values()) {
            if (part instanceof AtomicEditor) {
                AtomicEditor editor = (AtomicEditor) part;
                list.add(editor);
            }
        }
        return list;
    }

    /**
     * 返回当前所有的CompoundEditor
     */
    public static ArrayList<CompoundEditor> getCompoundEditors() {
        ArrayList<CompoundEditor> list = new ArrayList<CompoundEditor>();
        for (BIPEditor part : viewerMap.values()) {
            if (part instanceof CompoundEditor) {
                CompoundEditor editor = (CompoundEditor) part;
                list.add(editor);
            }
        }
        return list;
    }

    public static ImageDescriptor getImage(String location) {
        return Activator.getImageDescriptor(location);
    }

    private ComponentTypeModel model;
    private PaletteRoot        paletteRoot;
    private PaletteGroup       toolGroup;

    public BIPEditor() {
        setEditDomain(new DefaultEditDomain(this));
    }

    public void setEditorTitle(String title) {
        setPartName(title);
    }

    public ComponentTypeModel getModel() {
        if (model == null) {
            IEditorInput iEditorInput = getEditorInput();
            if (iEditorInput instanceof BIPModelEditorInput) {
                BIPModelEditorInput editorInput = (BIPModelEditorInput) iEditorInput;
                model = editorInput.getModel();
            } else if (iEditorInput instanceof BIPFileEditorInput) {
                BIPFileEditorInput editorInput = (BIPFileEditorInput) iEditorInput;
                model = editorInput.getModel();
            }
        }
        return model;
    }

    public boolean isSaveOnCloseNeeded() {
        if (getEditorInput() instanceof BIPModelEditorInput) {
            return false;
        }
        return isDirty();
    }

    @Override
    protected PaletteRoot getPaletteRoot() {
        if (paletteRoot == null) {
            paletteRoot = new PaletteRoot();

            toolGroup = new PaletteGroup("选择工具");
            ToolEntry tool = new SelectionToolEntry("选择");
            toolGroup.add(tool);
            tool = new MarqueeToolEntry("选择多个");
            toolGroup.add(tool);
            paletteRoot.add(toolGroup);
        }
        return paletteRoot;
    }

    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart())
                            .getZoomManager();
        }
        if (type == IContentOutlinePage.class) {
            return new BIPContentOutlinePage(new TreeViewer());
        }
        if (type.equals(IPropertySheetPage.class)) {
            return new PropertySheetPage() {
                public void createControl(Composite parent) {
                    // super.createControl(parent);
                    PropertySheetSorter sorter = new PropertySheetSorter() {
                        public int compare(IPropertySheetEntry entryA, IPropertySheetEntry entryB) {
                            return getCollator().compare(entryA.getDescription(),
                                            entryB.getDescription());
                        }
                    };
                    this.setSorter(sorter);
                    super.createControl(parent);
                }
            };
        }
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
            sharedKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry()
                            .getAction(ActionFactory.DELETE.getId())); // Delete
            sharedKeyHandler.put(KeyStroke.getReleased('', 122, SWT.CTRL), getActionRegistry()
                            .getAction(ActionFactory.UNDO.getId()));// undo
            sharedKeyHandler.put(KeyStroke.getReleased('', 121, SWT.CTRL), getActionRegistry()
                            .getAction(ActionFactory.REDO.getId()));// redo
            sharedKeyHandler.put(KeyStroke.getReleased('', 97, SWT.CTRL), getActionRegistry()
                            .getAction(ActionFactory.SELECT_ALL.getId()));// Select-all
            sharedKeyHandler.put(
                            KeyStroke.getReleased((char) 0x03, 99, SWT.CTRL),
                            getActionRegistry().getAction(
                                            CopyComponentAction.class.getCanonicalName())); // 复制
                                                                                            // Ctrl+C
            sharedKeyHandler.put(
                            KeyStroke.getReleased((char) 0x16, 118, SWT.CTRL),
                            getActionRegistry().getAction(
                                            PasteComponentAction.class.getCanonicalName())); // 黏贴
                                                                                             // Ctrl+V
            sharedKeyHandler.put(KeyStroke.getReleased((char) 24, (int) 'x', SWT.CTRL),
                            getActionRegistry().getAction(ActionFactory.CUT.getId())); // 剪切 Ctrl+X

            sharedKeyHandler.put(
                            KeyStroke.getReleased((char) 19, (int) 's', SWT.CTRL),
                            getActionRegistry().getAction(
                                            SaveComponentTypeAction.class.getCanonicalName())); // 保存
                                                                                                // CTRL+S

            // TODO Save Project。ctrl+shift+s是自带的，保存当前所有的页面。

            sharedKeyHandler.put(
                            KeyStroke.getReleased((char) 19, (int) 's', SWT.ALT),
                            getActionRegistry().getAction(
                                SaveTopLevelModelInContextMenuAction.class.getCanonicalName())); // 保存
                                                                                          // ALT+S
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
            ActionRegistry registry = getActionRegistry();
            IActionBars bars = pageSite.getActionBars();

            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            bars.updateActionBars();
        }

        @Override
        public void createControl(Composite parent) {
            getViewer().createControl(parent);
            getViewer().setEditDomain(getEditDomain());
            getViewer().setEditPartFactory(TreeEditPartFactory.getInstance());
            model = getModel();
            if (model != null) {
                getViewer().setContents(model);
            }
            getSelectionSynchronizer().addViewer(getViewer());

        }

        @Override
        public Control getControl() {
            return getViewer().getControl();
        }

        public void dispose() {
            // 从 TreeViewer 中删除 SelectionSynchronizer
            getSelectionSynchronizer().removeViewer(getViewer());
            super.dispose();
        }
    }

    public PaletteGroup getToolGroup() {
        return toolGroup;
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
