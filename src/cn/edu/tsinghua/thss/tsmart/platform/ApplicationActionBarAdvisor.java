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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.CloseAllAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewCompoundEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create.NewProjectAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenAtomicEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenCompoundEditorAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenLibraryAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenProjectAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open.OpenViewAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveTopLevelModelAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.CodeGenerationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditCodeGenConceptBinding;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditingCodeGenerationDevicesAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditingCodeGenerationMappingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageAtomicTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageBaselineOptionAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageCompoundTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageConnectorTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageDataTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManagePortTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ModelCheckingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.SettingModelingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.SimulationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ValidateCodeGenerationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ValidateModelCheckingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ViewBaselineAction;

/**
 * 主界面的菜单栏
 * 
 * @author Huangcd
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction                   exitAction;
    private IWorkbenchAction                   aboutAction;

    private XMLEditorAction                    xmlAction;
    private NewAtomicEditorAction              newAtomicAction;
    private NewCompoundEditorAction            newCompoundAction;
    private NewProjectAction                   newProjectAction;
    private NewLibraryAction                   newLibraryAction;

    private OpenProjectAction                  openProjectAction;
    private OpenLibraryAction                  openLibraryAction;
    private OpenAtomicEditorAction             openAtomicAction;
    private OpenCompoundEditorAction           openCompoundAction;

    private SaveTopLevelModelAction            saveTopLevelModelInMenuAction;
    private CloseAllAction                     closeAllAction;

    private ManageDataTypeAction               manageDataTypeAction;
    private ManagePortTypeAction               managePortTypeAction;
    private ManageConnectorTypeAction          manageConnectorTypeAction;
    private ManageAtomicTypeAction             manageAtomicTypeAction;
    private ManageCompoundTypeAction           manageCompoundTypeAction;
    private SettingModelingAction              settingModelingAction;

    private ManageBaselineOptionAction         manageBaselineOptionAction;
    private ViewBaselineAction                 viewBaselineAction;

    private ValidateModelCheckingAction        validateModelCheckingAction;
    private ValidateCodeGenerationAction       validateCodeGenerationAction;
    private EditingCodeGenerationMappingAction editingCodeGenerationMappingAction;
    private EditingCodeGenerationDevicesAction editingCodeGenerationDevicesAction;
    private EditCodeGenConceptBinding          editCodeGenConceptBinding;

    private ModelCheckingAction                modelCheckingAction;
    private SimulationAction                   simulationAction;
    private CodeGenerationAction               codeGenerationAction;

    private OpenViewAction                     openOutlineAction;

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
            newProjectAction = new NewProjectAction(window);
            register(newProjectAction);

            newLibraryAction = new NewLibraryAction(window);
            register(newLibraryAction);

            newAtomicAction = new NewAtomicEditorAction(window);
            register(newAtomicAction);

            newCompoundAction = new NewCompoundEditorAction(window);
            register(newCompoundAction);

            xmlAction = new XMLEditorAction(window);
            register(xmlAction);

            openLibraryAction = new OpenLibraryAction(window);
            register(openLibraryAction);

            openProjectAction = new OpenProjectAction(window);
            register(openProjectAction);

            openAtomicAction = new OpenAtomicEditorAction(window);
            register(openAtomicAction);

            openCompoundAction = new OpenCompoundEditorAction(window);
            register(openCompoundAction);

            saveTopLevelModelInMenuAction = new SaveTopLevelModelAction(window);
            register(saveTopLevelModelInMenuAction);

            closeAllAction = new CloseAllAction(window);
            register(closeAllAction);
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

            settingModelingAction = new SettingModelingAction(window);
            register(settingModelingAction);
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
            validateModelCheckingAction = new ValidateModelCheckingAction(window);
            register(validateModelCheckingAction);

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
            editCodeGenConceptBinding = new EditCodeGenConceptBinding(window);
            register(editCodeGenConceptBinding);

            validateCodeGenerationAction = new ValidateCodeGenerationAction(window);
            register(validateCodeGenerationAction);

            editingCodeGenerationMappingAction = new EditingCodeGenerationMappingAction(window);
            register(editingCodeGenerationMappingAction);

            editingCodeGenerationDevicesAction = new EditingCodeGenerationDevicesAction(window);
            register(editingCodeGenerationDevicesAction);

            codeGenerationAction = new CodeGenerationAction(window);
            register(codeGenerationAction);
        }
        // window
        {

            openOutlineAction = new OpenViewAction(window);
            register(openOutlineAction);

            /*
             * openConsoleAction=new OpenConsoleAction(); register(openConsoleAction);
             */
        }
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager("文件");

        MenuManager newMenu = new MenuManager("新建");

        MenuManager openMenu = new MenuManager("打开");

        fileMenu.add(newMenu);
        newMenu.add(newLibraryAction);
        newMenu.add(newProjectAction);
        newMenu.add(newAtomicAction);
        newMenu.add(newCompoundAction);

        fileMenu.add(openMenu);
        // openMenu.add(xmlAction);
        openMenu.add(openLibraryAction);
        openMenu.add(openProjectAction);
        openMenu.add(openAtomicAction);
        openMenu.add(openCompoundAction);

        fileMenu.add(saveTopLevelModelInMenuAction);

        fileMenu.add(new Separator());
        fileMenu.add(exitAction);

        MenuManager baselineMenu = new MenuManager("基准线");
        baselineMenu.add(manageBaselineOptionAction);
        baselineMenu.add(viewBaselineAction);

        MenuManager modelCheckingMenu = new MenuManager("模型检测");
        modelCheckingMenu.add(validateModelCheckingAction);
        modelCheckingMenu.add(modelCheckingAction);

        MenuManager simulationMenu = new MenuManager("仿真");
        simulationMenu.add(simulationAction);

        MenuManager codeGenerationMenu = new MenuManager("代码生成");
        codeGenerationMenu.add(editCodeGenConceptBinding);
        codeGenerationMenu.add(editingCodeGenerationMappingAction);
        codeGenerationMenu.add(editingCodeGenerationDevicesAction);
        codeGenerationMenu.add(validateCodeGenerationAction);
        codeGenerationMenu.add(codeGenerationAction);

        MenuManager modelingMenu = new MenuManager("建模");
        modelingMenu.add(manageDataTypeAction);
        modelingMenu.add(managePortTypeAction);
        modelingMenu.add(manageConnectorTypeAction);
        modelingMenu.add(manageAtomicTypeAction);
        modelingMenu.add(manageCompoundTypeAction);
        modelingMenu.add(new Separator());
        modelingMenu.add(settingModelingAction);

        MenuManager windowMenu = new MenuManager("窗口");

        MenuManager showViewMenu = new MenuManager("查看视图");
        windowMenu.add(showViewMenu);
        // showViewMenu.add(openConsoleAction);
        // showViewMenu.add(openOutlineAction);


        MenuManager helpMenu = new MenuManager("帮助");
        helpMenu.add(aboutAction);

        menuBar.add(fileMenu);
        menuBar.add(modelingMenu);
        menuBar.add(baselineMenu);
        menuBar.add(modelCheckingMenu);
        menuBar.add(simulationMenu);
        menuBar.add(codeGenerationMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);
    }
}
