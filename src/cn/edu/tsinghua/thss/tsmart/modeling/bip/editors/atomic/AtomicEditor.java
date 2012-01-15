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
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.CopyComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.VariableSelectionAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.AlignDataAndTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveComponentLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveComponentTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveTopLevelModelInContextMenuAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPFileEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.PlaceCreationTool;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class AtomicEditor extends BIPEditor {
    public final static String id = AtomicEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private PaletteDrawer      dataPalette;
    private PaletteDrawer      portPalette;

    @Override
    protected void initializeGraphicalViewer() {
        super.initializeGraphicalViewer();
        IEditorInput input = getEditorInput();
        if (input instanceof BIPFileEditorInput) {
            setEditorTitle(((BIPFileEditorInput) input).getName());
        } else {
            setEditorTitle(getModel().getName());
        }
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
                        new CreationToolEntry(Messages.AtomicEditor_0, Messages.AtomicEditor_1, new CopyFactory(
                                        DataTypeModel.getModelByName("bool")), //$NON-NLS-1$
                                        getImage("icons/bool_16.png"), //$NON-NLS-1$
                                        getImage("icons/bool_32.png")); //$NON-NLS-1$
        addDataCreationToolEntry(boolCreationEntry);
        if (GlobalProperties.getInstance().isMultipleDataTypeAvailble()) {
            CreationToolEntry intCreationEntry =
                            new CreationToolEntry(Messages.AtomicEditor_5, Messages.AtomicEditor_6, new CopyFactory(
                                            DataTypeModel.getModelByName("int")), //$NON-NLS-1$
                                            getImage("icons/int_16.png"), //$NON-NLS-1$
                                            getImage("icons/int_32.png")); //$NON-NLS-1$
            addDataCreationToolEntry(intCreationEntry);
            for (Map.Entry<String, DataTypeModel> entry : DataTypeModel.getTypeEntries()) {
                if (entry.getKey().equals("int") || entry.getKey().equals("bool")) { //$NON-NLS-1$ //$NON-NLS-2$
                    continue;
                }
                CreationToolEntry creationToolEntry =
                                new CreationToolEntry(entry.getKey(), Messages.AtomicEditor_12 + entry.getKey()
                                                + Messages.AtomicEditor_13, new CopyFactory(entry.getValue()),
                                                BIPEditor.getImage("icons/new_data_16.png"), //$NON-NLS-1$
                                                BIPEditor.getImage("icons/new_data_32.png")); //$NON-NLS-1$
                addDataCreationToolEntry(creationToolEntry);
                DataTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
            }
        }
    }

    private void initPortCreationEntry() {
        CreationToolEntry ePortCreationEntry =
                        new CreationToolEntry("ePort", Messages.AtomicEditor_17, new CopyFactory( //$NON-NLS-1$
                                        PortTypeModel.getModelByName("ePort")), //$NON-NLS-1$
                                        getImage("icons/port_16.png"), //$NON-NLS-1$
                                        getImage("icons/port_32.png")); //$NON-NLS-1$
        addPortCreationToolEntry(ePortCreationEntry);
        for (Map.Entry<String, PortTypeModel> entry : PortTypeModel.getTypeEntries()) {
            if (entry.getKey().equals("ePort")) { //$NON-NLS-1$
                continue;
            }
            CreationToolEntry creationToolEntry =
                            new CreationToolEntry(entry.getKey(), Messages.AtomicEditor_22
                                            + entry.getValue().exportToBip(), new CopyFactory(
                                            entry.getValue()),
                                            BIPEditor.getImage("icons/port_16.png"), //$NON-NLS-1$
                                            BIPEditor.getImage("icons/port_32.png")); //$NON-NLS-1$
            addPortCreationToolEntry(creationToolEntry);
            PortTypeModel.addToolEntry(entry.getKey(), this, creationToolEntry);
        }
    }

    private void initAtomicPalette() {
        PlaceCreationToolEntry placeCreationEntry =
                        new PlaceCreationToolEntry(Messages.AtomicEditor_25, Messages.AtomicEditor_26, new SimpleFactory(
                                        PlaceModel.class), getImage("icons/place_16.png"), //$NON-NLS-1$
                                        getImage("icons/place_32.png")); //$NON-NLS-1$

        ConnectionCreationToolEntry connectionCreationEntry =
                        new ConnectionCreationToolEntry(Messages.AtomicEditor_29, Messages.AtomicEditor_30, new SimpleFactory(
                                        TransitionModel.class),
                                        getImage("icons/transition_16.png"), //$NON-NLS-1$
                                        getImage("icons/transition_32.png")); //$NON-NLS-1$
        getToolGroup().add(placeCreationEntry);
        getToolGroup().add(connectionCreationEntry);

        dataPalette = new PaletteDrawer(Messages.AtomicEditor_33);
        portPalette = new PaletteDrawer(Messages.AtomicEditor_34);

        getPaletteRoot().add(dataPalette);
        getPaletteRoot().add(portPalette);
    }

    public void dispose() {
        removeViewerEditEntry(viewer);
        GlobalProperties.getInstance().getTopModel().removeOpenModel((ComponentTypeModel) getModel());
        super.dispose();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        saveProperties();
        new SaveComponentTypeAction(this).run();
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

        action = new AlignDataAndTypeAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());        
        
        action = new SaveComponentLibraryAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new SaveTopLevelModelInContextMenuAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new VariableSelectionAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

        action = new CopyComponentAction(this);
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
