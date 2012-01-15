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
        connectorPalette = new PaletteDrawer(Messages.CompoundEditor_0);
        getPaletteRoot().add(connectorPalette);
        initConnectorCreationEntry();

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        if (topModel instanceof ProjectModel) {
            initLibrary();
        } else if (topModel instanceof LibraryModel) {
            ToolEntry entry =
                            new CreationToolEntry(Messages.CompoundEditor_1, Messages.CompoundEditor_2, new SimpleFactory(
                                            AtomicTypeModel.class),
                                            getImage("icons/atomic_16.png"), //$NON-NLS-1$
                                            getImage("icons/atomic_32.png")); //$NON-NLS-1$
            getToolGroup().add(entry);
            entry =
                            new CreationToolEntry(Messages.CompoundEditor_5, Messages.CompoundEditor_6, new SimpleFactory(
                                            CompoundTypeModel.class),
                                            getImage("icons/compound_16.png"), //$NON-NLS-1$
                                            getImage("icons/compound_32.png")); //$NON-NLS-1$
            getToolGroup().add(entry);
            atomicPalette = new PaletteDrawer(Messages.CompoundEditor_9);
            getPaletteRoot().add(atomicPalette);

            compoundPalette = new PaletteDrawer(Messages.CompoundEditor_10);
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
                                new CreationToolEntry(connector.getName(), Messages.CompoundEditor_11
                                                + connector.getName() + Messages.CompoundEditor_12, new CopyFactory(
                                                connector), getImage("icons/connector_16.png"), //$NON-NLS-1$
                                                getImage("icons/connector_32.png")); //$NON-NLS-1$
                addConnectorCreationToolEntry(toolEntry);
            }
            // 项目依赖库
            for (ProjectModel.LibraryEntry entry : project.getUsedLibraryEntries()) {
                PaletteDrawer drawer = new PaletteDrawer(entry.getName());
                for (ComponentTypeModel model : entry.getLibs()) {
                    String subImageName =
                                    (model instanceof AtomicTypeModel) ? "atomic" : "compound"; //$NON-NLS-1$ //$NON-NLS-2$
                    CreationToolEntry toolEntry =
                                    new CreationToolEntry(model.getName(), model.getComment(),
                                                    new CopyFactory(model), getImage("icons/" //$NON-NLS-1$
                                                                    + subImageName + "_16.png"), //$NON-NLS-1$
                                                    getImage("icons/" + subImageName + "_32.png")); //$NON-NLS-1$ //$NON-NLS-2$
                    drawer.add(toolEntry);
                }
                getPaletteRoot().add(drawer);
            }
        }
    }

    private void initConnectorCreationEntry() {
        for (Map.Entry<String, ConnectorTypeModel> entry : ConnectorTypeModel.getTypeEntries()) {
            CreationToolEntry toolEntry =
                            new CreationToolEntry(entry.getKey(), Messages.CompoundEditor_21 + entry.getKey() + Messages.CompoundEditor_22,
                                            new CopyFactory(entry.getValue()),
                                            getImage("icons/connector_16.png"), //$NON-NLS-1$
                                            getImage("icons/connector_32.png")); //$NON-NLS-1$
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
                                            getImage("icons/atomic_16.png"), //$NON-NLS-1$
                                            getImage("icons/atomic_32.png")); //$NON-NLS-1$
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
                                            getImage("icons/compound_16.png"), //$NON-NLS-1$
                                            getImage("icons/compound_32.png")); //$NON-NLS-1$
            addCompoundCreationToolEntry(toolEntry);
            CompoundTypeModel.addToolEntry(entry.getKey(), this, toolEntry);
        }
    }

    public void removeConnectorCreationToolEntry(CreationToolEntry entry) {
        if (connectorPalette == null) {
            return;
        }
        connectorPalette.remove(entry);
    }

    public void addConnectorCreationToolEntry(CreationToolEntry entry) {
        if (connectorPalette == null) {
            return;
        }
        connectorPalette.add(entry);
    }

    public void removeAtomicCreationToolEntry(CreationToolEntry entry) {
        if (atomicPalette == null) {
            return;
        }
        atomicPalette.remove(entry);
    }

    public void addAtomicCreationToolEntry(CreationToolEntry entry) {
        if (atomicPalette == null) {
            return;
        }
        atomicPalette.add(entry);
    }

    public void removeCompoundCreationToolEntry(CreationToolEntry entry) {
        if (compoundPalette == null) {
            return;
        }
        compoundPalette.remove(entry);
    }

    public void addCompoundCreationToolEntry(CreationToolEntry entry) {
        if (compoundPalette == null) {
            return;
        }
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
        // startupModel is not allow to close
        // if (properties.getTopModel() instanceof ProjectModel
        // && getModel().equals(
        // ((ProjectModel) properties.getTopModel()).getStartupModel())) {
        // return;
        // }
        super.dispose();
    }

    /** Save properties to model */
    protected void saveProperties() {}

    /** Load properties */
    protected void loadProperties() {}
}
