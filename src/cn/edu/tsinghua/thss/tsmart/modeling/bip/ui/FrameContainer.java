package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ComponentEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundEditPart;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

public class FrameContainer extends Panel {
    private final static GlobalProperties properties = GlobalProperties.getInstance();
    private ComponentEditPart             owner;
    private Label                         typeNameLabel;
    private Label                         nameLabel;

    public FrameContainer(String name, String typeName, ComponentEditPart owner) {
        this.owner = owner;
        setLayoutManager(new FreeformLayout());
        setOpaque(false);
        this.typeNameLabel = new Label(name);
        this.typeNameLabel.setOpaque(true);
        this.typeNameLabel.setForegroundColor(ColorConstants.darkBlue);
        this.typeNameLabel.setFont(properties.getDefaultEditorFont());
        System.out.println(this.typeNameLabel.getPreferredSize());
        // TODO 将typeName 和 name 两个label放在方框中间
        this.add(this.typeNameLabel);
        int radius = IModel.BULLET_RADIUS;
        System.out.println(owner.getModel().getPositionConstraint());
        this.setConstraint(this.typeNameLabel, new Rectangle(radius, radius, -1, -1));
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        super.paint(graphics);
        if (owner == null) {
            return;
        }
        if (owner instanceof AtomicEditPart || owner instanceof CompoundEditPart) {
            Rectangle rect = owner.getModel().getPositionConstraint();
            graphics.setForegroundColor(ColorConstants.black);
            int radius = IModel.BULLET_RADIUS;
            graphics.drawRectangle(rect.x + radius, rect.y + radius, rect.width - (2 * radius),
                            rect.height - (2 * radius));
        }
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getTypeNameLabel() {
        return typeNameLabel;
    }

    public ComponentEditPart getOwner() {
        return owner;
    }
}
