package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;

@SuppressWarnings("rawtypes")
public class PortEditDialog extends AbstractEditDialog {
    private PortModel  instance;
    @SuppressWarnings("unused")
    private IContainer container;
    private Text       textType;
    private Text       textName;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public PortEditDialog(Shell parentShell, PortModel data) {
        super(parentShell, Messages.PortEditDialog_0);
        this.instance = data;
        this.container = instance.getParent();
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

        Button buttonExport = new Button(container, SWT.CHECK);
        buttonExport.setBounds(278, 18, 59, 17);
        buttonExport.setText(Messages.PortEditDialog_1);

        List listAllDatas = new List(container, SWT.BORDER | SWT.MULTI);
        listAllDatas.setBounds(7, 44, 126, 161);
        listAllDatas.setTouchEnabled(true);

        textType = new Text(container, SWT.BORDER);
        textType.setBounds(45, 15, 73, 23);

        Label labelType = new Label(container, SWT.NONE);
        labelType.setBounds(7, 18, 32, 17);
        labelType.setText(Messages.PortEditDialog_2);

        Label labelName = new Label(container, SWT.NONE);
        labelName.setText(Messages.PortEditDialog_3);
        labelName.setBounds(141, 18, 32, 17);

        textName = new Text(container, SWT.BORDER);
        textName.setBounds(179, 15, 73, 23);

        List listPortDatas = new List(container, SWT.BORDER | SWT.MULTI);
        listPortDatas.setTouchEnabled(true);
        listPortDatas.setBounds(209, 44, 126, 161);

        Button buttonAdd = new Button(container, SWT.NONE);
        buttonAdd.setBounds(146, 61, 48, 27);
        buttonAdd.setText(">>"); //$NON-NLS-1$

        Button buttonRemove = new Button(container, SWT.NONE);
        buttonRemove.setText("<<"); //$NON-NLS-1$
        buttonRemove.setBounds(146, 130, 48, 27);

        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(347, 300);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {}

    @Override
    protected boolean validateUserInput() {
        return false;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
