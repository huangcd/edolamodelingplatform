package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.BulletModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditConnectorPortDialog;


public class CreateConnectorPortCommand extends Command {
    private CompoundTypeModel  parent;
    private ConnectorTypeModel source;
    private BulletModel        target;
    private ConnectorPortModel connection;

    public ConnectorTypeModel getSource() {
        return source;
    }

    @Override
    public boolean canExecute() {
        return (source != null && target != null);
    }

    @Override
    public void execute() {
        connection.attachSource();
        connection.attachTarget();
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditConnectorPortDialog dialog = new EditConnectorPortDialog(shell, connection);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }

    @Override
    public void redo() {
        connection.attachSource();
        connection.attachTarget();
    }

    @Override
    public void undo() {
        connection.detachSource();
        connection.detachSource();
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
        this.connection.setParent(this.parent);
        this.connection.setName(this.source.getDefaultConnectionPortName());
    }

    public void setSource(ConnectorTypeModel source) {
        this.source = source;
        this.connection.setSource(source);
    }

    public void setTarget(BulletModel target) {
        this.target = target;
        this.connection.setTarget(target);
    }

    public void setConnection(ConnectorPortModel connection) {
        this.connection = connection;
    }

}
