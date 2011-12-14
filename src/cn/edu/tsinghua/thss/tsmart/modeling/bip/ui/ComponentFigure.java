package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Panel;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.BaseGraphicalEditPart;

/**
 * @author Huangcd (huangcd.thu@gmail.com)
 * @time 2011-7-8 下午11:25:15
 * @project CereusBip
 * @package cereusbip.ui
 * @class ComponentFigure.java
 */
public class ComponentFigure extends Panel {
    private BaseGraphicalEditPart owner;

    public ComponentFigure(BaseGraphicalEditPart owner) {
        this.owner = owner;
        setLayoutManager(new FreeformLayout());
        setOpaque(false);
    }

    @Override
    // COMMENT 相对位置
    protected boolean useLocalCoordinates() {
        return true;
    }

    public BaseGraphicalEditPart getOwner() {
        return owner;
    }

    public void setOwner(BaseGraphicalEditPart owner) {
        this.owner = owner;
    }
}
