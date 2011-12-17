package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.PlaceCreationTool;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

@SuppressWarnings("rawtypes")
public class AtomicEditor extends BIPEditor {
    public final static String id = AtomicEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private IModel             model;
    private PaletteDrawer      dataStack;
    private PaletteDrawer      portStack;

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        setEditorTitle(getModel().getName());
        viewer = getGraphicalViewer();
        putViewerEditorEntry(viewer, this);
        viewer.setContents(getModel());
        viewer.setContextMenu(new AtomicEditorContextMenuProvider(viewer, getActionRegistry()));
        initPaletteRoot();
        initDataCreationEntry();
        initPortCreationEntry();
    }

    /**
     * 动态增加一个data创建按钮
     * 
     * @param entry
     */
    public void addDataCreationToolEntry(CreationToolEntry entry) {
        dataStack.add(entry);
    }

    /**
     * 动态删除一个data创建按钮
     * 
     * @param entry
     */
    public void removeDataCreationToolEntry(CreationToolEntry entry) {
        dataStack.remove(entry);
    }

    /**
     * 动态增加一个port创建按钮
     * 
     * @param entry
     */
    public void addPortCreationToolEntry(CreationToolEntry entry) {
        portStack.add(entry);
    }

    /**
     * 动态删除一个port创建按钮
     * 
     * @param entry
     */
    public void removePortCreationToolEntry(CreationToolEntry entry) {
        portStack.remove(entry);
    }

    private void initDataCreationEntry() {
        CreationToolEntry boolCreationEntry =
                        new CreationToolEntry("布尔", "增加一个布尔变量", new CopyFactory(
                                        DataTypeModel.boolData), getImage("icons/bool_16.png"),
                                        getImage("icons/bool_32.png"));
        addDataCreationToolEntry(boolCreationEntry);
        if (GlobalProperties.getInstance().isMultipleDataTypeAvailble()) {
            CreationToolEntry intCreationEntry =
                            new CreationToolEntry("整数", "增加一个整数变量", new CopyFactory(
                                            DataTypeModel.intData), getImage("icons/int_16.png"),
                                            getImage("icons/int_32.png"));
            addDataCreationToolEntry(intCreationEntry);
            for (Map.Entry<String, DataTypeModel> entry : DataTypeModel.getTypeEntries()) {
                if (entry.getKey().equals("int") || entry.getKey().equals("bool")) {
                    continue;
                }
                CreationToolEntry creationToolEntry =
                                new CreationToolEntry(entry.getKey(), "新建一个" + entry.getKey()
                                                + "变量", new CopyFactory(entry.getValue()),
                                                BIPEditor.getImage("icons/new_data_16.png"),
                                                BIPEditor.getImage("icons/new_data_32.png"));
                addDataCreationToolEntry(creationToolEntry);
                DataTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
            }
        }
    }

    private void initPortCreationEntry() {
        CreationToolEntry ePortCreationEntry =
                        new CreationToolEntry("ePort", "增加一个空端口", new CopyFactory(
                                        PortTypeModel.ePortType), getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(ePortCreationEntry);
        for (Map.Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
            if (entry.getKey().equals("ePort")) {
                continue;
            }
            CreationToolEntry creationToolEntry =
                            new CreationToolEntry(entry.getKey(), "新建一个" + entry.getKey() + "端口",
                                            new CopyFactory(entry.getValue()),
                                            BIPEditor.getImage("icons/port_16.png"),
                                            BIPEditor.getImage("icons/port_32.png"));
            addPortCreationToolEntry(creationToolEntry);
            PortTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
        }
    }

    // TODO
    private void initPaletteRoot() {
        PaletteDrawer atomicPalette = new PaletteDrawer("原子组件");
        PlaceCreationToolEntry placeCreationEntry =
                        new PlaceCreationToolEntry("状态", "新建一个状态", new SimpleFactory(
                                        PlaceTypeModel.class), getImage("icons/place_16.png"),
                                        getImage("icons/place_32.png"));

        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry("迁移", "新建一个迁移", new SimpleFactory(
                                        TransitionTypeModel.class),
                                        getImage("icons/transition_16.png"),
                                        getImage("icons/transition_32.png"));
        // dataStack = new PaletteStack("变量", "增加一个变量", getImage("icons/new_data_32.png"));
        // portStack = new PaletteStack("端口", "增加一个端口", getImage("icons/port_32.png"));
        // atomicPalette.add(dataStack);
        // atomicPalette.add(portStack);
        // getPaletteRoot().setDefaultEntry(placeCreationEntry);
        atomicPalette.add(placeCreationEntry);
        atomicPalette.add(connectionCreationEntry);

        dataStack = new PaletteDrawer("变量类型");
        portStack = new PaletteDrawer("端口类型");

        getPaletteRoot().add(atomicPalette);
        getPaletteRoot().add(dataStack);
        getPaletteRoot().add(portStack);
        // getPaletteRoot().add(portStack);
    }

    public void dispose() {
        removeViewerEditEntry(viewer);
        System.out.println("remove viewer from viewerEditorMap");
        super.dispose();
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

        IAction action;
        // action = new CreateDataTypeAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());

        // IAction action = new CreateDataAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());
        //
        // action = new CreatePriorityAction(this);
        // registry.registerAction(action);
        // getSelectionActions().add(action.getId());

        action = new ExportAction(this);
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
        if (type == IContentOutlinePage.class) {
            return new BIPContentOutlinePage(new TreeViewer());
        }
        return super.getAdapter(type);
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

    class PlaceCreationToolEntry extends ToolEntry {
        protected final CreationFactory factory;

        public PlaceCreationToolEntry(String label, String shortDesc, CreationFactory factory,
                        ImageDescriptor iconSmall, ImageDescriptor iconLarge) {
            super(label, shortDesc, iconSmall, iconLarge, PlaceCreationTool.class);
            this.factory = factory;
            setToolProperty(CreationTool.PROPERTY_CREATION_FACTORY, factory);
        }
    }
}
