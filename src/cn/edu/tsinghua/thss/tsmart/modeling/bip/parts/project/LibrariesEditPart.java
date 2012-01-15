package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel.LibraryEntry;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

@SuppressWarnings("unchecked")
public class LibrariesEditPart extends AbstractTreeEditPart {

    public List<LibraryEntry> getModel() {
        return (List<LibraryEntry>) super.getModel();
    }

    protected String getText() {
        return Messages.LibrariesEditPart_0;
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(Activator.getImageDescriptor("icons/library_obj.gif").createImage()); //$NON-NLS-1$
    }

    @Override
    protected List<String> getModelChildren() {
        List<String> libraries = new ArrayList<String>();
        for (LibraryEntry lib : getModel()) {
            libraries.add(lib.getName());
        }
        return libraries;
    }
}
