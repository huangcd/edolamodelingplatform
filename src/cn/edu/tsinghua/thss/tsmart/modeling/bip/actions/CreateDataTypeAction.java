package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.CreateDataTypeCommand;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

@SuppressWarnings("rawtypes")
public class CreateDataTypeAction extends SelectionAction {
    public final static String id = "createDataTypeAction";

    public CreateDataTypeAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("增加新数据类型");
        setToolTipText("增加一个新的数据类型");
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        return GlobalProperties.getInstance().isMultipleDataTypeAvailble();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        super.run();
        CreateDataTypeCommand command = new CreateDataTypeCommand();
        execute(command);
    }
}
