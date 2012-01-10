package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditPropertyDialog extends AbstractEditDialog {
    private Text text;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditPropertyDialog(Shell parentShell) {
        super(parentShell, "\u7F16\u8F91\u5C5E\u6027");
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

        text = new Text(container, SWT.BORDER | SWT.MULTI);
        text.setBounds(10, 40, 348, 169);

        ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
        toolBar.setBounds(10, 10, 348, 25);

        ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
        toolItem.setText("\u53D8\u91CF");

        ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
        toolItem_1.setText("==");

        ToolItem toolItem_2 = new ToolItem(toolBar, SWT.NONE);
        toolItem_2.setText("!=");

        ToolItem toolItem_3 = new ToolItem(toolBar, SWT.NONE);
        toolItem_3.setText("\\/");

        ToolItem toolItem_4 = new ToolItem(toolBar, SWT.NONE);
        toolItem_4.setText("/\\");

        ToolItem tltmNot = new ToolItem(toolBar, SWT.NONE);
        tltmNot.setText("not");


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
