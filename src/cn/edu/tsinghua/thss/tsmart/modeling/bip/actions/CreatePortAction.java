package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreatePortCommand;


public class CreatePortAction extends SelectionAction {
    public CreatePortAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("Add port...");
        setToolTipText("Add a new port");
        setId(CreatePortCommand.CREATE_PORT_COMMAND);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        if (getCreatePortCommand() != null) return true;
        return false;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        super.run();
        List list = getSelectedObjects();
        if (list == null || list.size() == 0) return;
        Command command =
                        ((EditPart) getSelectedObjects().get(0)).getCommand(new Request(
                                        CreatePortCommand.CREATE_PORT_COMMAND));
        execute(command);
    }

    @SuppressWarnings("rawtypes")
    public Command getCreatePortCommand() {
        Request request = new Request(CreatePortCommand.CREATE_PORT_COMMAND);
        List list = getSelectedObjects();
        if (list == null || list.size() == 0) return null;
        EditPart editPart = (EditPart) getSelectedObjects().get(0);
        Command command = editPart.getCommand(request);
        return command;
    }
}
