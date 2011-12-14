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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.NewAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.NewCompoundEditorAction;

/**
 * 主界面的菜单栏
 * 
 * @author Huangcd
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction        exitAction;
    private IWorkbenchAction        aboutAction;
    private NewAtomicEditorAction   newAtomicAction;
    private NewCompoundEditorAction newCompoundAction;
    private XMLEditorAction         xmlAction;

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
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("文件", "文件");
        
        MenuManager newMenu = new MenuManager("新建", "new");
        
        MenuManager openMenu = new MenuManager("打开", "open");
        
        fileMenu.add(newMenu);
        newMenu.add(newAtomicAction);
        newMenu.add(newCompoundAction);
        
        fileMenu.add(openMenu);
        openMenu.add(xmlAction);
        
        fileMenu.add(new Separator());
        fileMenu.add(exitAction);

        MenuManager helpMenu = new MenuManager("帮助", "help");
        helpMenu.add(aboutAction);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
    }
}
