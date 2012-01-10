package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.CopyComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.PasteComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.VariableSelectionAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveComponentLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveComponentTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveTopLevelModelInContextMenuAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPFileEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class CompoundEditor extends BIPEditor {
    public final static String id         = CompoundEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private PaletteDrawer      connectorPalette;
    private PaletteDrawer      atomicPalette;
    private PaletteDrawer      compoundPalette;
    private GlobalProperties   properties = GlobalProperties.getInstance();

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
        viewer.setContextMenu(new CompoundEditorContextMenuProvider(viewer, getActionRegistry()));
        initCompoundPalette();
    }

    private void initCompoundPalette() {
        connectorPalette = new PaletteDrawer("连接子");
        getPaletteRoot().add(connectorPalette);
        initConnectorCreationEntry();

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        if (topModel instanceof ProjectModel) {
            initLibrary();
        } else if (topModel instanceof LibraryModel) {
            ToolEntry entry =
                            new CreationToolEntry("原子构件", "新建一个空白的原子构件", new SimpleFactory(
                                            AtomicTypeModel.class),
                                            getImage("icons/atomic_16.png"),
                                            getImage("icons/atomic_32.png"));
            getToolGroup().add(entry);
            entry =
                            new CreationToolEntry("复合构件", "新建一个空白的复合构件", new SimpleFactory(
                                            CompoundTypeModel.class),
                                            getImage("icons/compound_16.png"),
                                            getImage("icons/compound_32.png"));
            getToolGroup().add(entry);
            atomicPalette = new PaletteDrawer("原子构件库");
            getPaletteRoot().add(atomicPalette);

            compoundPalette = new PaletteDrawer("复合构件库");
            getPaletteRoot().add(compoundPalette);
            initAtomicCreationEntry();
            initCompoundCreationEntry();
        }
    }

    /**
     * 载入项目库。
     * 
     */
    public void initLibrary() {
        ITopModel topModel = properties.getTopModel();
        if (topModel instanceof ProjectModel) {
            ProjectModel project = (ProjectModel) properties.getTopModel();
            // 项目连接子
            for (ConnectorTypeModel connector : project.getConnectorTypes()) {
                CreationToolEntry toolEntry =
                                new CreationToolEntry(connector.getName(), "新建一个"
                                                + connector.getName() + "连接子", new CopyFactory(
                                                connector), getImage("icons/connector_16.png"),
                                                getImage("icons/connector_32.png"));
                addConnectorCreationToolEntry(toolEntry);
            }
            // 项目依赖库
            for (ProjectModel.LibraryEntry entry : project.getUsedLibraryEntries()) {
                PaletteDrawer drawer = new PaletteDrawer(entry.getName());
                for (ComponentTypeModel model : entry.getLibs()) {
                    String subImageName =
                                    (model instanceof AtomicTypeModel) ? "atomic" : "compound";
                    CreationToolEntry toolEntry =
                                    new CreationToolEntry(model.getName(), model.getComment(),
                                                    new CopyFactory(model), getImage("icons/"
                                                                    + subImageName + "_16.png"),
                                                    getImage("icons/" + subImageName + "_32.png"));
                    drawer.add(toolEntry);
                }
                getPaletteRoot().add(drawer);
            }
        }
    }

    private void initConnectorCreationEntry() {
        for (Map.Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
            CreationToolEntry toolEntry =
                            new CreationToolEntry(entry.getKey(), "新建一个" + entry.getKey() + "连接子",
                                            new CopyFactory(entry.getValue()),
                                            getImage("icons/connector_16.png"),
                                            getImage("icons/connector_32.png"));
            addConnectorCreationToolEntry(toolEntry);
            ConnectorTypeModel.addToolEntry(entry.getKey(), this, toolEntry);
        }
    }

    private void initAtomicCreationEntry() {
        for (Map.Entry<String, AtomicTypeModel> entry : AtomicTypeModel.getTypeEntries()) {
            AtomicTypeModel model = entry.getValue();
            CreationToolEntry toolEntry =
                            new CreationToolEntry(model.getName(), model.getComment(),
                                            new CopyFactory(model),
                                            getImage("icons/atomic_16.png"),
                                            getImage("icons/atomic_32.png"));
            addAtomicCreationToolEntry(toolEntry);
            AtomicTypeModel.addToolEntry(entry.getKey(), this, toolEntry);
        }
    }

    private void initCompoundCreationEntry() {
        for (Map.Entry<String, CompoundTypeModel> entry : CompoundTypeModel.getTypeEntries()) {
            CompoundTypeModel model = entry.getValue();
            CreationToolEntry toolEntry =
                            new CreationToolEntry(model.getName(), model.getComment(),
                                            new CopyFactory(model),
                                            getImage("icons/compound_16.png"),
                                            getImage("icons/compound_32.png"));
            addCompoundCreationToolEntry(toolEntry);
            CompoundTypeModel.addToolEntry(entry.getKey(), this, toolEntry);
        }
    }

    public void removeConnectorCreationToolEntry(CreationToolEntry entry) {
        connectorPalette.remove(entry);
    }

    public void addConnectorCreationToolEntry(CreationToolEntry entry) {
        connectorPalette.add(entry);
    }

    public void removeAtomicCreationToolEntry(CreationToolEntry entry) {
        atomicPalette.remove(entry);
    }

    public void addAtomicCreationToolEntry(CreationToolEntry entry) {
        atomicPalette.add(entry);
    }

    public void removeCompoundCreationToolEntry(CreationToolEntry entry) {
        compoundPalette.remove(entry);
    }

    public void addCompoundCreationToolEntry(CreationToolEntry entry) {
        compoundPalette.add(entry);
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

        action = new PasteComponentAction(this);
        registry.registerAction(action);
        getSelectionActions().add(action.getId());

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
    }

    public void dispose() {
        removeViewerEditEntry(viewer);
        super.dispose();
    }

    /** Save properties to model */
    protected void saveProperties() {}

    /** Load properties */
    protected void loadProperties() {}
}
