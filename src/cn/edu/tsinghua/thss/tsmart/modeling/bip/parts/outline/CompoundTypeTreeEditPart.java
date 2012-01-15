package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.project.TopLevelModelEditPart;

@SuppressWarnings("all")
public class CompoundTypeTreeEditPart extends BaseTreeEditPart {

    public CompoundTypeModel getModel() {
        return (CompoundTypeModel) super.getModel();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        // 为了双击打开编辑页面，不能返回孩子
        if (getParent() instanceof TopLevelModelEditPart) {
            return Collections.EMPTY_LIST;
        }
        List<IInstance> modelChildren = new ArrayList<IInstance>();
        for (IInstance instance : getModel().getChildren()) {
            if (instance instanceof InvisibleBulletModel || instance instanceof PriorityModel) {
                continue;
            }
            modelChildren.add(instance);
        }
        return modelChildren;
    }

    protected String getText() {
        return getModel().getName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(IModel.NAME))
            refreshVisuals();
        else if (evt.getPropertyName().equals(IModel.CHILDREN)) refresh();
    }

    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    public void refreshVisuals() {
        setWidgetText(getText());
        setWidgetImage(BIPEditor.getImage("icons/compound_16.png").createImage());
    }
}
