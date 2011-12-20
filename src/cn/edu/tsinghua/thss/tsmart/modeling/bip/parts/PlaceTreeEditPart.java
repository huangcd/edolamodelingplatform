package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PlaceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;



public class PlaceTreeEditPart extends BaseTreeEditPart {

	private PlaceModel getCastedModel() {
		return (PlaceModel) getModel();
	}


	protected String getText() {
		if(getCastedModel().getName()==null)
			return "";
		return getCastedModel().getName();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
			refreshVisuals();
	}

	@Override
	public void refresh() {
		refreshVisuals();
		//refreshChildren();
	}

	public void refreshVisuals() {
		setWidgetText(getText());
		setWidgetImage(BIPEditor.getImage("icons/place_16.png").createImage());
	}

	@Override
	// °²×°edit policy
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteModelEditPolicy());
	}

}
