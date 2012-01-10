package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenDialogAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

public class ValidateCodeGenerationAction extends OpenDialogAction {

    public static final String ID = ValidateCodeGenerationAction.class.getCanonicalName();

    public ValidateCodeGenerationAction(IWorkbenchWindow window) {
        super(window, ID, "验证模型是否满足要求", "验证模型是否满足要求", null);
    }

    @Override
    protected Dialog getDialog() {
        return null;
    }

    public void run() {
        // TODO 验证模型是否满足要求

        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            CompoundTypeModel model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
            // 如果不通过验证，不能导出模型

            try {
                if (model.checkCodeGenValid()) {
                    Shell shell = Display.getDefault().getActiveShell();
                    FileDialog dialog = new FileDialog(shell, SWT.SAVE);
                    dialog.setOverwrite(true);
                    dialog.setFilterExtensions(new String[] {"*.bip"});
                    String path = dialog.open();
                    if (path == null) {
                        return;
                    }

                    String bipContext = model.exportAllToBIPforCodeGen();
                    FileWriter writer = new FileWriter(path);
                    writer.write(bipContext);
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (EdolaModelingException e) {
                MessageUtil.ShowErrorDialog(e.getMessage(), "Error");
            }


        }

    }
}
