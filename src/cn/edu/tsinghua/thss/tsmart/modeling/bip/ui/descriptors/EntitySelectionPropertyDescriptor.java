package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.*;

public class EntitySelectionPropertyDescriptor extends PropertyDescriptor {

	/**
	 * Creates an property descriptor with the given id and display name.
	 * 
	 * @param id the id of the property
	 * @param displayName the name to display for the property
	 */
	public EntitySelectionPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new EntitySelectionDialogCellEditor(parent);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		return editor;
	}

}
