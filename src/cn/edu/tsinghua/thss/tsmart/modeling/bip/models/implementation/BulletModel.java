package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

/**
 * 小圆点，用于表示export port
 * 
 * @author huangcd
 * 
 */
@SuppressWarnings("rawtypes")
public class BulletModel extends BaseInstanceModel<BulletModel, IType, IContainer> {

    private PortModel port;
    private int       direction;

    public BulletModel(PortModel portModel) {
        port = portModel;
    }

    public PortModel getPort() {
        return port;
    }

    public String getPortName() {
        return port.getName();
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
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
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    @Override
    public BulletModel setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = positionConstraint.getCopy().setSize(BULLET_RADIUS * 2, BULLET_RADIUS * 2);
        return super.setPositionConstraint(rect);
    }
}
