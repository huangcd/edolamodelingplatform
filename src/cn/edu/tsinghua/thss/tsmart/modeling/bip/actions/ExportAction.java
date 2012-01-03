package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.io.FileWriter;
import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.BIPModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageDialog;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

/**
 * Created by Huangcd
 * 
 * Date: 11-9-14
 * 
 * Time: 下午1:14
 */
public class ExportAction extends SelectionAction {
    public final static String ID = "Export to Edola";

    public ExportAction(IWorkbenchPart part) {
        super(part);
        this.setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        Activator.getDefault().getLog().log(new IStatus() {

            @Override
            public boolean matches(int severityMask) {
                return true;
            }

            @Override
            public boolean isOK() {
                return false;
            }

            @Override
            public boolean isMultiStatus() {
                return false;
            }

            @Override
            public int getSeverity() {
                return IStatus.WARNING;
            }

            @Override
            public String getPlugin() {
                return "EdolaModelingPlatform";
            }

            @Override
            public String getMessage() {
                return "哇哈哈";
            }

            @Override
            public Throwable getException() {
                return new RuntimeException("哈哈哈");
            }

            @Override
            public int getCode() {
                return IStatus.WARNING;
            }

            @Override
            public IStatus[] getChildren() {
                return new IStatus[0];
            }
        });
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            Shell shell = Display.getDefault().getActiveShell();
            FileDialog dialog = new FileDialog(shell, SWT.SAVE);
            dialog.setOverwrite(true);
            dialog.setFilterExtensions(new String[] {"*.bip"});
            String path = dialog.open();
            if (path == null) {
                return;
            }
            CompoundTypeModel model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
            // 如果不通过验证，不能导出模型
            if (!model.validateFull()) {
                return;
            }
            try {
                String bipContext = model.exportAllToBIP();
                FileWriter writer = new FileWriter(path);
                writer.write(bipContext);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BIPModelingException e) {
                MessageDialog.ShowErrorDialog(e.getMessage(), "Error");
            }
        }
    }
}
