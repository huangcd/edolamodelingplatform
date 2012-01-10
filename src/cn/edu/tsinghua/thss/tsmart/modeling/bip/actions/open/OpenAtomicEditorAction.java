package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.open;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic.AtomicEditor;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * �����˲˵��Ͷ�Ӧ�Ĺ��߰�ť����ʾ�ַ�����ͼ�꣬�� ����ID �ȵȡ�
 * 
 * @author Huangcd
 */
public class OpenAtomicEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = OpenAtomicEditorAction.class.getCanonicalName();

    public OpenAtomicEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("ԭ�ӹ���");
        setToolTipText("��ԭ�ӹ���");
        setImageDescriptor(Activator.getImageDescriptor("icons/atomic_16.png"));
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
        dialog.setFilterExtensions(new String[] {"*." + GlobalProperties.ATOMIC_EXTENSION});
        String path = dialog.open();
        if (path == null) {
            return;
        }
        // TODO ��Atomic��CompoundҲ��BIPEditor����ͳһ���У�����ѯ���Ƿ���ӵ���
        BIPFileEditorInput input = new BIPFileEditorInput(new File(path));

        try {
            window.getActivePage().openEditor(input, AtomicEditor.id);
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
