package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Text;

public class CustomCellEditorLocator implements CellEditorLocator {

    private IFigure   figure; // （1）Text 控件要处于 Figure 所在的为止
    private Rectangle rect;

    public CustomCellEditorLocator(IFigure f) {
        figure = f; // （2）因此要在构造函数中得到为哪个 Figure 设置 Text 控件
        rect = figure.getBounds().getCopy();
    }

    public void relocate(CellEditor celleditor) {
        Text text = (Text) celleditor.getControl();
        rect = figure.getBounds().getCopy();
        figure.translateToAbsolute(rect);
        text.setBounds(rect.x, rect.y, rect.width, rect.height > 20 ? 20 : rect.height);
    }
}
