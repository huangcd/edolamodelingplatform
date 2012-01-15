package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.VariableSelectionDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingProperty;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingPropertySyntax;

/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditPropertyDialog extends AbstractEditDialog {
    private Text                  content;
    private Text                  description;
    private Label                 errorLabel;

    private ModelCheckingProperty property = null;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditPropertyDialog(Shell parentShell, ModelCheckingProperty property) {
        super(parentShell, Messages.EditPropertyDialog_0);
        setTitle(Messages.EditPropertyDialog_1);
        this.property = property;
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

        content = new Text(container, SWT.BORDER | SWT.MULTI);
        content.setBounds(10, 70, 425, 186);

        ToolBar toolBar = new ToolBar(container, SWT.FLAT | SWT.RIGHT);
        toolBar.setBounds(10, 39, 425, 25);

        ToolItem toolItem = new ToolItem(toolBar, SWT.NONE);
        toolItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                VariableSelectionDialog dialog = new VariableSelectionDialog(getShell(), false);
                if (Window.OK == dialog.open()) {
                    content.insert(dialog.getData());
                }
            }
        });
        toolItem.setText(Messages.EditPropertyDialog_2);

        ToolItem toolItem_1 = new ToolItem(toolBar, SWT.NONE);
        toolItem_1.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		content.insert(" == "); //$NON-NLS-1$
        	}
        });
        toolItem_1.setText("=="); //$NON-NLS-1$

        ToolItem toolItem_3 = new ToolItem(toolBar, SWT.NONE);
        toolItem_3.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		content.insert("|"); //$NON-NLS-1$
        	}
        });
        toolItem_3.setText(" | "); //$NON-NLS-1$

        ToolItem toolItem_4 = new ToolItem(toolBar, SWT.NONE);
        toolItem_4.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		content.insert(" & ");       		 //$NON-NLS-1$
        	}
        });
        toolItem_4.setText("&&"); //$NON-NLS-1$

        ToolItem tltmNot = new ToolItem(toolBar, SWT.NONE);
        tltmNot.addSelectionListener(new SelectionAdapter() {
        	@Override
        	public void widgetSelected(SelectionEvent e) {
        		content.insert(" ! "); //$NON-NLS-1$
        	}
        });
        tltmNot.setText("!"); //$NON-NLS-1$

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(10, 10, 61, 17);
        lblNewLabel.setText(Messages.EditPropertyDialog_11);

        description = new Text(container, SWT.BORDER);
        description.setBounds(77, 10, 358, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 262, 425, 17);


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
        button.setText(Messages.EditPropertyDialog_12);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditPropertyDialog_13);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(451, 374);
    }

    @Override
    protected void initValues() {
        if (property == null) {
            property = new ModelCheckingProperty();
        }

        description.setText(property.description);
        content.setText(property.property);
    }

    @Override
    protected void updateValues() {
        property.description = description.getText();
        property.property = content.getText();

    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {

        if (description.getText().equals("") || content.getText().equals("")) { //$NON-NLS-1$ //$NON-NLS-2$
            handleError(Messages.EditPropertyDialog_16);
            return false;
        }

        ModelCheckingPropertySyntax parser = new ModelCheckingPropertySyntax();
        if(parser.propertyParser(content.getText()) == false)
        {
        	handleError(Messages.EditPropertyDialog_17);
            return false;
        }
        // TODO 张华枫：检测输入是否满足语法约束，先不用管变量名是否合法
        // COMPLETED 已经完成 by 张华枫

        return true;
    }

    public ModelCheckingProperty getData() {
        return property;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }
}
