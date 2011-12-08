package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.FontRegistry;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;


/**
 * @author huangcd (huangcd.thu@gmail.com)
 * @time 2011-6-21 下午03:44:22
 * @project CereusBip
 * @package cereusbip.parts
 * @class ContentsEditPart.java
 */
@SuppressWarnings("rawtypes")
public class AtomicTypeEditPart extends PageContainerEditPart {
    @Override
    // 安装 edit policy
    protected void createEditPolicies() {
        // installEditPolicy(EditPolicy.LAYOUT_ROLE, new AtomicChildEditPolicy());
        // installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new CustomDirectEditPolicy()); // 直接编辑
        // installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteAtomicEditPolicy());
    }

    public AtomicTypeModel getModel() {
        return (AtomicTypeModel) super.getModel();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        return getModel().getChildren();
    }

    @Override
    public void refresh() {
        refreshChildren();
        for (Object obj : getChildren()) {
            if (obj instanceof PlaceEditPart) {
                PlaceEditPart placeEditPart = (PlaceEditPart) obj;
                Rectangle base = placeEditPart.getModel().getPositionConstraint();
                System.out.println(base);
                Label name = new Label(placeEditPart.getModel().getName());
                Dimension size = new Dimension(20, 18);
                int x = base.x + base.width / 2 - size.width / 2;
                int y = base.y - size.width;
                base = new Rectangle(new Point(x, y), size);
                System.out.println(base);
                getFigure().add(name, new Rectangle(new Point(x, y), size));
            }
        }
        super.refresh();
    }

    // 双击编辑
    @Override
    protected void performDoubleClick() {
        // AtomicTypeModel child = (AtomicTypeModel) getModel();
        //
        // Shell shell = Display.getDefault().getActiveShell();
        // if (shell != null) {
        // EditAtomicTypeDialog dialog = new EditAtomicTypeDialog(shell, child,
        // child.getParent());
        // dialog.setBlockOnOpen(true);
        // dialog.open();
        // }
    }
}
