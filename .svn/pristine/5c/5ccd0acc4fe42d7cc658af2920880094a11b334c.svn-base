package cn.edu.tsinghua.thss.tsmart.modeling.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MessageBoxUtil {
    public static void ShowErrorMessage(String title, String message) {
        MessageBox box =
                        new MessageBox(Display.getCurrent().getActiveShell(), SWT.OK
                                        | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
        box.setText(title);
        box.setMessage(message);
        box.open();
        // 打印错误信息，由此判断是哪里调用这个函数
        new Exception().printStackTrace();
    }

    public static void ShowErrorMessage(Shell parent, String title, String message) {
        MessageBox box = new MessageBox(parent, SWT.OK | SWT.ICON_ERROR | SWT.APPLICATION_MODAL);
        box.setText(title);
        box.setMessage(message);
        box.open();
        // 打印错误信息，由此判断是哪里调用这个函数
        new Exception().printStackTrace();
    }
}
