package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

public abstract class BaseEditableEditPart extends BaseGraphicalEditPart {

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

    @Override
    public abstract void propertyChange(PropertyChangeEvent evt);

    /**
     * ���汾��֧��ֱ�ӱ༭
     */
    protected void performDirectEdit() {}

    // ˫���Ի���
    protected abstract void performDoubleClick();
}
