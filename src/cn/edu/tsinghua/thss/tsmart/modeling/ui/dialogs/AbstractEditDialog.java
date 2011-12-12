package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.ModelingProperties;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

public abstract class AbstractEditDialog extends Dialog {
    private String               title      = "{YOU NEED TO SET TITLE FIRST}";
    protected ModelingProperties properties = GlobalProperties.getInstance();

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
     * 通过用户输入更新模型，在validateUserInput返回为true之后调用
     */
    protected abstract void updateValues();

    /**
     * 对话框内容初始化
     */
    protected abstract void initValues();



    @Override
    protected void okPressed() {
        if (!validateUserInput()) {
            return;
        }
        updateValues();
        super.okPressed();
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * 判断用户输入是否合法。在用户点击ok按钮的时候调用，如果输入合法，将调用updateValues更新模型的值；如果不合法，直接返回（不更改模型）
     * 
     * @return
     */
    protected abstract boolean validateUserInput();
}
