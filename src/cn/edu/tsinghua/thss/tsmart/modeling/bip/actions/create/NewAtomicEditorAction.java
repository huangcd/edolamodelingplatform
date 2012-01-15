package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AddLibrarySettingDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * �����˲˵��Ͷ�Ӧ�Ĺ��߰�ť����ʾ�ַ�����ͼ�꣬�� ����ID �ȵȡ�
 * 
 * @author Huangcd
 */
public class NewAtomicEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = NewAtomicEditorAction.class.getCanonicalName();

    public NewAtomicEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText(Messages.NewAtomicEditorAction_0);
        setToolTipText(Messages.NewAtomicEditorAction_1);
        setImageDescriptor(Activator.getImageDescriptor("icons/atomic_16.png")); //$NON-NLS-1$
        window.getSelectionService().addSelectionListener(this);
    }

    @Override
    public void dispose() {
        window.getSelectionService().removePostSelectionListener(this);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    @SuppressWarnings("rawtypes")
    @Override
    public void run() {
        GlobalProperties properties = GlobalProperties.getInstance();
        TopLevelModel topModel = properties.getTopModel();
        // XXX ��������Ŀ��ͼ���Ƿ������½����
        // if (topModel instanceof ProjectModel) {
        // MessageUtil.ShowErrorDialog("��Ŀģʽ�²����½����", "����");
        // return;
        // }
        if (topModel instanceof LibraryModel) {
            AtomicTypeModel model = new AtomicTypeModel().setName("atomic0"); //$NON-NLS-1$
            if (MessageUtil.showConfirmDialog(Messages.NewAtomicEditorAction_4, Messages.NewAtomicEditorAction_5)) {
                AddLibrarySettingDialog dialog =
                                new AddLibrarySettingDialog(Display.getCurrent().getActiveShell(),
                                                model);
                dialog.setBlockOnOpen(true);
                int resultCode = dialog.open();
                if (resultCode == AddLibrarySettingDialog.OK) {
                    topModel.addChild(model);
                } else {
                    MessageUtil.showMessageDialog(Messages.NewAtomicEditorAction_6, ""); //$NON-NLS-2$
                }
            }
            topModel.addOpenModel(model);
            BIPEditor.openBIPEditor(model);
        } else if (topModel instanceof ProjectModel) {
            AtomicTypeModel model = new AtomicTypeModel().setName("atomic0"); //$NON-NLS-1$
            if (MessageUtil.showConfirmDialog(Messages.NewAtomicEditorAction_9, Messages.NewAtomicEditorAction_10)) {
                AddLibrarySettingDialog dialog =
                                new AddLibrarySettingDialog(Display.getCurrent().getActiveShell(),
                                                model);
                dialog.setBlockOnOpen(true);
                int resultCode = dialog.open();
                if (resultCode == AddLibrarySettingDialog.OK) {
                    topModel.addChild(model);
                }
            }
            topModel.addOpenModel(model);
            BIPEditor.openBIPEditor(model);
        }
    }
}
