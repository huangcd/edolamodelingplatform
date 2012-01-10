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
 * 定义了菜单和对应的工具按钮的显示字符串和图标，以 及其ID 等等。
 * 
 * @author Huangcd
 */
public class NewCompoundEditorAction extends Action implements ISelectionListener, IWorkbenchAction {

    private final IWorkbenchWindow window;
    public static final String     ID = NewCompoundEditorAction.class.getCanonicalName();

    public NewCompoundEditorAction(IWorkbenchWindow window) {
        this.window = window;
        setId(ID);
        setText("复合构件");
        setToolTipText("新建一个复合构件");
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
        // XXX 考虑在项目视图下是否允许新建组件
        // if (topModel instanceof ProjectModel) {
        // MessageUtil.ShowErrorDialog("项目模式下不能新建组件", "错误");
        // return;
        // }
        if (topModel instanceof LibraryModel) {
            CompoundTypeModel model = new CompoundTypeModel().setName("compound0");
            if (MessageUtil.showConfirmDialog("是否将组件添加到当前库？", "确认")) {
                AddLibrarySettingDialog dialog =
                                new AddLibrarySettingDialog(Display.getCurrent().getActiveShell(),
                                                model);
                dialog.setBlockOnOpen(true);
                int resultCode = dialog.open();
                if (resultCode == AddLibrarySettingDialog.OK) {
                    topModel.addChild(model);
                } else {
                    MessageUtil.showMessageDialog("组件将不被保存到库，您依然可以在以后右键选择将组件保存到库", "");
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
