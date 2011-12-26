package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPFileEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

/**
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
public class OpenCompoundEditorAction extends Action
                implements
                    ISelectionListener,
                    IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = OpenCompoundEditorAction.class.getCanonicalName();

    public OpenCompoundEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("复合组件");
        setToolTipText("打开一个复合组件");
        setImageDescriptor(Activator.getImageDescriptor("icons/compound_16.png"));
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
        Shell shell = Display.getCurrent().getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.OPEN);
        dialog.setFilterExtensions(new String[] {"*.edolam"});
        String path = dialog.open();
        if (path == null) {
            return;
        }
        BIPFileEditorInput input = new BIPFileEditorInput(new File(path));

        try {
            window.getActivePage().openEditor(input, CompoundEditor.id);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
