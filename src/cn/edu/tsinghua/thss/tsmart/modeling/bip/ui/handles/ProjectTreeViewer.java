package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import java.util.Arrays;

import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class ProjectTreeViewer extends TreeViewer {
    private final static Event mouseDoubleClick = new Event();
    static {
        mouseDoubleClick.type = SWT.MouseDoubleClick;
    }

    @Override
    protected void hookControl() {
        final Tree tree = (Tree) getControl();
        System.out.println("tree");
        tree.addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent e) {}

            @Override
            public void mouseDown(MouseEvent e) {}

            @Override
            public void mouseDoubleClick(MouseEvent e) {
                if (tree.getSelectionCount() == 0) {
                    return;
                }
                TreeItem[] items = tree.getSelection();
                for (TreeItem item : items) {
                    System.out.println(item);
                    System.out.println(Arrays.toString(item.getListeners(SWT.MouseDoubleClick)));
                    for (Listener listener : item.getListeners(SWT.MouseDoubleClick)) {
                        listener.handleEvent(mouseDoubleClick);
                    }
                }
            }
        });
        super.hookControl();
    }
}
