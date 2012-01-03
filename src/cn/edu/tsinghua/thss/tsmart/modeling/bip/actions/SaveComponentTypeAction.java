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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;

@SuppressWarnings("rawtypes")
public class SaveComponentTypeAction extends SelectionAction {
    public final static String id = SaveComponentTypeAction.class.getCanonicalName();

    public SaveComponentTypeAction(IWorkbenchPart part) {
        super(part);
        setLazyEnablementCalculation(false);
    }

    @Override
    protected void init() {
        super.init();
        setText("保存构件");
        setToolTipText("将当前构件保存到文件");
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
        if (!(part instanceof BIPEditor)) {
            return;
        }
        BIPEditor editor = (BIPEditor) part;
        IModel model = editor.getModel();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        if (model instanceof AtomicTypeModel) {
            dialog.setFilterExtensions(new String[] {"*.edolaa"});
        } else if (model instanceof CompoundTypeModel) {
            dialog.setFilterExtensions(new String[] {"*.edolam"});
        } else {
            return;
        }
        String path = dialog.open();
        if (path == null) {
            return;
        }
        File file = new File(path);
        if (file.exists()) {
            MessageDialog overwriteDialog =
                            new MessageDialog(shell, "文件已存在", Display.getCurrent().getSystemImage(
                                            SWT.ICON_WARNING), "文件已存在，是否覆盖?",
                                            MessageDialog.WARNING, new String[] {
                                                            IDialogConstants.YES_LABEL,
                                                            IDialogConstants.NO_LABEL}, 1);
            if (overwriteDialog.open() != Window.OK) {
                return;
            }
        }
        try {
            byte[] bytes = model.exportToBytes();
            FileOutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
