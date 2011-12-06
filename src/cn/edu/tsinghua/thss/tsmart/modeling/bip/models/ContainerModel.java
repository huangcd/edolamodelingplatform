package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import java.util.ArrayList;
import java.util.List;

public abstract class ContainerModel extends BaseModel {
    private List<BaseModel>    children = new ArrayList<BaseModel>();
    public final static String CHILDREN = "ContainerChildren";

    public boolean addChild(BaseModel model) {
        children.add(model);
        firePropertyChange(CHILDREN, null, null);
        return true;
    }

    public List<BaseModel> getChildren() {
        return children;
    }

    public boolean removeChild(BaseModel model) {
        children.remove(model);
        firePropertyChange(CHILDREN, null, null);
        return true;
    }

    public void clearChildren() {
        if (children != null || children.size() > 0) {
            for (int i = 0; i < children.size(); i++) {
                BaseModel model = children.get(i);
                removeChild(model);
            }
            children = null;
            children = new ArrayList<BaseModel>();
        }

    }
}
