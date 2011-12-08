package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.core.runtime.Path;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPFileEditorInput;

/**
 * ���ù���̨�������ԣ�
 * 
 * @author Huangcd
 * 
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("EDOLA Modelling Platform");
    }

    @Override
    public void postWindowOpen() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        try {
            page.openEditor(new BIPFileEditorInput(new Path("")),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor", true);
        } catch (PartInitException e) {
            MessageBox errorBox = new MessageBox(window.getShell());
            errorBox.setMessage(e.getMessage());
            errorBox.open();
        }
    }


}
