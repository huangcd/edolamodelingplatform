package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public class BipModel extends BaseTypeModel<BipModel, IInstance, IContainer>
                implements
                    IContainer<BipModel, IContainer, IModel> {
    private List<IModel>       children = new ArrayList<IModel>();

    public BipModel() {}

    public BipModel addChild(IModel model) {
        children.add(model);
        firePropertyChange(CHILDREN);
        return this;
    }

    public List<IModel> getChildren() {
        return children;
    }

    public boolean removeChild(IModel model) {
        children.remove(model);
        firePropertyChange(CHILDREN);
        return true;
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {}

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public IInstance createInstance() {
        return null;
    }

    @Override
    public BipModel copy() {
        return new BipModel();
    }

}
