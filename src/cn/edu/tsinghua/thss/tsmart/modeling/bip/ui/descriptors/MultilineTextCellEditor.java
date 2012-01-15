package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MultilineTextEditDialog;

public class MultilineTextCellEditor extends DialogCellEditor {
    public MultilineTextCellEditor(Composite parent) {
        super(parent);
    }

    @Override
    protected Object openDialogBox(Control cellEditorWindow) {
        Shell shell = Display.getDefault().getActiveShell();
        MultilineTextEditDialog dialog = new MultilineTextEditDialog(shell, (String) getValue());
        dialog.setBlockOnOpen(true);
        dialog.open();
        return dialog.getAction();
    }
}
