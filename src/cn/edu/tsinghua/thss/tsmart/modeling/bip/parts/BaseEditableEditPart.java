package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public abstract class BaseEditableEditPart extends BaseGraphicalEditPart {
    // private CustomDirectEditManager directManager = null; // �༭�ı�

    // ���� �����ڴ��������request����ֱ�ӱ༭�ı�
    public void performRequest(Request req) {
        // ��� Request�� REQ_DIRECT_EDIT����ִ��ֱ�ӱ༭���Եĸ������� performDirectEdit
        if (req.getType().equals(RequestConstants.REQ_DIRECT_EDIT)) {
            performDirectEdit();
        }
        // ����˫���¼�
        else if (req.getType().equals(RequestConstants.REQ_OPEN)) {
            performDoubleClick();
        } else {
            super.performRequest(req);
        }
    }

    protected void performDirectEdit() {
        // if (directManager == null) {
        // // �����û�� directManager���򴴽�һ���������� Text��λ����ͼ�ξ���
        // directManager =
        // new CustomDirectEditManager(this, TextCellEditor.class,
        // new CustomCellEditorLocator(getFigure()));
        // }
        // directManager.show(); // ��ʾ��� directManager
    }

    // ˫���Ի���
    protected abstract void performDoubleClick();
}
