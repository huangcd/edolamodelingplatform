package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.ComponentChildrenEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteModelEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.FrameContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class ComponentEditPart extends BaseEditableEditPart {
    private Label typeLabel;
    private Label instanceLabel;
    private Panel panel;

    public void setModel(Object model) {
        List<PortModel> exportPorts =
                        ((ComponentTypeModel) ((ComponentModel) model).getType()).getExportPorts();
        List<BulletModel> bullets = new ArrayList<BulletModel>();
        boolean allNullRect = true;
        for (PortModel port : exportPorts) {
            BulletModel bullet = port.getBullet();
            Rectangle rect = bullet.getPositionConstraint();
            if (rect == null) {
                // 初始化Bullet的位置
                rect = new Rectangle();
                bullet.setPositionConstraint(rect);
            } else if (!rect.getLocation().equals(0, 0)) {
                allNullRect = false;
            }
            bullets.add(bullet);
        }
        if (allNullRect && bullets.size() > 1) {
            ((ComponentModel) model).reorderBullets();
        }
        super.setModel(model);
    }

    public ComponentModel getModel() {
        return (ComponentModel) super.getModel();
    }

    @Override
    protected IFigure createFigure() {
        // TODO 增加缩略图的tooltip
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
        typeLabel.setFont(properties.getDefaultEditorBoldFont());

        instanceLabel = new Label(getModel().getName());
        instanceLabel.setOpaque(true);
        instanceLabel.setFont(properties.getDefaultEditorFont());

        if (this instanceof AtomicEditPart) {
            typeLabel.setForegroundColor(properties.getAtomicLabelColor());
            instanceLabel.setForegroundColor(properties.getAtomicLabelColor());
            // getModel().getType().setName("AtomicType");

        } else if (this instanceof CompoundEditPart) {
            typeLabel.setForegroundColor(properties.getCompoundLabelColor());
            instanceLabel.setForegroundColor(properties.getCompoundLabelColor());
        }

        panel.add(typeLabel);
        panel.add(instanceLabel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshVisuals();
        if (IModel.CONSTRAINT.equals(evt.getPropertyName())) {
            centerLabels();
            refreshChildren();
            for (Object child : getChildren()) {
                if (child instanceof EditPart) {
                    ((EditPart) child).refresh();
                }
            }
            ComponentTypeModel type = (ComponentTypeModel) getModel().getType();
            for (PortModel exportPort : (List<PortModel>) type.getExportPorts()) {
                for (ConnectionModel connection : exportPort.getBullet().getTargetConnections()) {
                    connection.firePropertyChange(IModel.TARGET);
                }
            }
        }
        if (IModel.TYPE_NAME.equals(evt.getPropertyName())) {
            typeLabel.setText(getModel().getType().getName());
            centerLabels();
        } else if (IModel.NAME.equals(evt.getPropertyName())) {
            if (getModel().getParent() != null) {
                if (!getModel().getParent().isNewNameAlreadyExistsInParent(getModel(),
                                getModel().getName())) {
                    instanceLabel.setText(getModel().getName());
                    centerLabels();
                } else {
                    MessageUtil.ShowRenameErrorDialog(getModel().getName());
                    getModel().setName(getModel().getOldName());
                }
            } else {
                instanceLabel.setText(getModel().getName());
                centerLabels();
            }

        } else if (IModel.EXPORT_PORT.equals(evt.getPropertyName())) {
            refreshChildren();
        } else {
            refreshChildren();
        }
    }

    @Override
    protected void createEditPolicies() {
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new DeleteModelEditPolicy());
        installEditPolicy(EditPolicy.LAYOUT_ROLE, new ComponentChildrenEditPolicy());
    }

    @Override
    protected void performDoubleClick() {
        ComponentTypeModel container = (ComponentTypeModel) getModel().getType();
        BIPEditor.openBIPEditor(container);
    }

    @Override
    protected List<BulletModel> getModelChildren() {
        List<PortModel> exportPorts = ((ComponentTypeModel) getModel().getType()).getExportPorts();
        List<BulletModel> bullets = new ArrayList<BulletModel>();
        for (PortModel port : exportPorts) {
            BulletModel bullet = port.getBullet();
            Rectangle rect = bullet.getPositionConstraint();
            if (rect == null) {
                // 初始化Bullet的位置
                rect = new Rectangle();
                bullet.setPositionConstraint(rect);
            }
            bullets.add(bullet);
        }
        return bullets;
    }
}
