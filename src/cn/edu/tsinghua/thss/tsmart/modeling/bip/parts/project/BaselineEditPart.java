package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class BaselineEditPart extends AbstractTreeEditPart {
    protected String getText() {
        return Messages.BaselineEditPart_0 + (String) getModel();
    }

    @Override
    public void refresh() {
        refreshVisuals();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(Activator.getImageDescriptor("icons/workset.gif").createImage()); //$NON-NLS-1$
    }
}
