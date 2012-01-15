package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.navigator;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.views.ViewsPlugin;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor.ProjectContentOutlinePage;

@SuppressWarnings("restriction")
public class ProjectView extends ContentOutline {
    public static final String ID = ProjectView.class.getCanonicalName();

    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(getPageBook(), ID);
    }

    /*
     * (non-Javadoc) Method declared on PageBookView.
     */
    protected PageRec doCreatePage(IWorkbenchPart part) {
        // Try to get an outline page.
        Object obj = ViewsPlugin.getAdapter(part, ProjectContentOutlinePage.class, false);
        if (obj instanceof IContentOutlinePage) {
            IContentOutlinePage page = (IContentOutlinePage) obj;
            if (page instanceof IPageBookViewPage) {
                initPage((IPageBookViewPage) page);
            }
            page.createControl(getPageBook());
            return new PageRec(part, page);
        }
        // There is no content outline
        return null;
    }
}
