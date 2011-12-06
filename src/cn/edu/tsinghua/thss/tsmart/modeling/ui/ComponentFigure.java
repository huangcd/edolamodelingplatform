package cn.edu.tsinghua.thss.tsmart.modeling.ui;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Rectangle;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BaseModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.AtomicTypeEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.BaseGraphicalEditPart;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.CompoundTypeEditPart;

/**
 * @author Huangcd (huangcd.thu@gmail.com)
 * @time 2011-7-8 下午11:25:15
 * @project CereusBip
 * @package cereusbip.ui
 * @class ComponentFigure.java
 */
public class ComponentFigure extends Panel {
    private String                name;
    private BaseGraphicalEditPart owner;
    private Label                 title;

    public ComponentFigure(String name, BaseGraphicalEditPart owner) {
        this.name = name;
        this.owner = owner;
        setLayoutManager(new FreeformLayout());
        setOpaque(false);
        title = new Label(name);
        title.setOpaque(true);
        title.setBackgroundColor(ColorConstants.lightGray);

        this.add(title);
        int radius = BaseModel.BULLET_RADIUS;
        this.setConstraint(title, new Rectangle(radius, radius, -1, -1));
    }

    @Override
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (owner == null) {
            return;
        }
        if (owner instanceof AtomicTypeEditPart || owner instanceof CompoundTypeEditPart) {
            Rectangle rect = ((BaseModel) owner.getModel()).getPositionConstraint();
            graphics.setForegroundColor(ColorConstants.black);
            int radius = BaseModel.BULLET_RADIUS;
            graphics.drawRectangle(rect.x + radius, rect.y + radius, rect.width - (2 * radius),
                            rect.height - (2 * radius));
        }
    }

    @Override
    // COMMENT 相对位置
    protected boolean useLocalCoordinates() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.title.setText(name);
    }

    public BaseGraphicalEditPart getOwner() {
        return owner;
    }

    public void setOwner(BaseGraphicalEditPart owner) {
        this.owner = owner;
    }

    public Label getTitle() {
        return title;
    }
}
