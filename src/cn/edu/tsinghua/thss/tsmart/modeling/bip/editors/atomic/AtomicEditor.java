package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.SaveComponentTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPModuleEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.PlaceCreationTool;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

@SuppressWarnings("rawtypes")
public class AtomicEditor extends BIPEditor {
    public final static String id = AtomicEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private PaletteDrawer      dataPalette;
    private PaletteDrawer      portPalette;

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        setEditorTitle(getModel().getName());
        viewer = getGraphicalViewer();
        putViewerEditorEntry(viewer, this);
        viewer.setContents(getModel());
        viewer.setContextMenu(new AtomicEditorContextMenuProvider(viewer, getActionRegistry()));
        initAtomicPalette();
        initDataCreationEntry();
        initPortCreationEntry();
    }

    /**
     * 动态增加一个data创建按钮
     * 
     * @param entry
     */
    public void addDataCreationToolEntry(CreationToolEntry entry) {
        dataPalette.add(entry);
    }

    /**
     * 动态删除一个data创建按钮
     * 
     * @param entry
     */
    public void removeDataCreationToolEntry(CreationToolEntry entry) {
        dataPalette.remove(entry);
    }

    /**
     * 动态增加一个port创建按钮
     * 
     * @param entry
     */
    public void addPortCreationToolEntry(CreationToolEntry entry) {
        portPalette.add(entry);
    }

    /**
     * 动态删除一个port创建按钮
     * 
     * @param entry
     */
    public void removePortCreationToolEntry(CreationToolEntry entry) {
        portPalette.remove(entry);
    }

    private void initDataCreationEntry() {
        CreationToolEntry boolCreationEntry =
                        new CreationToolEntry("布尔", "增加一个布尔变量", new CopyFactory(
                                        DataTypeModel.getDataTypeModel("bool")),
                                        getImage("icons/bool_16.png"),
                                        getImage("icons/bool_32.png"));
        addDataCreationToolEntry(boolCreationEntry);
        if (GlobalProperties.getInstance().isMultipleDataTypeAvailble()) {
            CreationToolEntry intCreationEntry =
                            new CreationToolEntry("整数", "增加一个整数变量", new CopyFactory(
                                            DataTypeModel.getDataTypeModel("int")),
                                            getImage("icons/int_16.png"),
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
                                        PortTypeModel.getPortTypeModel("ePort")),
                                        getImage("icons/port_16.png"),
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

    private void initAtomicPalette() {
        PlaceCreationToolEntry placeCreationEntry =
                        new PlaceCreationToolEntry("状态", "新建一个状态", new SimpleFactory(
                                        PlaceTypeModel.class), getImage("icons/place_16.png"),
                                        getImage("icons/place_32.png"));

        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry("迁移", "新建一个迁移", new SimpleFactory(
                                        TransitionTypeModel.class),
                                        getImage("icons/transition_16.png"),
                                        getImage("icons/transition_32.png"));
        getToolGroup().add(placeCreationEntry);
        getToolGroup().add(connectionCreationEntry);

        dataPalette = new PaletteDrawer("变量类型");
        portPalette = new PaletteDrawer("端口类型");

        getPaletteRoot().add(dataPalette);
        getPaletteRoot().add(portPalette);
    }

    public void dispose() {
        removeViewerEditEntry(viewer);
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        if (getEditorInput() instanceof BIPModuleEditorInput) {
            return;
        }
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

        action = new SaveComponentTypeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new ExportAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

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

    /** Save properties to model */
    protected void saveProperties() {}

    /** Load properties */
    protected void loadProperties() {}

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
