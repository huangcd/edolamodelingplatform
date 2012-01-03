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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.OpenCompoundEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.CodeGenerationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditingCodeGenerationMappingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageAtomicTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageBaselineOptionAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageCompoundTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageConnectorTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageDataTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManagePortTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ModelCheckingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.SimulationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ValidateAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ViewBaselineAction;

/**
 * 主界面的菜单栏
 * 
 * @author Huangcd
 * 
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction                   exitAction;
    private IWorkbenchAction                   aboutAction;

    private XMLEditorAction                    xmlAction;
    private NewAtomicEditorAction              newAtomicAction;
    private NewCompoundEditorAction            newCompoundAction;
    private OpenAtomicEditorAction             openAtomicAction;
    private OpenCompoundEditorAction           openCompoundAction;

    private ManageDataTypeAction               manageDataTypeAction;
    private ManagePortTypeAction               managePortTypeAction;
    private ManageConnectorTypeAction          manageConnectorTypeAction;
    private ManageAtomicTypeAction             manageAtomicTypeAction;
    private ManageCompoundTypeAction           manageCompoundTypeAction;

    private ManageBaselineOptionAction         manageBaselineOptionAction;
    private ViewBaselineAction                 viewBaselineAction;

    private ValidateAction                     validateAction;
    private ModelCheckingAction                modelCheckingAction;
    private SimulationAction                   simulationAction;
    private EditingCodeGenerationMappingAction editingCodeGenerationMappingAction;
    private CodeGenerationAction               codeGenerationAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        // system
        {
            exitAction = ActionFactory.QUIT.create(window);
            register(exitAction);

            aboutAction = ActionFactory.ABOUT.create(window);
            register(aboutAction);
        }

        // file
        {
            newAtomicAction = new NewAtomicEditorAction(window);
            register(newAtomicAction);

            newCompoundAction = new NewCompoundEditorAction(window);
            register(newCompoundAction);

            xmlAction = new XMLEditorAction(window);
            register(xmlAction);

            openAtomicAction = new OpenAtomicEditorAction(window);
            register(openAtomicAction);

            openCompoundAction = new OpenCompoundEditorAction(window);
            register(openCompoundAction);
        }

        // types
        {
            manageDataTypeAction = new ManageDataTypeAction(window);
            register(manageDataTypeAction);

            managePortTypeAction = new ManagePortTypeAction(window);
            register(managePortTypeAction);

            manageConnectorTypeAction = new ManageConnectorTypeAction(window);
            register(manageConnectorTypeAction);

            manageAtomicTypeAction = new ManageAtomicTypeAction(window);
            register(manageAtomicTypeAction);

            manageCompoundTypeAction = new ManageCompoundTypeAction(window);
            register(manageCompoundTypeAction);
        }

        // baseline
        {
            manageBaselineOptionAction = new ManageBaselineOptionAction(window);
            register(manageBaselineOptionAction);

            viewBaselineAction = new ViewBaselineAction(window);
            register(viewBaselineAction);
        }

        // model checking
        {
            validateAction = new ValidateAction(window);
            register(validateAction);

            modelCheckingAction = new ModelCheckingAction(window);
            register(modelCheckingAction);
        }

        // simulation
        {
            simulationAction = new SimulationAction(window);
            register(simulationAction);
        }

        // code generation
        {
            editingCodeGenerationMappingAction = new EditingCodeGenerationMappingAction(window);
            register(editingCodeGenerationMappingAction);

            codeGenerationAction = new CodeGenerationAction(window);
            register(codeGenerationAction);
        }
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("文件");

        MenuManager newMenu = new MenuManager("新建");

        MenuManager openMenu = new MenuManager("打开");

        fileMenu.add(newMenu);
        newMenu.add(newAtomicAction);
        newMenu.add(newCompoundAction);

        fileMenu.add(openMenu);
        openMenu.add(xmlAction);
        openMenu.add(openAtomicAction);
        openMenu.add(openCompoundAction);

        fileMenu.add(new Separator());
        fileMenu.add(exitAction);

        MenuManager baselineMenu = new MenuManager("基准线");
        baselineMenu.add(manageBaselineOptionAction);
        baselineMenu.add(viewBaselineAction);

        MenuManager modelCheckingMenu = new MenuManager("模型检测");
        modelCheckingMenu.add(validateAction);
        modelCheckingMenu.add(modelCheckingAction);

        MenuManager simulationMenu = new MenuManager("仿真");
        simulationMenu.add(simulationAction);

        MenuManager codeGenerationMenu = new MenuManager("代码生成");
        codeGenerationMenu.add(editingCodeGenerationMappingAction);
        codeGenerationMenu.add(validateAction);
        codeGenerationMenu.add(codeGenerationAction);

        MenuManager modelingMenu = new MenuManager("建模");
        modelingMenu.add(manageDataTypeAction);
        modelingMenu.add(managePortTypeAction);
        modelingMenu.add(manageConnectorTypeAction);
        modelingMenu.add(manageAtomicTypeAction);
        modelingMenu.add(manageCompoundTypeAction);

        MenuManager helpMenu = new MenuManager("帮助");
        helpMenu.add(aboutAction);

        menuBar.add(fileMenu);
        menuBar.add(modelingMenu);
        menuBar.add(baselineMenu);
        menuBar.add(modelCheckingMenu);
        menuBar.add(simulationMenu);
        menuBar.add(codeGenerationMenu);
        menuBar.add(helpMenu);
    }
}
