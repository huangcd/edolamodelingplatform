package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AddLibrarySettingDialog;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class SaveComponentLibraryAction extends SelectionAction {
    public final static String id = SaveComponentLibraryAction.class.getCanonicalName();

    public SaveComponentLibraryAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("保存到库");
        setToolTipText("保存当前组件到库");
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        // project mode下不能另存为库
        if (GlobalProperties.getInstance().getTopModel() instanceof ProjectModel) {
            return false;
        }
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        if (list.isEmpty()) {
            return false;
        }
        Object obj = list.get(list.size() - 1);
        if (obj instanceof AtomicTypeEditPart || obj instanceof CompoundTypeEditPart
                        || obj instanceof AtomicEditPart || obj instanceof CompoundEditPart) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        Object editPart = list.get(list.size() - 1);
        IModel model = (IModel) ((EditPart) editPart).getModel();
        ComponentTypeModel typeModel;
        if (model instanceof ComponentModel) {
            typeModel = (ComponentTypeModel) ((ComponentModel) model).getType();
        } else {
            typeModel = (ComponentTypeModel) model;
        }
        AddLibrarySettingDialog dialog =
                        new AddLibrarySettingDialog(Display.getCurrent().getActiveShell(),
                                        typeModel);
        dialog.setBlockOnOpen(true);
        if (AddLibrarySettingDialog.OK == dialog.open()) {
            GlobalProperties.getInstance().getTopModel().addChild(typeModel);
        }
    }
}
