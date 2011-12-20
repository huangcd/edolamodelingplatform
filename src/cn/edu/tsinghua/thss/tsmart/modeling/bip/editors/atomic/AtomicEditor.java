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
     * ��̬����һ��data������ť
     * 
     * @param entry
     */
    public void addDataCreationToolEntry(CreationToolEntry entry) {
        dataPalette.add(entry);
    }

    /**
     * ��̬ɾ��һ��data������ť
     * 
     * @param entry
     */
    public void removeDataCreationToolEntry(CreationToolEntry entry) {
        dataPalette.remove(entry);
    }

    /**
     * ��̬����һ��port������ť
     * 
     * @param entry
     */
    public void addPortCreationToolEntry(CreationToolEntry entry) {
        portPalette.add(entry);
    }

    /**
     * ��̬ɾ��һ��port������ť
     * 
     * @param entry
     */
    public void removePortCreationToolEntry(CreationToolEntry entry) {
        portPalette.remove(entry);
    }

    private void initDataCreationEntry() {
        CreationToolEntry boolCreationEntry =
                        new CreationToolEntry("����", "����һ����������", new CopyFactory(
                                        DataTypeModel.getDataTypeModel("bool")),
                                        getImage("icons/bool_16.png"),
                                        getImage("icons/bool_32.png"));
        addDataCreationToolEntry(boolCreationEntry);
        if (GlobalProperties.getInstance().isMultipleDataTypeAvailble()) {
            CreationToolEntry intCreationEntry =
                            new CreationToolEntry("����", "����һ����������", new CopyFactory(
                                            DataTypeModel.getDataTypeModel("int")),
                                            getImage("icons/int_16.png"),
                                            getImage("icons/int_32.png"));
            addDataCreationToolEntry(intCreationEntry);
            for (Map.Entry<String, DataTypeModel> entry : DataTypeModel.getTypeEntries()) {
                if (entry.getKey().equals("int") || entry.getKey().equals("bool")) {
                    continue;
                }
                CreationToolEntry creationToolEntry =
                                new CreationToolEntry(entry.getKey(), "�½�һ��" + entry.getKey()
                                                + "����", new CopyFactory(entry.getValue()),
                                                BIPEditor.getImage("icons/new_data_16.png"),
                                                BIPEditor.getImage("icons/new_data_32.png"));
                addDataCreationToolEntry(creationToolEntry);
                DataTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
            }
        }
    }

    private void initPortCreationEntry() {
        CreationToolEntry ePortCreationEntry =
                        new CreationToolEntry("ePort", "����һ���ն˿�", new CopyFactory(
                                        PortTypeModel.getPortTypeModel("ePort")),
                                        getImage("icons/port_16.png"),
                                        getImage("icons/port_32.png"));
        addPortCreationToolEntry(ePortCreationEntry);
        for (Map.Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
            if (entry.getKey().equals("ePort")) {
                continue;
            }
            CreationToolEntry creationToolEntry =
                            new CreationToolEntry(entry.getKey(), "�½�һ��" + entry.getKey() + "�˿�",
                                            new CopyFactory(entry.getValue()),
                                            BIPEditor.getImage("icons/port_16.png"),
                                            BIPEditor.getImage("icons/port_32.png"));
            addPortCreationToolEntry(creationToolEntry);
            PortTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
        }
    }

    private void initAtomicPalette() {
        PlaceCreationToolEntry placeCreationEntry =
                        new PlaceCreationToolEntry("״̬", "�½�һ��״̬", new SimpleFactory(
                                        PlaceTypeModel.class), getImage("icons/place_16.png"),
                                        getImage("icons/place_32.png"));

        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry("Ǩ��", "�½�һ��Ǩ��", new SimpleFactory(
                                        TransitionTypeModel.class),
                                        getImage("icons/transition_16.png"),
                                        getImage("icons/transition_32.png"));
        getToolGroup().add(placeCreationEntry);
        getToolGroup().add(connectionCreationEntry);

        dataPalette = new PaletteDrawer("��������");
        portPalette = new PaletteDrawer("�˿�����");

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
