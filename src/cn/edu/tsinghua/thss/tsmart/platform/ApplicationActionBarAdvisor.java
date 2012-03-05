package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.ExportLibrariesAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.ImportLibrariesAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save.SaveTopLevelModelAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.CodeGenerationAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.CodeRunAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditCodeGenConceptBinding;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditModelCheckingProperties;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditProritiesAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditingCodeGenerationDevicesAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.EditingCodeGenerationMappingAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageAtomicTypeAction;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.types.ManageBaselineAction;
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
import cn.edu.tsinghua.thss.tsmart.platform.language.SwitchChineseAction;
import cn.edu.tsinghua.thss.tsmart.platform.language.SwitchEnglishAction;

/**
 * 主界面的菜单栏
 * 
 * @author Huangcd
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    private IWorkbenchAction                   exitAction;
    private IWorkbenchAction                   aboutAction;

    private NewAtomicEditorAction              newAtomicAction;
    private NewCompoundEditorAction            newCompoundAction;
    private NewProjectAction                   newProjectAction;
    private NewLibraryAction                   newLibraryAction;

    private OpenProjectAction                  openProjectAction;
    private OpenLibraryAction                  openLibraryAction;
    private OpenAtomicEditorAction             openAtomicAction;
    private OpenCompoundEditorAction           openCompoundAction;

    private SaveTopLevelModelAction            saveTopLevelModelInMenuAction;
    private ExportLibrariesAction              exportLibrariesAction;
    private ImportLibrariesAction              importLibrariesAction;
    private CloseAllAction                     closeAllAction;

    private ManageDataTypeAction               manageDataTypeAction;
    private ManagePortTypeAction               managePortTypeAction;
    private ManageConnectorTypeAction          manageConnectorTypeAction;
    private ManageAtomicTypeAction             manageAtomicTypeAction;
    private ManageCompoundTypeAction           manageCompoundTypeAction;
    private SettingModelingAction              settingModelingAction;
    private EditProritiesAction                editProritiesAction;

    private ManageBaselineOptionAction         manageBaselineOptionAction;
    private ViewBaselineAction                 viewBaselineAction;
    private ManageBaselineAction               manageBaselineAction;

    private ValidateModelCheckingAction        validateModelCheckingAction;
    private ValidateCodeGenerationAction       validateCodeGenerationAction;
    private EditingCodeGenerationMappingAction editingCodeGenerationMappingAction;
    private EditingCodeGenerationDevicesAction editingCodeGenerationDevicesAction;
    private EditCodeGenConceptBinding          editCodeGenConceptBinding;

    private ModelCheckingAction                modelCheckingAction;
    private EditModelCheckingProperties        editModelCheckingProperties;
    private SimulationAction                   simulationAction;
    private CodeGenerationAction               codeGenerationAction;
    private CodeRunAction                      codeRunAction;

    private OpenViewAction                     openOutlineAction;

    private SwitchChineseAction                switchChineseAction;
    private SwitchEnglishAction                switchEnglishAction;

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

            exportLibrariesAction = new ExportLibrariesAction(window);
            register(exportLibrariesAction);

            importLibrariesAction = new ImportLibrariesAction(window);
            register(importLibrariesAction);

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

            editProritiesAction = new EditProritiesAction(window);
            register(editProritiesAction);

            settingModelingAction = new SettingModelingAction(window);
            register(settingModelingAction);
        }

        // baseline
        {
            manageBaselineOptionAction = new ManageBaselineOptionAction(window);
            register(manageBaselineOptionAction);

            viewBaselineAction = new ViewBaselineAction(window);
            register(viewBaselineAction);

            manageBaselineAction = new ManageBaselineAction(window);
            register(manageBaselineAction);
        }

        // model checking
        {
            validateModelCheckingAction = new ValidateModelCheckingAction(window);
            register(validateModelCheckingAction);

            editModelCheckingProperties = new EditModelCheckingProperties(window);
            register(editModelCheckingProperties);

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

            codeRunAction = new CodeRunAction(window);
            register(codeRunAction);
        }
        // window
        {

            openOutlineAction = new OpenViewAction(window);
            register(openOutlineAction);

            /*
             * openConsoleAction=new OpenConsoleAction(); register(openConsoleAction);
             */
        }
        // language-switch
        {
            switchChineseAction = new SwitchChineseAction();
            register(switchChineseAction);
            // SwitchEnglishAction
            switchEnglishAction = new SwitchEnglishAction();
            register(switchEnglishAction);
        }
    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager fileMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_0);

        MenuManager newMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_1);

        MenuManager openMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_2);

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

        fileMenu.add(new Separator());
        fileMenu.add(saveTopLevelModelInMenuAction);
        fileMenu.add(exportLibrariesAction);
        fileMenu.add(importLibrariesAction);

        fileMenu.add(new Separator());
        fileMenu.add(exitAction);

        MenuManager baselineMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_3);
        baselineMenu.add(manageBaselineOptionAction);
        baselineMenu.add(viewBaselineAction);
        baselineMenu.add(manageBaselineAction);

        MenuManager modelCheckingMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_4);
        modelCheckingMenu.add(validateModelCheckingAction);
        modelCheckingMenu.add(editModelCheckingProperties);
        modelCheckingMenu.add(modelCheckingAction);

        MenuManager simulationMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_5);
        simulationMenu.add(simulationAction);

        MenuManager codeGenerationMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_6);
        codeGenerationMenu.add(editCodeGenConceptBinding);
        codeGenerationMenu.add(editingCodeGenerationMappingAction);
        codeGenerationMenu.add(editingCodeGenerationDevicesAction);
        codeGenerationMenu.add(validateCodeGenerationAction);
        codeGenerationMenu.add(codeGenerationAction);
        codeGenerationMenu.add(codeRunAction);

        MenuManager modelingMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_7);
        modelingMenu.add(manageDataTypeAction);
        modelingMenu.add(managePortTypeAction);
        modelingMenu.add(manageConnectorTypeAction);
        modelingMenu.add(manageAtomicTypeAction);
        modelingMenu.add(manageCompoundTypeAction);
        modelingMenu.add(editProritiesAction);
        modelingMenu.add(new Separator());
        //modelingMenu.add(settingModelingAction);

        MenuManager windowMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_8);

        MenuManager showViewMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_9);
        windowMenu.add(showViewMenu);
        // showViewMenu.add(openConsoleAction);
        // showViewMenu.add(openOutlineAction);

        MenuManager helpMenu = new MenuManager(Messages.ApplicationActionBarAdvisor_10);
        helpMenu.add(aboutAction);
        helpMenu.add(switchChineseAction);
        helpMenu.add(switchEnglishAction);

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
