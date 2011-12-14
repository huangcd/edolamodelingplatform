package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.platform.PlatformApplication;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
public class NewAtomicEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID =
                                                      "cn.edu.tsinghua.thss.tsmart.modeling.bip.NewAtomicEditorAction";

    public NewAtomicEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("原子组件");
        setToolTipText("新建原子组件");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                        PlatformApplication.PLUGIN_ID, "icons/atomic_16.png"));
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    @Override
    public void run() {
        try {
            window.getActivePage().openEditor(
                            new BIPModuleEditorInput(new AtomicTypeModel().setName("atomic")),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.AtomicEditor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
