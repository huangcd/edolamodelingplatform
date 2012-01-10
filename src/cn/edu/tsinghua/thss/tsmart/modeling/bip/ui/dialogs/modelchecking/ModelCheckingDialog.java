package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class ModelCheckingDialog extends AbstractEditDialog {

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ModelCheckingDialog(Shell parentShell) {
        super(parentShell, "\u6A21\u578B\u68C0\u6D4B");
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        Button btnNewButton = new Button(container, SWT.NONE);
        btnNewButton.setBounds(293, 10, 72, 22);
        btnNewButton.setText("\u65B0\u5EFA\u5C5E\u6027");

        Button button = new Button(container, SWT.NONE);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        button.setText("\u4FEE\u6539\u5C5E\u6027");
        button.setBounds(293, 38, 72, 22);

        Button button_1 = new Button(container, SWT.NONE);
        button_1.setText("\u5220\u9664\u5C5E\u6027");
        button_1.setBounds(293, 66, 72, 22);

        Group group = new Group(container, SWT.NONE);
        group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        group.setText("\u5171\u6027\u9700\u6C42");
        group.setBounds(10, 10, 277, 99);

        Button button_2 = new Button(group, SWT.CHECK);
        button_2.setBounds(23, 23, 59, 16);
        button_2.setText("\u5C5E\u60271");
        button_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        Button button_3 = new Button(group, SWT.CHECK);
        button_3.setBounds(23, 45, 59, 16);
        button_3.setText("\u5C5E\u60272");
        button_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        Button button_4 = new Button(group, SWT.CHECK);
        button_4.setBounds(23, 67, 59, 16);
        button_4.setText("\u5C5E\u60273");
        button_4.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        Group group_1 = new Group(container, SWT.NONE);
        group_1.setText("\u7528\u6237\u81EA\u5B9A\u4E49\u9700\u6C42");
        group_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
        group_1.setBounds(10, 109, 277, 99);

        Button button_5 = new Button(group_1, SWT.CHECK);
        button_5.setText("\u5C5E\u60274");
        button_5.setBounds(23, 23, 59, 16);
        button_5.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        Button button_6 = new Button(group_1, SWT.CHECK);
        button_6.setText("\u5C5E\u60275");
        button_6.setBounds(23, 45, 59, 16);
        button_6.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));

        Button button_7 = new Button(group_1, SWT.CHECK);
        button_7.setText("\u5C5E\u60276");
        button_7.setBounds(23, 67, 59, 16);
        button_7.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));


        initValues();
        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        Button button =
                        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                                        true);
        button.setText("\u786E\u5B9A");
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText("\u53D6\u6D88");
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(374, 296);
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void updateValues() {

    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {

        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
