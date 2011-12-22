package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ExportAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.PasteComponentAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.SaveComponentTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPFileEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;

@SuppressWarnings("rawtypes")
public class CompoundEditor extends BIPEditor {
    public final static String id = CompoundEditor.class.getCanonicalName();
    private GraphicalViewer    viewer;
    private PaletteDrawer      connectorPalette;
    private PaletteDrawer      atomicPalette;
    private PaletteDrawer      compoundPalette;

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
        ToolEntry entry =
                        new CreationToolEntry("原子组件", "新建一个空白的原子组件", new SimpleFactory(
                                        AtomicTypeModel.class), getImage("icons/atomic_16.png"),
                                        getImage("icons/atomic_32.png"));
        getToolGroup().add(entry);
        entry =
                        new CreationToolEntry("复合组件", "新建一个空白的复合组件", new SimpleFactory(
                                        CompoundTypeModel.class),
                                        getImage("icons/compound_16.png"),
                                        getImage("icons/compound_32.png"));
        getToolGroup().add(entry);
        entry =
                        new ConnectionCreationToolEntry("迁移", "新建一个迁移", new SimpleFactory(
                                        ConnectionTypeModel.class),
                                        getImage("icons/transition_16.png"),
                                        getImage("icons/transition_32.png"));
        getToolGroup().add(entry);

        connectorPalette = new PaletteDrawer("连接子");
        getPaletteRoot().add(connectorPalette);

        atomicPalette = new PaletteDrawer("原子组件库");
        getPaletteRoot().add(atomicPalette);

        compoundPalette = new PaletteDrawer("复合组件库");
        getPaletteRoot().add(compoundPalette);
        initConnectorCreationEntry();
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

    public void removeConnectorCreationToolEntry(CreationToolEntry entry) {
        connectorPalette.remove(entry);
    }

    public void addConnectorCreationToolEntry(CreationToolEntry entry) {
        connectorPalette.add(entry);
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
