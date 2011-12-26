package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        final String properties = "org.eclipse.ui.views.PropertySheet";
        final String outline = "org.eclipse.ui.views.ContentOutline";
        IFolderLayout leftBottomFolder =
                        layout.createFolder("LeftButtom", IPageLayout.BOTTOM, 0.80f,
                                        layout.getEditorArea());
        leftBottomFolder.addView(properties);
        IFolderLayout rightBottomFolder =
                        layout.createFolder("RightTop", IPageLayout.RIGHT, 0.8f,
                                        layout.getEditorArea());
        rightBottomFolder.addView(outline);
        layout.setEditorAreaVisible(true);
    }
}
