package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-29<br/>
 * Time: ÏÂÎç9:34<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class PriorityModel<Parent extends IContainer>
                extends BaseInstanceModel<PriorityModel, BaseTypeModel, Parent> {

    private static final long serialVersionUID = -5887302928533148242L;
    @Element(name = "left")
    private IModel            left;
    @Element(name = "right")
    private IModel            right;
    @Element(name = "guard")
    private GuardModel        condition        = new GuardModel().setGuard("true");

    @Override
    public String exportToBip() {
        if (condition.getGuard().equals("true")) {
            return String.format("priority %s %s < %s", getName(), getLeftPort().getName(),
                            getRightPort().getName());
        } else {
            return String.format("priority %s %s -> %s < %s", getName(), getCondition().getGuard(),
                            getLeftPort().getName(), getRightPort().getName());
        }
    }

    public IModel getLeftPort() {
        return left;
    }

    public void setLeftPort(IModel leftPort) {
        this.left = leftPort;
    }

    public IModel getRightPort() {
        return right;
    }

    public void setRightPort(IModel rightPort) {
        this.right = rightPort;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public GuardModel getCondition() {
        return condition;
    }

    public void setCondition(GuardModel condition) {
        this.condition = condition;
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
    public void setPropertyValue(Object id, Object value) {}
}
