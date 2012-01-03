package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.BaseGraphicalEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;

@SuppressWarnings("rawtypes")
public class CopyComponentAction extends SelectionAction {
    public final static String id = CopyComponentAction.class.getCanonicalName();

    public CopyComponentAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("复制");
        setToolTipText("复制当前构件");
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
        // 只对最后一个被选择的对象进行处理，判断是否可以复制
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
        Object obj = list.get(list.size() - 1);
        if (obj instanceof AtomicTypeEditPart || obj instanceof CompoundTypeEditPart
                        || obj instanceof AtomicEditPart || obj instanceof CompoundEditPart) {
            BIPEditor.setCopyObject((IModel) ((BaseGraphicalEditPart) obj).getModel());
        }
    }
}
