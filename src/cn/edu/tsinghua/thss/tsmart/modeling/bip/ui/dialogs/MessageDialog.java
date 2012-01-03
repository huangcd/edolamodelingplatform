package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import javax.swing.JOptionPane;

public class MessageDialog {
    public static int DELETEDATATYPE      = 1;
    public static int DELETEPORTTYPE      = 2;
    public static int DELETECONNECTORTYPE = 3;

    public static void ShowErrorDialog(String errorMessage, String title) {
        // MessageBox box =
        // new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_ERROR
        // | SWT.OK);
        // box.setMessage(errorMessage);
        // box.setText(title);
        // box.open();
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void ShowWarningDialog(String errorMessage, String title) {
        // MessageBox box =
        // new MessageBox(Display.getCurrent().getActiveShell(), SWT.ICON_WARNING
        // | SWT.OK);
        // box.setMessage(errorMessage);
        // box.setText(title);
        // box.open();
        JOptionPane.showMessageDialog(null, errorMessage, title, JOptionPane.WARNING_MESSAGE);
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
}
