package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    private static final String PERSPECTIVE_ID = "EdolaModelingPlatform.perspective"; //$NON-NLS-1$

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return PERSPECTIVE_ID;
    }
    //TODO ��Ŀ��ͼ��
    //����������Ĺ��̵����Navigator����ʾ,���޸�
    @Override
    public IAdaptable getDefaultPageInput() {

        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        return workspace.getRoot();

    }
    
    @SuppressWarnings("deprecation")
    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        org.eclipse.ui.ide.IDE.registerAdapters();
    }
}
