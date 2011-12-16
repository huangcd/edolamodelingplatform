package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ComponentChildrenEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.FrameContainer;

@SuppressWarnings({"rawtypes", "unchecked"})
// TODO 安装ChildEditPolicy
public abstract class ComponentEditPart extends BaseEditableEditPart {
    private Label typeLabel;
    private Label instanceLabel;
    private Panel panel;

    public IComponentInstance getModel() {
        return (IComponentInstance) super.getModel();
    }

    @Override
    protected IFigure createFigure() {
        // TODO panel.translateToParent(p): 坐标转换
        panel = new FrameContainer(this, IModel.BULLET_RADIUS);
        panel.setFont(properties.getDefaultEditorFont());
        initLabels();
        centerLabels();
        return panel;
    }

    /** 将typeName 和 name 两个label放在方框中间 */
    private void centerLabels() {
        Rectangle rect = getModel().getPositionConstraint();
        Dimension typeSize = typeLabel.getPreferredSize();
        Dimension instanceSize = instanceLabel.getPreferredSize();
        int width = Math.max(typeSize.width, instanceSize.width);
        int height = typeSize.height + instanceSize.height + 3;
        if (width + 30 > rect.width) {
            getModel().setPositionConstraint(rect.getCopy().setWidth(width + 30));
            rect = getModel().getPositionConstraint();
        }
        int x = (rect.width - typeSize.width) / 2;
        int y = (rect.height - height) / 2;
        typeLabel.setBounds(new Rectangle(new Point(x, y), typeSize));

        x = (rect.width - instanceSize.width) / 2;
        y += instanceSize.height + 3;
        instanceLabel.setBounds(new Rectangle(new Point(x, y), instanceSize));
    }

    private void initLabels() {
        typeLabel = new Label(getModel().getType().getName());
        typeLabel.setOpaque(true);
        typeLabel.setForegroundColor(ColorConstants.darkBlue);
        typeLabel.setFont(properties.getDefaultEditorFont());
        panel.add(typeLabel);

        instanceLabel = new Label(getModel().getName());
        instanceLabel.setOpaque(true);
        instanceLabel.setForegroundColor(ColorConstants.blue);
        instanceLabel.setFont(properties.getDefaultEditorFont());
        panel.add(instanceLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            centerLabels();
        }
        if (AtomicModel.TYPE_NAME.equals(evt.getPropertyName())) {
            typeLabel.setText(getModel().getType().getName());
            centerLabels();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            instanceLabel.setText(getModel().getName());
            centerLabels();
        } else if (PortModel.EXPORT.equals(evt.getPropertyName())) {
            refreshChildren();
        }
        refreshVisuals();
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ComponentChildrenEditPolicy());
    }

    @Override
    protected void performDoubleClick() {
        IComponentType container = (IComponentType) getModel().getType();
        BIPEditor.openBIPEditor(container);
    }

    @Override
    protected List<BulletModel> getModelChildren() {
        List<IPort> exportPorts = ((IComponentType) getModel().getType()).getExportPorts();
        List<BulletModel> bullets = new ArrayList<BulletModel>();
        for (IPort port : exportPorts) {
            BulletModel bullet = port.getBullet();
            Rectangle rect = bullet.getPositionConstraint();
            if (rect == null) {
                rect = new Rectangle();
                bullet.setPositionConstraint(rect);
            }
            bullets.add(bullet);
        }
        return bullets;
    }
}
