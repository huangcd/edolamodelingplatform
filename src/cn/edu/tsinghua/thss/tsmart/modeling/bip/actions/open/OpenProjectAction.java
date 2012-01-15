package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.progress.IProgressService;

import cn.edu.tsinghua.thss.tsmart.baseline.BaselineDataAccessor;
import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Baseline;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
@SuppressWarnings({"all"})
public class OpenProjectAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = OpenProjectAction.class.getCanonicalName();

    public OpenProjectAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText(Messages.OpenProjectAction_0);
        setToolTipText(Messages.OpenProjectAction_1);
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
        final String path = dialog.open();
        if (path == null) {
            return;
        }
        try {
            IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
            progressService.runInUI(PlatformUI.getWorkbench().getProgressService(),
                            new RunnableWithProgress(path), ResourcesPlugin.getWorkspace()
                                            .getRoot());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private class RunnableWithProgress implements IRunnableWithProgress {
        private String path;

        public RunnableWithProgress(String path) {
            super();
            this.path = path;
        }

        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException,
                        InterruptedException {
            CodeGenProjectModel project = null;
            try {
                project = CodeGenProjectModel.load(new File(path).getParentFile(), monitor);
                if (!baselineExists(project)) {
                    MessageUtil.ShowErrorDialog(Messages.OpenProjectAction_2 + project.getBaseline() + Messages.OpenProjectAction_3, Messages.OpenProjectAction_4);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                MessageUtil.ShowErrorDialog(Messages.OpenProjectAction_5, Messages.OpenProjectAction_6);
                return;
            }
            // 关闭当前所有页面
            BIPEditor.closeAllEditor();
            TopLevelModel old = GlobalProperties.getInstance().setTopModel(project);
            project.loadTypes(monitor);
            // 基准线一致性检查
            if (!project.checkValidateBaseline()) {
                if (MessageUtil.showConfirmDialog(Messages.OpenProjectAction_7, Messages.OpenProjectAction_8)) {
                    project.cleanEntityNames();
                } else {
                    BIPEditor.closeAllEditor();
                    return;
                }
            }
        }
    }

    private boolean baselineExists(ProjectModel project) {
        String baseline = project.getBaseline();
        BaselineDataAccessor bda = new BaselineDataAccessor();
        List<Baseline> baselines = bda.getBaselines();
        for (Baseline b : baselines) {
            if (b.getName().equals(baseline)) {
                return true;
            }
        }
        return false;
    }
}
