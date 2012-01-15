package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project.TopLevelModelEditPart;

@SuppressWarnings("all")
public class AtomicTypeTreeEditPart extends BaseTreeEditPart {

    public AtomicTypeModel getModel() {
        return (AtomicTypeModel) super.getModel();
    }

    protected List<IInstance> getModelChildren() {
        // 为了双击打开编辑页面，不能返回孩子
        if (getParent() instanceof TopLevelModelEditPart) {
            return Collections.EMPTY_LIST;
        }
        return getModel().getChildren();
    }

    protected String getText() {
        return getModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IModel.CHILDREN)) refresh();
        if (evt.getPropertyName().equals(IModel.NAME)) refreshVisuals();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(BIPEditor.getImage("icons/atomic_16.png").createImage());
    }

    public void refreshChildren() {
        List children = getModelChildren();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) instanceof AtomicTypeModel
                                || children.get(i).equals(getModel())) {
                    getModel().removeChild(getModel().getChildren().get(0));
                }
            }
        }
        super.refreshChildren();
    }

    @Override
    protected void createEditPolicies() {}

}
