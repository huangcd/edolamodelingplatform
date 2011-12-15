package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;

/**
 * 在Atomic或者Compound的边框中重定位小圆点，使小圆点互不重叠
 * 
 * @author huangcd
 * 
 */
public class BorderLocator implements Locator {

    @Override
    public void relocate(IFigure target) {}

    /**
     * 在Atomic或者Compound的边框中重定位小圆点，使小圆点互不重叠
     * 
     * @param targets
     */
    public void relocate(List<IFigure> targets) {}
}
