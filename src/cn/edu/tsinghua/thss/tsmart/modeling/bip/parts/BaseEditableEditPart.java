package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public abstract class BaseEditableEditPart extends BaseGraphicalEditPart {

    // 重载 ，用于处理特殊的request，如直接编辑文本
    public void performRequest(Request req) {
        // 如果 Request是 REQ_DIRECT_EDIT，则执行直接编辑属性的辅助函数 performDirectEdit
        if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
            performDirectEdit();
        }
        // 处理双击事件
        else if (req.getType().equals(RequestConstants.REQ_OPEN)) {
            performDoubleClick();
        } else {
            super.performRequest(req);
        }
    }

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    /**
     * 本版本不支持直接编辑
     */
    protected void performDirectEdit() {}

    // 双击对话框
    protected abstract void performDoubleClick();
}
