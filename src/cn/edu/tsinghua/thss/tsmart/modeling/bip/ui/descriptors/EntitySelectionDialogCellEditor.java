package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.EntitySelectionDialog;


/**
 * @author Administrator
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class EntitySelectionDialogCellEditor extends DialogCellEditor {

	/**
	 * Creates a new Font dialog cell editor parented under the given control.
	 * The cell editor value is <code>null</code> initially, and has no 
	 * validator.
	 *
	 * @param parent the parent control
	 */
	protected EntitySelectionDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
	    Shell shell = Display.getCurrent().getActiveShell();
	    
	    EntitySelectionDialog dialog;
	    if(getValue() instanceof ArrayList<?>)
	        dialog = new EntitySelectionDialog(shell, (ArrayList<String>) getValue());
	    else
	        dialog = new EntitySelectionDialog(shell, new ArrayList<String>());
        dialog.setBlockOnOpen(true);
        if (Dialog.OK == dialog.open()) {
            return dialog.getEntityNames();
        }
        return dialog.getEntityNames();
	}

}
