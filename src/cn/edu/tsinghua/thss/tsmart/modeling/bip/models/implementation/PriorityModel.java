package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-29<br/>
 * Time: ÏÂÎç9:34<br/>
 */
@SuppressWarnings({"unused", "unchecked"})
@Root
public class PriorityModel<Parent extends IContainer, Port extends IPort>
    extends BaseInstanceModel<PriorityModel, PriorityTypeModel, Parent> {

    @Element(name = "left")
    private Port leftPort;
    @Element(name = "right")
    private Port rightPort;
    @Element(name = "guard")
    private GuardModel condition = new GuardModel().setGuard("true");

    public PriorityModel<Parent, Port> copy(Port leftPort, Port rightPort) {
        PriorityModel<Parent, Port> model = this.copy();
        model.leftPort = leftPort;
        model.rightPort = rightPort;
        return model;
    }

    @Override
    public String exportToBip() {
        if (condition.getGuard().equals("true")) {
            return String.format("priority %s %s < %s",
                                 getName(),
                                 getLeftPort().getName(),
                                 getRightPort().getName());
        } else {
            return String.format("priority %s %s -> %s < %s",
                                 getName(),
                                 getCondition().getGuard(),
                                 getLeftPort().getName(),
                                 getRightPort().getName());
        }
    }

    public Port getLeftPort() {
        return leftPort;
    }

    public void setLeftPort(Port leftPort) {
        this.leftPort = leftPort;
    }

    public Port getRightPort() {
        return rightPort;
    }

    public void setRightPort(Port rightPort) {
        this.rightPort = rightPort;
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
}
