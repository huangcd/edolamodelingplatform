package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
@SuppressWarnings("rawtypes")
public class OpenLibraryAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = OpenLibraryAction.class.getCanonicalName();

    public OpenLibraryAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("构件库");
        setToolTipText("打开Edola构件库");
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    @Override
    public void run() {
        Shell shell = Display.getCurrent().getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[] {LibraryModel.FILE_NAME});
        try {
            dialog.setFilterPath(Activator.getPreferenceDirection().getCanonicalPath());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String path = dialog.open();
        if (path == null) {
            return;
        }
        // 关闭当前所有页面
        BIPEditor.closeAllEditor();
        try {
            LibraryModel lib = LibraryModel.load(new File(path).getParentFile());
            GlobalProperties.getInstance().setTopModel(lib);
            lib.loadTypes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO 打开构件库
    }
}
