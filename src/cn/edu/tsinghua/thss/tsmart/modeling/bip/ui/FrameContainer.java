package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ComponentEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class FrameContainer extends Panel {
    private ComponentEditPart owner;
    private int               radius;
    private Image             image;

    public FrameContainer(ComponentEditPart owner, int radius) {
        this.owner = owner;
        setOpaque(false);
        setLayoutManager(new FreeformLayout());
        this.radius = radius;
        if (owner instanceof AtomicEditPart) {
            image = Activator.getImageDescriptor("icons/atomic_32.png").createImage();
        } else if (owner instanceof CompoundEditPart) {
            image = Activator.getImageDescriptor("icons/compound_32.png").createImage();
        }
    }

    @Override
    public void paint(Graphics graphics) {
        if (owner == null) {
            return;
        }
        graphics.setForegroundColor(new Color(Display.getDefault(), 119, 147, 60));
        graphics.setLineWidth(3);
        if (owner instanceof AtomicEditPart) {
            Rectangle constraint = owner.getModel().getPositionConstraint();
            Rectangle rect =
                            new Rectangle(constraint.x + radius, constraint.y + radius,
                                            constraint.width - (2 * radius), constraint.height
                                                            - (2 * radius));
            graphics.drawRectangle(rect);
            graphics.drawImage(image, new Point(constraint.x + radius, constraint.y + radius));
        } else if (owner instanceof CompoundEditPart) {
            Rectangle constraint = owner.getModel().getPositionConstraint();
            Rectangle rect =
                            new Rectangle(constraint.x + radius, constraint.y + radius,
                                            constraint.width - (2 * radius), constraint.height
                                                            - (2 * radius));
            graphics.setBackgroundColor(new Color(Display.getDefault(), 202, 250, 202));
            graphics.fillRectangle(rect);
            graphics.drawRectangle(rect);
            graphics.drawImage(image, new Point(constraint.x + radius, constraint.y + radius));
        }
        paintClientArea(graphics);
    }

    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }

    public ComponentEditPart getOwner() {
        return owner;
    }
}
