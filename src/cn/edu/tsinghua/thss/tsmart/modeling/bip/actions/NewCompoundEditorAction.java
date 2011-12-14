package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPModuleEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.platform.PlatformApplication;

/**
 * �����˲˵��Ͷ�Ӧ�Ĺ��߰�ť����ʾ�ַ�����ͼ�꣬�� ����ID �ȵȡ�
 * 
 * @author Huangcd
 */
public class NewCompoundEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID =
                                                      "cn.edu.tsinghua.thss.tsmart.modeling.bip.NewCompoundEditorAction";

    public NewCompoundEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("�������");
        setToolTipText("�½�һ���������");
        setImageDescriptor(AbstractUIPlugin.imageDescriptorFromPlugin(
                        PlatformApplication.PLUGIN_ID, "icons/compound_16.png"));
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
                            new BIPModuleEditorInput(new CompoundTypeModel().setName("compound")),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.CompoundEditor");
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }
}
