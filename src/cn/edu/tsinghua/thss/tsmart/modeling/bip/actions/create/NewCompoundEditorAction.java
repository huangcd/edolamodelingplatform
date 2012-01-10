package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.create;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
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
public class NewCompoundEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = NewCompoundEditorAction.class.getCanonicalName();

    public NewCompoundEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("���Ϲ���");
        setToolTipText("�½�һ�����Ϲ���");
        setImageDescriptor(Activator.getImageDescriptor("icons/compound_16.png"));
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
            CompoundTypeModel model = new CompoundTypeModel().setName("compound0");
            if (MessageUtil.showConfirmDialog("�Ƿ������ӵ���ǰ�⣿", "ȷ��")) {
                AddLibrarySettingDialog dialog =
                                new AddLibrarySettingDialog(Display.getCurrent().getActiveShell(),
                                                model);
                dialog.setBlockOnOpen(true);
                int resultCode = dialog.open();
                if (resultCode == AddLibrarySettingDialog.OK) {
                    topModel.addChild(model);
                } else {
                    MessageUtil.showMessageDialog("������������浽�⣬����Ȼ�������Ժ��Ҽ�ѡ��������浽��", "");
                }
            }
            topModel.addOpenModel(model);
            BIPEditor.openBIPEditor(model);
        } else if (topModel instanceof ProjectModel) {
            CompoundTypeModel model = new CompoundTypeModel().setName("compound0");
            topModel.addOpenModel(model);
            BIPEditor.openBIPEditor(model);
        }
    }
}
