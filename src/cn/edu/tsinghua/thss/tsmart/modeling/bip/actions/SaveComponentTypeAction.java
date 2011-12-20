package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;

public class SaveComponentTypeAction extends SelectionAction {
    public final static String id = SaveComponentTypeAction.class.getCanonicalName();

    public SaveComponentTypeAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("�������");
        setToolTipText("����ǰ������浽�ļ�");
        setId(id);
        setEnabled(false);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        IWorkbenchPart part = getWorkbenchPart();
        System.out.println(part);
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setFilterExtensions(new String[] {"*.bipm"});
        String path = dialog.open();
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            MessageDialog overwriteDialog =
                            new MessageDialog(shell, "�ļ��Ѵ���", Display.getCurrent().getSystemImage(
                                            SWT.ICON_WARNING), "�ļ��Ѵ��ڣ��Ƿ񸲸�?",
                                            MessageDialog.WARNING, new String[] {
                                                            IDialogConstants.YES_LABEL,
                                                            IDialogConstants.NO_LABEL}, 1);
            if (overwriteDialog.open() != Window.OK) {
                return;
            }
        }
        if (part instanceof BIPEditor) {
            BIPEditor editor = (BIPEditor) part;
            try {
                byte[] bytes = editor.getModel().exportToBytes();
                FileOutputStream out = new FileOutputStream(path);
                out.write(bytes);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
