package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import java.io.FileWriter;
import java.io.IOException;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;

/**
 * Created by Huangcd
 * 
 * Date: 11-9-14
 * 
 * Time: ����1:14
 */
public class ExportAction extends SelectionAction {
    public final static String ID = Messages.ExportAction_0;

    public ExportAction(IWorkbenchPart part) {
        super(part);
        this.setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        CompoundTypeModel model;
        /*
         * TopLevelModel topModel = GlobalProperties.getInstance().getTopModel(); //
         * ��Ŀģʽ�µ�������startupModel if (topModel instanceof ProjectModel) { model = ((ProjectModel)
         * topModel).getStartupModel(); } // ��ģʽ�µ������ǵ�ǰҳ�����ͼ else {
         */
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        IEditorPart editor = page.getActiveEditor();
        if (editor instanceof CompoundEditor) {
            model = (CompoundTypeModel) ((CompoundEditor) editor).getModel();
        } else {
            return;
        }
        // }
        MessageUtil.clearProblemMessage();

        // �����ͨ����֤�����ܵ���ģ��
        if (!model.validateFull()) {
            //return;
        }
        try {
            Shell shell = Display.getDefault().getActiveShell();
            FileDialog dialog = new FileDialog(shell, SWT.SAVE);
            dialog.setOverwrite(true);
            dialog.setFilterExtensions(new String[] {"*.edola"}); //$NON-NLS-1$
            String path = dialog.open();
            if (path == null) {
                return;
            }

            String bipContext = model.exportAllToBIP();
            FileWriter writer = new FileWriter(path);
            writer.write(bipContext);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EdolaModelingException e) {
            MessageUtil.ShowErrorDialog(e.getMessage(), Messages.ExportAction_2);
        }
    }
}
