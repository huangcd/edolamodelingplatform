package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;

@SuppressWarnings("rawtypes")
public class PasteComponentAction extends SelectionAction {
    public final static String id = PasteComponentAction.class.getCanonicalName();

    public PasteComponentAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("粘贴");
        setToolTipText("粘贴复制的组件");
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
        if (obj instanceof CompoundTypeEditPart && BIPEditor.getCopyObject() != null) {
            if (BIPEditor.isCopyObject(((CompoundTypeEditPart) obj).getModel())) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        Object container = list.get(list.size() - 1);
        if (container instanceof CompoundTypeEditPart) {
            CompoundTypeModel compound = ((CompoundTypeEditPart) container).getModel();
            IInstance obj = BIPEditor.getCopyObject();
            obj.setPositionConstraint(new Rectangle(0, 0, 0, 0));
            compound.addChild(obj);
            BIPEditor.setCopyObject(null);
        }
    }
}
