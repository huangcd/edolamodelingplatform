package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FanRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.AtomicTypeChildrenEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles.BetterBendpointConnectionRouter;


/**
 * @author huangcd (huangcd.thu@gmail.com)
 */
@SuppressWarnings("rawtypes")
public class AtomicTypeEditPart extends PageContainerEditPart {
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new AtomicTypeChildrenEditPolicy());
    }

    public AtomicTypeModel getModel() {
        return (AtomicTypeModel) super.getModel();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        return getModel().getChildren();
    }

    public IFigure getFigure() {
        if (figure == null) {
            setFigure(createFigure());
            ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
            // 反锯齿，连线稍微好看一点
            if ((getViewer().getControl().getStyle() & SWT.MIRRORED) == 0)
                cLayer.setAntialias(SWT.ON);

            // 设置Router样式
            BetterBendpointConnectionRouter bendpointRouter = new BetterBendpointConnectionRouter();
            FanRouter fanRouter = new FanRouter();
            fanRouter.setSeparation(20);
            fanRouter.setNextRouter(bendpointRouter);
            cLayer.setConnectionRouter(bendpointRouter);
        }
        return figure;
    }

    @Override
    public void refresh() {
        refreshChildren();
        super.refresh();
    }

    @Override
    protected void performDoubleClick() {}
}
