package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

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
		 if (getCastedModel().isInitialPlace()) {
		     setWidgetImage(BIPEditor.getImage("icons/initplace.png").createImage());
		 }
		 else
		setWidgetImage(BIPEditor.getImage("icons/place_16.png").createImage());
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE,
				new DeleteModelEditPolicy());
	}

}
