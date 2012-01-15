package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.regex.Pattern;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

public abstract class AbstractEditDialog extends Dialog {
    private String               title      = "{YOU NEED TO SET TITLE FIRST}"; //$NON-NLS-1$
    protected GlobalProperties   properties = GlobalProperties.getInstance();
    private static final Pattern identifier = Pattern.compile("[\\w&&[^0-9]]\\w*"); //$NON-NLS-1$

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

    /**
     * 返回用于显示错误信息的Label
     * 
     * @return
     */
    protected abstract Label getErrorLabel();

    /**
     * 
     * @param message
     */
    protected void handleError(String message) {
        if (getErrorLabel() == null) {
            System.err.println(message);
            return;
        }
        getErrorLabel().setForeground(ColorConstants.red);
        String txt=message;;
        //txt=getErrorLabel().getText()+"\n"+txt;
        getErrorLabel().setText(txt);
    }

    protected void clearError() {
        if (getErrorLabel() == null) {            
            return;
        }
        getErrorLabel().setForeground(ColorConstants.red);
        getErrorLabel().setText(""); //$NON-NLS-1$
    }
    
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

    public boolean isIdentifier(String str) {
        return identifier.matcher(str).matches();
    }

    /**
     * 判断用户输入是否合法。在用户点击ok按钮的时候调用，如果输入合法，将调用updateValues更新模型的值；如果不合法，直接返回（不更改模型）
     * 
     * @return
     */
    protected abstract boolean validateUserInput();
}
