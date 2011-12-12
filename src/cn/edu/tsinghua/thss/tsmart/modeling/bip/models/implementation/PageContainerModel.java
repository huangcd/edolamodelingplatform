package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

/**
 * Ò³ÃæÈÝÆ÷
 * 
 * @author Huangcd
 */
@SuppressWarnings("rawtypes")
public class PageContainerModel extends BaseTypeModel<PageContainerModel, IInstance, IContainer>
                implements
                    IContainer<PageContainerModel, IContainer, IModel> {
    private List<IModel>          children  = new ArrayList<IModel>();

    public PageContainerModel addChild(IModel model) {
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

    public Object getPropertyValue(Object id) {
        return null;
    }

    public boolean isPropertySet(Object id) {
        return false;
    }

    public void resetPropertyValue(Object id) {}

    public void setPropertyValue(Object id, Object value) {}

    @Override
    public IContainer getParent() {
        return null;
    }

    @Override
    public PageContainerModel setParent(IContainer parent) {
        return this;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public PageContainerModel setName(String newName) {
        return this;
    }

    @Override
    public String exportToString() {
        return "";
    }

    @Override
    public byte[] exportToBytes() throws IOException {
        return new byte[0];
    }

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
    public PageContainerModel copy() {
        return new PageContainerModel();
    }

    @Override
    public boolean isNewNameAlreadyExistsInParent(IModel child, String newName) {
        for (IModel instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }
}
