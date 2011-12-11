package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public abstract class BaseEditableEditPart extends BaseGraphicalEditPart {
    // private CustomDirectEditManager directManager = null; // 编辑文本

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

    protected void performDirectEdit() {
        // if (directManager == null) {
        // // 如果还没有 directManager，则创建一个：类型是 Text，位置由图形决定
        // directManager =
        // new CustomDirectEditManager(this, TextCellEditor.class,
        // new CustomCellEditorLocator(getFigure()));
        // }
        // directManager.show(); // 显示这个 directManager
    }

    // 双击对话框
    protected abstract void performDoubleClick();
}
