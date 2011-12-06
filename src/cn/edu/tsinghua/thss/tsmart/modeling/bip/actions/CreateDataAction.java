package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateDataCommand;


public class CreateDataAction extends SelectionAction {
    public CreateDataAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("Add data...");
        setToolTipText("Add a new data");
        setId(CreateDataCommand.CREATE_DATA_COMMAND);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        if (getCreateDataCommand() != null) return true;
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
                                        CreateDataCommand.CREATE_DATA_COMMAND));
        execute(command);
    }

    @SuppressWarnings("rawtypes")
    public Command getCreateDataCommand() {
        Request request = new Request(CreateDataCommand.CREATE_DATA_COMMAND);
        List list = getSelectedObjects();
        if (list == null || list.size() == 0) return null;
        EditPart editPart = (EditPart) getSelectedObjects().get(0);
        Command command = editPart.getCommand(request);
        return command;
    }
}
