package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AddLibrarySettingDialog;

@SuppressWarnings("rawtypes")
public class SaveComponentToPaletteAction extends SelectionAction {
    public final static String id = SaveComponentToPaletteAction.class.getCanonicalName();

    public SaveComponentToPaletteAction(IWorkbenchPart part) {
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
            String name = dialog.getName();
            if (name == null) {
                name = typeModel.getName();
            }
            if (typeModel instanceof AtomicTypeModel) {
                AtomicTypeModel.addType(name, (AtomicTypeModel) typeModel);
            } else if (typeModel instanceof CompoundTypeModel) {
                CompoundTypeModel.addType(name, (CompoundTypeModel) typeModel);
            }
        }
    }
}
