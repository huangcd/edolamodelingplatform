package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;
import cn.edu.tsinghua.thss.tsmart.platform.Properties;

public abstract class AbstractEditDialog extends Dialog {
    private String title = "{YOU NEED TO SET TITLE FIRST}";
    protected Properties properties = new GlobalProperties();

    protected AbstractEditDialog(Shell shell, String title) {
        super(shell);
        setTitle(title);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(getTitle());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 对话框内容初始化
     */
    protected abstract void initValues();

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * 判断用户输入是否合法
     * 
     * @return
     */
    protected abstract boolean validateUserInput();
}
