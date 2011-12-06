package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.tsinghua.thss.tsmart.platform.PlatformApplication;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 * 
 */
public class BIPEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = "cn.edu.tsinghua.thss.tsmart.BIPEditAction";
    private IStructuredSelection   selection;

    public BIPEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("&BIP Editor");
        setToolTipText("Draw a BIP model");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                        PlatformApplication.PLUGIN_ID, "icons/compound_16.png"));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        // TODO Auto-generated method stub
    }

    @Override
    public void run() {
        try {
            window.getActivePage().openEditor(new BIPFileEditorInput(new Path("plugin.xml")),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
