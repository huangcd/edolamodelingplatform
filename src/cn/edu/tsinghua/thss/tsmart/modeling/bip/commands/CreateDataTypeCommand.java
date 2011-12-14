package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;

@SuppressWarnings("rawtypes")
public class CreateDataTypeCommand extends Command {
    private final static IInputValidator typeNameValidate;
    String                               type;
    DataTypeModel                        model;

    static {
        typeNameValidate = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                if (DataTypeModel.getTypes().contains(newText)) {
                    return "���������Ѵ���";
                }
                if (newText == null || newText.isEmpty()) {
                    return "������յ���������";
                }
                return null;
            }
        };
    }

    @Override
    public void execute() {
        InputDialog dialog =
                        new InputDialog(Display.getCurrent().getActiveShell(), "�����������",
                                        "�������������͵�����", "", typeNameValidate);
        dialog.setBlockOnOpen(true);
        int result = dialog.open();
        if (result == IDialogConstants.OK_ID) {
            type = dialog.getValue();
            System.out.println("add new data type:" + type);
            redo();
        }
    }

    @Override
    public void redo() {
        if (type == null) {
            return;
        }
        DataTypeModel.addType(type);
    }

    @Override
    public void undo() {
        if (type == null) {
            return;
        }
        DataTypeModel.removeType(type);
    }
}
