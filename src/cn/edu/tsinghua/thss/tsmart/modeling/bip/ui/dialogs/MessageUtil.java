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
        MessageBox box =
                        new MessageBox(Display.getDefault().getActiveShell(), SWT.ICON_ERROR
                                        | SWT.OK);
        box.setMessage(errorMessage);
        box.setText(title);
        box.open();
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
        String title = "����������";
        String errorMessage = "�޷�������Ϊ " + name + "��ָ�������������������ظ������ǹؼ��֡�";
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
                ShowErrorDialog(name, "ɾ������");
        }
    }

    public static void ShowDeleteDataTypeErrorDialog(String name) {
        String title = "ɾ���������ͳ���";
        String errorMessage = "�޷�ɾ���������� " + name + "�����ڸ�����ʵ����";
        ShowErrorDialog(errorMessage, title);
    }

    public static void ShowDeletePortTypeErrorDialog(String name) {
        String title = "ɾ���˿����ͳ���";
        String errorMessage = "�޷�ɾ���˿����� " + name + "�����ڸ�����ʵ����";
        ShowErrorDialog(errorMessage, title);
    }

    public static void ShowDeleteConnectorTypeErrorDialog(String name) {
        String title = "ɾ�����������ͳ���";
        String errorMessage = "�޷�ɾ������������ " + name + "�����ڸ�����ʵ����";
        ShowErrorDialog(errorMessage, title);
    }

    public static void showConsoleMessage(String message) {
        MessageConsole console = new MessageConsole("EdolaModeling Platform", null); // ��Ϣ�ĳ���Ҫ������
        ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IOConsole[] {console});
        try {
            console.activate();
            IOConsoleOutputStream out = console.newOutputStream(); // ���Console�������
            out.setColor(ColorConstants.black); // ������ɫ
            // out.setFontStyle(SWT.BOLD); // ��������
            out.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addProblemMessage(IResource resource, String message, int lineNumber,
                    int severity, int priority) throws CoreException {
        if (resource != null) {
            IMarker marker = resource.createMarker(IMarker.PROBLEM);
            if (message != null) marker.setAttribute(IMarker.MESSAGE, message);
            if (lineNumber >= 0) marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
            marker.setAttribute(IMarker.SEVERITY, severity);
            marker.setAttribute(IMarker.PRIORITY, priority);
        }
    }

    public static void addProblemErrorMessage(String message) throws CoreException {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot();
        int lineNumber = -1;
        int severity = IMarker.SEVERITY_ERROR;
        int priority = IMarker.PRIORITY_NORMAL;
        addProblemMessage(resource, message, lineNumber, severity, priority);
    }

    public static void addProblemWarningMessage(String message) throws CoreException {
        IResource resource = ResourcesPlugin.getWorkspace().getRoot();
        int lineNumber = -1;
        int severity = IMarker.SEVERITY_WARNING;
        int priority = IMarker.PRIORITY_NORMAL;
        addProblemMessage(resource, message, lineNumber, severity, priority);
    }

    public static void clearProblemMessage() throws CoreException {
        ResourcesPlugin.getWorkspace().getRoot()
                        .deleteMarkers(IMarker.PROBLEM, false, IResource.DEPTH_INFINITE);
    }

}
