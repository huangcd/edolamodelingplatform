package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class LibraryEditPart extends AbstractTreeEditPart {

    protected String getText() {
        return (String) getModel();
    }

    @Override
    public void refresh() {
        refreshVisuals();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(Activator.getImageDescriptor("icons/library_16.png").createImage());
    }
}
