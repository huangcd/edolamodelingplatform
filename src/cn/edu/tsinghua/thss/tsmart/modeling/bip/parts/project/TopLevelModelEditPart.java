package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;

@SuppressWarnings("all")
public class TopLevelModelEditPart extends AbstractTreeEditPart implements PropertyChangeListener {
    @Override
    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }

    public TopLevelModel getModel() {
        return (TopLevelModel) super.getModel();
    }

    protected String getText() {
        return ((TopLevelModel) getModel()).getName();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
    }

    @Override
    public void refresh() {
        if (getViewer() == null) {
            return;
        }
        try {
            refreshVisuals();
            refreshChildren();
        } catch (Exception ex) {
        }
    }

    @Override
    protected List<Object> getModelChildren() {
        if (getModel() == null) {
            return Collections.EMPTY_LIST;
        }
        List<Object> list = new ArrayList<Object>();
        list.add(getModel().getBaseline());
        if (getModel() instanceof ProjectModel) {
            list.add(((ProjectModel) getModel()).getUsedLibraryEntries());
        }
        for (Object type : getModel().getChildren()) {
            if (type instanceof ComponentTypeModel) {
                list.add(type);
            }
        }
        return list;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshChildren();
    }
}
