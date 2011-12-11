package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.swt.SWT;

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

    public IFigure getFigure() {
        if (figure == null) {
            setFigure(createFigure());
            ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
            // 反锯齿，连线稍微好看一点点
            if ((getViewer().getControl().getStyle() & SWT.MIRRORED) == 0)
                cLayer.setAntialias(SWT.ON);

            // 设置Router样式
            BendpointConnectionRouter bendpointConnectionRouter = new BendpointConnectionRouter();
            FanRouter fanRouter = new FanRouter();
            fanRouter.setSeparation(20);
            // ManhattanConnectionRouter manhattanRouter = new ManhattanConnectionRouter();
            // fanRouter.setNextRouter(new ShortestPathConnectionRouter(getFigure()));
            fanRouter.setNextRouter(bendpointConnectionRouter);
            cLayer.setConnectionRouter(fanRouter);
        }
        return figure;
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
