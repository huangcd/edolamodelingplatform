package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ComponentEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;

public class FrameContainer extends Panel {
    private ComponentEditPart owner;
    private int               radius;

    public FrameContainer(ComponentEditPart owner, int radius) {
        this.owner = owner;
        setOpaque(false);
        setLayoutManager(new FreeformLayout());
        this.radius = radius;
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        graphics.setAlpha(20);
        if (owner == null) {
            return;
        }
        if (owner instanceof AtomicEditPart) {
            Rectangle constraint = owner.getModel().getPositionConstraint();
            graphics.setForegroundColor(ColorConstants.black);
            graphics.setBackgroundColor(ColorConstants.green);
            Rectangle rect =
                            new Rectangle(constraint.x + radius, constraint.y + radius,
                                            constraint.width - (2 * radius), constraint.height
                                                            - (2 * radius));
            graphics.fillRectangle(rect);
            graphics.drawRectangle(rect);
        } else if (owner instanceof CompoundEditPart) {
            Rectangle constraint = owner.getModel().getPositionConstraint();
            graphics.setForegroundColor(ColorConstants.black);
            graphics.setBackgroundColor(ColorConstants.blue);
            Rectangle rect =
                            new Rectangle(constraint.x + radius, constraint.y + radius,
                                            constraint.width - (2 * radius), constraint.height
                                                            - (2 * radius));
            graphics.setLineWidth(3);
            graphics.fillRectangle(rect);
            graphics.drawRectangle(rect);
        }
    }

    @Override
    protected boolean useLocalCoordinates() {
        return true;
    }

    public ComponentEditPart getOwner() {
        return owner;
    }
}
