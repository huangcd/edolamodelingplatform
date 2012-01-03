package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        final String properties = "org.eclipse.ui.views.PropertySheet";
        final String outline = "org.eclipse.ui.views.ContentOutline";
        final String errorViewPart = "org.eclipse.ui.internal.ErrorViewPart";
        IFolderLayout folder =
                        layout.createFolder("1", IPageLayout.BOTTOM, 0.80f, layout.getEditorArea());
        folder.addView(properties);
        folder = layout.createFolder("2", IPageLayout.RIGHT, 0.8f, layout.getEditorArea());
        folder.addView(outline);
        //folder = layout.createFolder("3", IPageLayout.BOTTOM, 0.80f, layout.getEditorArea());
        //folder.addView(errorViewPart);
        layout.setEditorAreaVisible(true);
    }
}
