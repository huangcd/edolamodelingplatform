package cn.edu.tsinghua.thss.tsmart.modeling.bip;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
<<<<<<< HEAD
=======
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.TreeEditPartFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.handles.PlaceCreationTool;
<<<<<<< HEAD
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;
=======
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0

@SuppressWarnings("rawtypes")
public class AtomicEditor extends BIPEditor {
    private GraphicalViewer viewer;
    private IModel          model;
    private PaletteStack    dataStack;
    private PaletteStack    portStack;

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        setEditorTitle(getModel().getName());
        viewer = getGraphicalViewer();
        putViewerEditorEntry(viewer, this);
        viewer.setContents(getModel());
<<<<<<< HEAD
        viewer.setContextMenu(new AtomicEditorContextMenuProvider(viewer, getActionRegistry()));
=======
        viewer.setContextMenu(new BipContextMenuProvider(viewer, getActionRegistry()));
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0
        initPaletteRoot();
        initDataCreationEntry();
        initPortCreationEntry();
    }

    /**
     * ��̬����һ��data������ť
     * 
     * @param entry
     */
    public void addDataCreationToolEntry(CreationToolEntry entry) {
        dataStack.add(entry);
    }


    /**
     * ��̬����һ��port������ť
     * 
     * @param entry
     */
    public void addPortCreationToolEntry(CreationToolEntry entry) {
        portStack.add(entry);
    }

    private void initDataCreationEntry() {
        CreationToolEntry boolCreationEntry =
                        new CreationToolEntry("����", "����һ����������", new CopyFactory(
                                        DataTypeModel.boolData), getImage("icons/bool_16.png"),
                                        getImage("icons/bool_32.png"));
        addDataCreationToolEntry(boolCreationEntry);
<<<<<<< HEAD
        if (GlobalProperties.getInstance().isMultipleDataTypeAvailble()) {
            CreationToolEntry intCreationEntry =
                            new CreationToolEntry("����", "����һ����������", new CopyFactory(
                                            DataTypeModel.intData), getImage("icons/int_16.png"),
                                            getImage("icons/int_32.png"));
            addDataCreationToolEntry(intCreationEntry);
        }
        // CreationToolEntry dataCreationEntry =
        // new CreationToolEntry("����", "����һ���Զ������", new CopyFactory(
        // new DataTypeModel<AtomicTypeModel>("")),
        // getImage("icons/new_data_16.png"),
        // getImage("icons/new_data_32.png"));
        // addDataCreationToolEntry(dataCreationEntry);
=======
        CreationToolEntry intCreationEntry =
                        new CreationToolEntry("����", "����һ����������", new CopyFactory(
                                        DataTypeModel.intData), getImage("icons/int_16.png"),
                                        getImage("icons/int_32.png"));
        addDataCreationToolEntry(intCreationEntry);
        CreationToolEntry dataCreationEntry =
                        new CreationToolEntry("����", "����һ���Զ������", new CopyFactory(
                                        new DataTypeModel<AtomicTypeModel>("")),
                                        getImage("icons/new_data_16.png"),
                                        getImage("icons/new_data_32.png"));
        addDataCreationToolEntry(dataCreationEntry);
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0
    }

    private void initPortCreationEntry() {
        CreationToolEntry ePortCreationEntry =
                        new CreationToolEntry("ePort", "����һ���ն˿�", new CopyFactory(
                                        PortTypeModel.ePortType), getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(ePortCreationEntry);
        CreationToolEntry bPortCreationEntry =
                        new CreationToolEntry("bPort", "����һ����һ��bool�����Ķ˿�", new CopyFactory(
                                        PortTypeModel.bPortType), getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(bPortCreationEntry);
        CreationToolEntry iPortCreationEntry =
                        new CreationToolEntry("iPort", "����һ����һ��int�����Ķ˿�", new CopyFactory(
                                        PortTypeModel.iPortType), getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(iPortCreationEntry);
        CreationToolEntry dataCreationEntry =
                        new CreationToolEntry("����", "����һ���Զ���˿�", new CopyFactory(
                                        new PortTypeModel()), getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(dataCreationEntry);
    }

    private void initPaletteRoot() {
        PaletteDrawer atomicPalette = new PaletteDrawer("ԭ�����");
        PlaceCreationToolEntry placeCreationEntry =
                        new PlaceCreationToolEntry("״̬", "�½�һ��״̬", new SimpleFactory(
                                        PlaceTypeModel.class), getImage("icons/place_16.png"),
                                        getImage("icons/place_32.png"));

        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry("Ǩ��", "�½�һ��Ǩ��", new SimpleFactory(
                                        TransitionTypeModel.class),
                                        getImage("icons/transition_16.png"),
                                        getImage("icons/transition_32.png"));
        dataStack = new PaletteStack("����", "����һ������", getImage("icons/new_data_32.png"));
        portStack = new PaletteStack("�˿�", "����һ���˿�", getImage("icons/port_32.png"));
        atomicPalette.add(placeCreationEntry);
        atomicPalette.add(connectionCreationEntry);
        atomicPalette.add(dataStack);
        atomicPalette.add(portStack);
        // getPaletteRoot().setDefaultEntry(placeCreationEntry);
        getPaletteRoot().add(atomicPalette);
    }

<<<<<<< HEAD
    public void dispose() {
        removeViewerEditEntry(viewer);
        System.out.println("remove viewer from viewerEditorMap");
        super.dispose();
    }

=======
>>>>>>> 9000296a9394b07722d699ea835ad479e754b9a0
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

    // ����
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) {
            return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart())
                            .getZoomManager();
        }
        // ����� IContentOutlinePage ���ͣ��򷵻ظ� ContentOutlinePage
        if (type == IContentOutlinePage.class) {
            return new BIPContentOutlinePage(new TreeViewer());
        }
        return super.getAdapter(type);
    }

    /** Save properties to model */
    protected void saveProperties() {}

    /** Load properties */
    protected void loadProperties() {}

    // ���� ContentOutlinePage ��
    class BIPContentOutlinePage extends ContentOutlinePage {
        public BIPContentOutlinePage() {
            // ʹ�� GEF �� TreeViewer
            super(new TreeViewer());
        }

        public BIPContentOutlinePage(EditPartViewer viewer) {
            super(viewer);
        }

        public void init(IPageSite pageSite) {
            super.init(pageSite);
            // ���ע��� graphical editor �� Action
            ActionRegistry registry = getActionRegistry();
            // ʹ��Щ Action �ڴ����ͼ��Ҳ��Ч
            IActionBars bars = pageSite.getActionBars();

            String id = ActionFactory.UNDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.REDO.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));

            id = ActionFactory.DELETE.getId();
            bars.setGlobalActionHandler(id, registry.getAction(id));
            bars.updateActionBars();
        }

        // ����
        @Override
        public void createControl(Composite parent) {
            getViewer().createControl(parent);
            // ���� Edit Domain
            getViewer().setEditDomain(getEditDomain());
            // ���� EditPartFactory
            // getViewer().setEditPartFactory(new TreeEditPartFactory());
            getViewer().setEditPartFactory(TreeEditPartFactory.getInstance());
            // ����ͼ�ж�Ӧ�� BIPModel������
            if (model != null) {
                getViewer().setContents(model);
            }
            // ѡ��ͬ������ Graphical editor ��ѡ��ͼ�Σ�������ͼѡ���Ӧ�Ľڵ㣻��֮��Ȼ
            getSelectionSynchronizer().addViewer(getViewer());

        }

        @Override
        public Control getControl() {
            // �������ͼ�ǵ�ǰ��active����ͼʱ�����ؾ۽��Ŀؼ�
            // return sash;
            return getViewer().getControl();
        }

        public void dispose() {
            // �� TreeViewer ��ɾ�� SelectionSynchronizer
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