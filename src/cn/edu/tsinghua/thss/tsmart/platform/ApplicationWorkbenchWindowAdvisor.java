package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewProjectAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenProjectAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.SelectOpenActionDialog;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * 设置工作台窗口属性，
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
        configurer.setInitialSize(new Point(1000, 800));
        configurer.setShowCoolBar(true);
        configurer.setShowMenuBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle("EDOLA Modelling Platform");
    }

    @Override
    public void postWindowOpen() {
        int selection = GlobalProperties.OPENPROJECT;
        SelectOpenActionDialog selectDialog =
                        new SelectOpenActionDialog(this.getWindowConfigurer().getWindow()
                                        .getShell());
        selectDialog.setBlockOnOpen(true);
        int result = selectDialog.open();
        if (Window.OK == result) {
            selection = selectDialog.getSelection();
        }
        Action action;
        switch (selection) {
            case GlobalProperties.NEWPROJECT:
                action = new NewProjectAction(getWindowConfigurer().getWindow());
                action.run();
                break;
            case GlobalProperties.OPENPROJECT:
                action = new OpenProjectAction(getWindowConfigurer().getWindow());
                action.run();
                break;
            case GlobalProperties.NEWLIBRARY:
                action = new NewLibraryAction(getWindowConfigurer().getWindow());
                action.run();
                break;
            case GlobalProperties.OPENLIBRARY:
                action = new OpenLibraryAction(getWindowConfigurer().getWindow());
                action.run();
                break;
        }
    }
}
