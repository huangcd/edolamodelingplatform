package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import java.beans.PropertyChangeEvent;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

/**
 * 页面容器类
 * 
 * @author Huangcd
 */
public abstract class PageContainerEditPart extends BaseGraphicalEditPart {

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IModel.CHILDREN.equals(evt.getPropertyName())) {
            refresh();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            BIPEditor editor = BIPEditor.getEditorFromViewer(getViewer());
            editor.setEditorTitle(getModel().getName());
        } else {
            refresh();
        }
    }

    protected void refreshVisuals() {}

    @Override
    protected IFigure createFigure() {

        FreeformLayer figure = new FreeformLayer();
        figure.setLayoutManager(new FreeformLayout());
        return figure;
        // return new ComponentFigure(this);
    }

    // 重载 ，用于处理特殊的request，如直接编辑文本
    public void performRequest(Request req) {
        // 处理双击事件
        if (req.getType().equals(RequestConstants.REQ_OPEN)) {
            performDoubleClick();
        } else {
            super.performRequest(req);
        }
    }

    // 双击对话框
    protected abstract void performDoubleClick();
}
