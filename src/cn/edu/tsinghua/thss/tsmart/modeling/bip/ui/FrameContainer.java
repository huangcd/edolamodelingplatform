package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
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
        if (owner == null) {
            return;
        }
        if (owner instanceof AtomicEditPart || owner instanceof CompoundEditPart) {
            Rectangle rect = owner.getModel().getPositionConstraint();
            graphics.setForegroundColor(ColorConstants.black);
            graphics.drawRectangle(rect.x + radius, rect.y + radius, rect.width - (2 * radius),
                            rect.height - (2 * radius));
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
