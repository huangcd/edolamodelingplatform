package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

public class CustomCellEditorLocator implements CellEditorLocator {

    private IFigure   figure; // ��1��Text �ؼ�Ҫ���� Figure ���ڵ�Ϊֹ
    private Rectangle rect;

    public CustomCellEditorLocator(IFigure f) {
        figure = f; // ��2�����Ҫ�ڹ��캯���еõ�Ϊ�ĸ� Figure ���� Text �ؼ�
        rect = figure.getBounds().getCopy();
    }

    public void relocate(CellEditor celleditor) {
        Text text = (Text) celleditor.getControl();
        rect = figure.getBounds().getCopy();
        figure.translateToAbsolute(rect);
        text.setBounds(rect.x, rect.y, rect.width, rect.height > 20 ? 20 : rect.height);
    }
}
