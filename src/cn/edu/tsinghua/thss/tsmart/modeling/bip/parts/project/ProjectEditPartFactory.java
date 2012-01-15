package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline.TreeEditPartFactory;

public class ProjectEditPartFactory implements EditPartFactory {
    private final static ProjectEditPartFactory instance    = new ProjectEditPartFactory();
    private final static TreeEditPartFactory    treeFactory = TreeEditPartFactory.getInstance();
    private final static TopLevelModelEditPart  topEditPart = new TopLevelModelEditPart();

    private ProjectEditPartFactory() {}

    public static ProjectEditPartFactory getInstance() {
        return instance;
    }

    public static TopLevelModelEditPart getTopLevelModelEditPart() {
        return topEditPart;
    }

    @Override
    public EditPart createEditPart(EditPart context, Object model) {
        EditPart editPart;
        if (model instanceof ProjectModel) {
            topEditPart.setModel(model);
            return topEditPart;
        }
        if (model instanceof String && context instanceof TopLevelModelEditPart) {
            editPart = new BaselineEditPart();
            editPart.setModel(model);
            return editPart;
        }
        if (model instanceof List) {
            editPart = new LibrariesEditPart();
            editPart.setModel(model);
            return editPart;
        }
        if (model instanceof String && context instanceof LibrariesEditPart) {
            editPart = new LibraryEditPart();
            editPart.setModel(model);
            return editPart;
        }
        if (model instanceof IModel) {
            // 直接调用TreeEditPartFactory来获取EditPart
            return treeFactory.createEditPart(context, model);
        }
        return null;
    }
}
