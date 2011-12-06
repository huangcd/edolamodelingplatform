package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;


public class DeleteConnectorPortCommand extends Command {
    private ConnectorPortModel connectorPort;

    public void setConnectorPort(ConnectorPortModel connectorPortModel) {
        this.connectorPort = connectorPortModel;
    }

    @Override
    public void execute() {
        super.execute();
        connectorPort.detachSource();
        connectorPort.detachTarget();
    }

    @Override
    public void undo() {
        connectorPort.attachSource();
        connectorPort.attachTarget();
        super.undo();
    }
}
