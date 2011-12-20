package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import cn.edu.tsinghua.thss.tsmart.editors.xml.XMLEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ManageConnectorTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ManageDataTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.ManagePortTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.NewAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.NewCompoundEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenCompoundEditorAction;

/**
 * 主界面的菜单栏
 * 
 * @author Huangcd
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction          exitAction;
    private IWorkbenchAction          aboutAction;
    private NewAtomicEditorAction     newAtomicAction;
    private NewCompoundEditorAction   newCompoundAction;
    private ManageDataTypeAction      manageDataTypeAction;
    private ManagePortTypeAction      managePortTypeAction;
    private ManageConnectorTypeAction manageConnectorTypeAction;
    private OpenAtomicEditorAction    openAtomicAction;
    private OpenCompoundEditorAction  openCompoundAction;
    private XMLEditorAction           xmlAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);
        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
        newAtomicAction = new NewAtomicEditorAction(window);
        register(newAtomicAction);
        newCompoundAction = new NewCompoundEditorAction(window);
        register(newCompoundAction);
        xmlAction = new XMLEditorAction(window);
        register(xmlAction);
        manageDataTypeAction = new ManageDataTypeAction(window);
        register(manageDataTypeAction);
        managePortTypeAction = new ManagePortTypeAction(window);
        register(managePortTypeAction);
        manageConnectorTypeAction = new ManageConnectorTypeAction(window);
        register(manageConnectorTypeAction);
        openAtomicAction = new OpenAtomicEditorAction(window);
        register(openAtomicAction);
        openCompoundAction = new OpenCompoundEditorAction(window);
        register(openCompoundAction);
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("文件", "file");

        MenuManager newMenu = new MenuManager("新建", "new");

        MenuManager openMenu = new MenuManager("打开", "open");

        fileMenu.add(newMenu);
        newMenu.add(newAtomicAction);
        newMenu.add(newCompoundAction);

        fileMenu.add(openMenu);
        openMenu.add(xmlAction);
        openMenu.add(openAtomicAction);
        openMenu.add(openCompoundAction);

        fileMenu.add(new Separator());
        fileMenu.add(exitAction);

        MenuManager modelingMenu = new MenuManager("建模", "modeling");
        modelingMenu.add(manageDataTypeAction);
        modelingMenu.add(managePortTypeAction);
        modelingMenu.add(manageConnectorTypeAction);

        MenuManager helpMenu = new MenuManager("帮助", "help");
        helpMenu.add(aboutAction);

        menuBar.add(fileMenu);
        menuBar.add(modelingMenu);
        menuBar.add(helpMenu);
    }
}
