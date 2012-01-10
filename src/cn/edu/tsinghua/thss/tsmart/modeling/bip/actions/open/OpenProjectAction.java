package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open;

import java.io.File;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
public class OpenProjectAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = OpenProjectAction.class.getCanonicalName();

    public OpenProjectAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("项目");
        setToolTipText("打开Edola项目");
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
        dialog.setFilterExtensions(new String[] {ProjectModel.FILE_NAME});
        String path = dialog.open();
        if (path == null) {
            return;
        }
        // 关闭当前所有页面
        BIPEditor.closeAllEditor();
        try {
            CodeGenProjectModel project = CodeGenProjectModel.load(new File(path).getParentFile());
            GlobalProperties.getInstance().setTopModel(project);
            project.loadTypes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO 打开项目
    }
}
