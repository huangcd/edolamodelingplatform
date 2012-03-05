package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ComponentModel<Model extends BaseInstanceModel, Type extends ComponentTypeModel>
                extends BaseInstanceModel<Model, Type, CompoundTypeModel> {

    private static final long serialVersionUID = -5814929402789410662L;

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return String.format("component %s %s", getType().getName(), getName());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "构件名");
        name.setDescription("01");
        properties.add(name);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "标签");
        tag.setDescription("02");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        } else if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || ENTITY.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>) value);
        }
    }

    @Override
    public Model setPositionConstraint(Rectangle positionConstraint) {
        Rectangle rect = positionConstraint.getCopy().setSize(COMPONENT_SIZE);
        return super.setPositionConstraint(rect);
    }

    public void reorderBullets() {
        List<PortModel> exportPorts = ((ComponentTypeModel) getType()).getExportPorts();
        List<BulletModel> bullets = new ArrayList<BulletModel>();
        for (PortModel port : exportPorts) {
            bullets.add(port.getBullet());
        }
        int length = bullets.size();
        Dimension size = getPositionConstraint().getSize();
        int width = size.width;
        int height = size.height;
        int half = Math.max(0, (length - 4) / 2);
        Iterator<BulletModel> iter = bullets.iterator();
        Point point = new Point();
        final Dimension EMPTY_SIZE = new Dimension(0, 0);
        if (iter.hasNext()) {
            point.setLocation(20, 0);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
        if (iter.hasNext()) {
            point.setLocation(width - 20 - BULLET_RADIUS * 2, 0);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
        if (iter.hasNext()) {
            point.setLocation(20, height);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
        if (iter.hasNext()) {
            point.setLocation(width - 20 - BULLET_RADIUS * 2, height);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
        int delta = height / (half + 1);
        for (int i = 0, h = delta - BULLET_RADIUS; i < half; i++, h += delta) {
            point.setLocation(0, h);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
        int left = Math.max(0, length - 4 - half);
        delta = height / (left + 1);
        for (int i = 0, h = delta - BULLET_RADIUS; i < left; i++, h += delta) {
            point.setLocation(width, h);
            BulletModel bullet = iter.next();
            bullet.setDirection(ensureInFrame(point));
            bullet.setPositionConstraint(new Rectangle(point, EMPTY_SIZE));
        }
    }

    /**
     * 确保bullet在方框之上
     * 
     * @param point bullet被拖动到的位置，point在函数里面会被修改
     * @param componentBound component的范围
     * @return PositionConstants里面的方向，有NORTH、SOUTH、EAST、WEST返回值
     */
    public int ensureInFrame(Point point) {
        int x = point.x;
        int y = point.y;
        int direction = PositionConstants.NORTH;
        int width = COMPONENT_SIZE.width - 2 * IModel.BULLET_RADIUS;
        int height = COMPONENT_SIZE.height - 2 * IModel.BULLET_RADIUS;
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x > width) x = width;
        if (y > height) y = height;
        boolean xPosition = x < width / 2;
        boolean yPosition = y < height / 2;
        int xValue = xPosition ? x : width - x;
        int yValue = yPosition ? y : height - y;
        if (xValue < yValue) {
            x = xPosition ? 0 : width;
            direction = xPosition ? PositionConstants.WEST : PositionConstants.EAST;
        } else {
            y = yPosition ? 0 : height;
            direction = yPosition ? PositionConstants.NORTH : PositionConstants.SOUTH;
        }
        point.setLocation(x, y);
        return direction;
    }
}
