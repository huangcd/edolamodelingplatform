package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.AlignDataAndPortCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.DataEditPart;

@SuppressWarnings("rawtypes")
public class AlignDataAndTypeAction extends SelectionAction {
    public final static String id = AlignDataAndTypeAction.class.getCanonicalName();

    public AlignDataAndTypeAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText(Messages.AlignDataAndTypeAction_0);
        setToolTipText(Messages.AlignDataAndTypeAction_1);
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        AlignDataAndPortCommand command = new AlignDataAndPortCommand();
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        EditPart part = (EditPart) list.get(list.size() - 1);
        if (part instanceof DataEditPart) {
            DataEditPart editPart = (DataEditPart) part;
            command.setReference(editPart.getModel());
            command.setParent((AtomicTypeModel) editPart.getModel().getParent());
            command.execute();
        } else if (part instanceof AtomicTypeEditPart) {
            AtomicTypeEditPart editPart = (AtomicTypeEditPart) part;
            command.setParent(editPart.getModel());
            command.execute();
        }
    }
}
