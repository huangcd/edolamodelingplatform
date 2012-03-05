package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.AddDiamondCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BaseInstanceModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ConnectionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DiamondModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.InvisibleBulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.ConnectionEditPart;

@SuppressWarnings("rawtypes")
public class AddBulletAction extends SelectionAction {
    public final static String id = AddBulletAction.class.getCanonicalName();

    public AddBulletAction(IWorkbenchPart part) {
        super(part);
    }

    @Override
    protected void init() {
        super.init();
        setText("增加中间点");
        setToolTipText("增加连线中间点");
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        if (list.isEmpty()) {
            return false;
        }
        // 只对最后一个被选择的对象进行处理
        Object obj = list.get(list.size() - 1);
        if (!(obj instanceof ConnectionEditPart)) {
            return false;
        }
        return true;
    }

    @Override
    public void run() {
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        List list = selection.toList();
        if (list.isEmpty()) {
            return;
        }
        Object obj = list.get(list.size() - 1);
        if (!(obj instanceof ConnectionEditPart)) {
            return;
        }
        ConnectionEditPart editPart = (ConnectionEditPart) obj;
        ConnectionModel connection = editPart.getModel();
        BaseInstanceModel source = connection.getSource();
        BaseInstanceModel target = connection.getTarget();
        Point sourceCenter = source.getPositionConstraint().getCenter();
        Point targetCenter = target.getPositionConstraint().getCenter();
        // 如果是BulletModel，需要加上Component的位置偏移
        if (target instanceof BulletModel && !(target instanceof InvisibleBulletModel)) {
            BulletModel bullet = (BulletModel) target;
            targetCenter.translate(bullet.getPort().getParent().getInstance()
                            .getPositionConstraint().getLocation());
        }
        int x = (sourceCenter.x + targetCenter.x) >> 1;
        int y = (sourceCenter.y + targetCenter.y) >> 1;
        int size = IModel.DIAMOND_SIZE;
        Rectangle rect = new Rectangle(x, y, size, size);
        DiamondModel diamond = new DiamondModel().setPositionConstraint(rect);
        AddDiamondCommand command = new AddDiamondCommand();
        command.setOldConnection(connection);
        command.setParent((CompoundTypeModel) source.getParent());
        command.setSource(source);
        command.setTarget(target);
        command.setDiamond(diamond);
        getCommandStack().execute(command);
        // getCommandStack().execute(command);
        // TODO 增加的InternalBulletModel的具体操作在这里
    }
}
