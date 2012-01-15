package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.io.IOException;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.MessageConsole;

public class MessageUtil {
    public static int DELETE_DATA_TYPE      = 1;
    public static int DELETE_PORT_TYPE      = 2;
    public static int DELETE_CONNECTOR_TYPE = 3;

    public static void ShowErrorDialog(String errorMessage, String title) {
        try {
            MessageBox box =
                            new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR
                                            | SWT.OK);
            box.setMessage(errorMessage);
            box.setText(title);
            box.open();
        } catch (Exception ex) {

        }
    }

    public static void ShowWarningDialog(String errorMessage, String title) {
        MessageBox box =
                        new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_WARNING
                                        | SWT.OK);
        box.setMessage(errorMessage);
        box.setText(title);
        box.open();
    }

    public static boolean showConfirmDialog(String message, String title) {
        MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.YES | SWT.NO);
        box.setMessage(message);
        box.setText(title);
        int result = box.open();
        if (result == SWT.YES) {
            return true;
        }
        return false;
    }

    public static boolean showMessageDialog(String message, String title) {
        MessageBox box = new MessageBox(Display.getCurrent().getActiveShell(), SWT.OK);
        box.setMessage(message);
        box.setText(title);
        int result = box.open();
        if (result == SWT.YES) {
            return true;
        }
        return false;
    }

    public static void ShowRenameErrorDialog(String name) {
        String title = Messages.MessageUtil_0;
        String errorMessage = Messages.MessageUtil_1 + name + Messages.MessageUtil_2;
        ShowErrorDialog(errorMessage, title);
    }

    public static void ShowDeleteErrorDialog(String name, int type) {
        switch (type) {
            case 1:
                ShowDeleteDataTypeErrorDialog(name);
            case 2:
                ShowDeletePortTypeErrorDialog(name);
            case 3:
                ShowDeleteConnectorTypeErrorDialog(name);
            default:
                ShowErrorDialog(name, Messages.MessageUtil_3);
        }
    }

    public static void ShowDeleteDataTypeErrorDialog(String name) {
        String title = Messages.MessageUtil_4;
        String errorMessage = Messages.MessageUtil_5 + name + Messages.MessageUtil_6;
        ShowErrorDialog(errorMessage, title);
    }

    public static void ShowDeletePortTypeErrorDialog(String name) {
        String title = Messages.MessageUtil_7;
        String errorMessage = Messages.MessageUtil_8 + name + Messages.MessageUtil_9;
        ShowErrorDialog(errorMessage, title);
    }

    public static void ShowDeleteConnectorTypeErrorDialog(String name) {
        String title = Messages.MessageUtil_10;
        String errorMessage = Messages.MessageUtil_11 + name + Messages.MessageUtil_12;
        ShowErrorDialog(errorMessage, title);
    }

    public static void showConsoleMessage(String message) {
        MessageConsole console = new MessageConsole(Messages.MessageUtil_13, null); // 信息改成需要的名字
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IOConsole[] {console});
        try {
            console.activate();
            IOConsoleOutputStream out = console.newOutputStream(); // 获得Console的输出流
            out.setColor(ColorConstants.black); // 设置颜色
            // out.setFontStyle(SWT.BOLD); // 设置字体
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addProblemMessage(IResource resource, String message, int lineNumber,
                    int severity, int priority) {
        try {
            if (resource != null) {
                IMarker marker = resource.createMarker(IMarker.PROBLEM);
                if (message != null) marker.setAttribute(IMarker.MESSAGE, message);
                if (lineNumber >= 0) marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
                marker.setAttribute(IMarker.SEVERITY, severity);
                marker.setAttribute(IMarker.PRIORITY, priority);
            }
        } catch (CoreException ex) {
            ex.printStackTrace();
        }
    }

    public static void addProblemErrorMessage(String message) {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot();
        int lineNumber = -1;
        int severity = IMarker.SEVERITY_ERROR;
        int priority = IMarker.PRIORITY_NORMAL;
        addProblemMessage(resource, message, lineNumber, severity, priority);
    }

    public static void addProblemWarningMessage(String message) {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot();
        int lineNumber = -1;
        int severity = IMarker.SEVERITY_WARNING;
        int priority = IMarker.PRIORITY_NORMAL;
        addProblemMessage(resource, message, lineNumber, severity, priority);
    }

    public static void clearProblemMessage() {
        try {
            ResourcesPlugin.getWorkspace().getRoot()
                            .deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

}
