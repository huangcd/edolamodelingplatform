package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.CompoundChildrenEditPolicy;

@SuppressWarnings("rawtypes")
public class CompoundTypeEditPart extends PageContainerEditPart {
    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new CompoundChildrenEditPolicy());
    }

    public CompoundTypeModel getModel() {
        return (CompoundTypeModel) super.getModel();
    }

    @Override
    protected List<IInstance> getModelChildren() {
        List<IInstance> children = getModel().getChildren();
        List<IInstance> result = new ArrayList<IInstance>();
        for (IInstance instance : children) {
            if (instance instanceof PriorityModel) {
                continue;
            }
            result.add(instance);
        }
        return result;
    }

    public IFigure getFigure() {
        if (figure == null) {
            setFigure(createFigure());
            ConnectionLayer cLayer = (ConnectionLayer) getLayer(LayerConstants.CONNECTION_LAYER);
            // 反锯齿，连线稍微好看一点
            if ((getViewer().getControl().getStyle() & SWT.MIRRORED) == 0)
                cLayer.setAntialias(SWT.ON);

            // 设置Router样式
            cLayer.setConnectionRouter(new ManhattanConnectionRouter());
        }
        return figure;
    }

    @Override
    public void refresh() {
        refreshChildren();
        super.refresh();
    }

    // 双击编辑
    @Override
    protected void performDoubleClick() {}
}
