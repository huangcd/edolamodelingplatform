package cn.edu.tsinghua.thss.tsmart.modeling.bip.commands;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.requests.CopyFactory;

@SuppressWarnings("rawtypes")
public class CreateDataTypeCommand extends Command {
    private final static IInputValidator             typeNameValidate;
    private HashMap<AtomicEditor, CreationToolEntry> map;
    String                                           type;
    DataTypeModel                                    model;

    static {
        typeNameValidate = new IInputValidator() {
            @Override
            public String isValid(String newText) {
                if (DataTypeModel.typeSources.keySet().contains(newText)) {
                    return "数据类型已存在";
                }
                if (newText == null || newText.isEmpty()) {
                    return "不允许空的数据类型";
                }
                return null;
            }
        };
    }

    @Override
    public void execute() {
        InputDialog dialog =
                        new InputDialog(Display.getCurrent().getActiveShell(), "添加数据类型",
                                        "输入新数据类型的名字", "", typeNameValidate);
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
        map = new HashMap<AtomicEditor, CreationToolEntry>();
        model = new DataTypeModel(type);
        for (AtomicEditor editor : BIPEditor.getAtomicEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "新建一个" + type + "变量",
                                            new CopyFactory(model),
                                            BIPEditor.getImage("icons/new_data_16.png"),
                                            BIPEditor.getImage("icons/new_data_32.png"));
            map.put(editor, entry);
            editor.addDataCreationToolEntry(entry);
        }
        DataTypeModel.typeSources.put(type, model);
    }

    @Override
    public void undo() {
        if (type == null) {
            return;
        }
        for (Map.Entry<AtomicEditor, CreationToolEntry> entry : map.entrySet()) {
            entry.getKey().removeDataCreationToolEntry(entry.getValue());
        }
        DataTypeModel.typeSources.remove(type);
        map.clear();
    }
}
