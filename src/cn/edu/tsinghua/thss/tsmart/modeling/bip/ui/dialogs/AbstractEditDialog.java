package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.regex.Pattern;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.ModelingProperties;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;

public abstract class AbstractEditDialog extends Dialog {
    private String               title      = "{YOU NEED TO SET TITLE FIRST}";
    protected ModelingProperties properties = GlobalProperties.getInstance();
    public static final Pattern  identifier = Pattern.compile("[\\w&&[^0-9]]\\w*");

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
     * ͨ���û��������ģ�ͣ���validateUserInput����Ϊtrue֮�����
     */
    protected abstract void updateValues();

    /**
     * �Ի������ݳ�ʼ��
     */
    protected abstract void initValues();

    /**
     * ����������ʾ������Ϣ��Label
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
        getErrorLabel().setText(message);
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

    /**
     * �ж��û������Ƿ�Ϸ������û����ok��ť��ʱ����ã��������Ϸ���������updateValues����ģ�͵�ֵ��������Ϸ���ֱ�ӷ��أ�������ģ�ͣ�
     * 
     * @return
     */
    protected abstract boolean validateUserInput();
}