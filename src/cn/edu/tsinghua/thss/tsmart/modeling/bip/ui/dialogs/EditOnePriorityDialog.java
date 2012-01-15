package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.HashSet;

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
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PriorityModel;


/**
 * 
 * @author huangcd
 */
@SuppressWarnings("rawtypes")
public class EditOnePriorityDialog extends AbstractEditDialog {

    private CompoundTypeModel                ctm;

    private Text                             lessP;
    private Label                            errorLabel;
    private Text                             greatP;
    private PriorityModel<CompoundTypeModel> pm = null;
    private Text                             name;


    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EditOnePriorityDialog(Shell parentShell, CompoundTypeModel ctm,
                    PriorityModel<CompoundTypeModel> pm) {
        super(parentShell, Messages.EditOnePriorityDialog_0);
        setTitle(Messages.EditOnePriorityDialog_1);
        this.ctm = ctm;
        if (pm != null) {
            this.pm = new PriorityModel<CompoundTypeModel>();
            this.pm.setName(pm.getName());
            this.pm.setLeftPort(pm.getLeftPort());
            this.pm.setRightPort(pm.getRightPort());
        }
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

        Label label = new Label(container, SWT.NONE);
        label.setBounds(10, 57, 60, 17);
        label.setText(Messages.EditOnePriorityDialog_2);

        lessP = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        lessP.setBounds(76, 54, 243, 23);

        errorLabel = new Label(container, SWT.NONE);
        errorLabel.setBounds(10, 139, 395, 23);

        Label lblNewLabel_3 = new Label(container, SWT.NONE);
        lblNewLabel_3.setBounds(10, 105, 60, 17);
        lblNewLabel_3.setText(Messages.EditOnePriorityDialog_3);

        greatP = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        greatP.setBounds(76, 102, 243, 23);

        Button selectConnectorLess = new Button(container, SWT.NONE);
        selectConnectorLess.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ConnectorSelectionDialog dialog = new ConnectorSelectionDialog(getShell(), ctm);
                if (Window.OK == dialog.open()) {
                    lessP.setText(dialog.getData().getName());
                    pm.setLeftPort(dialog.getData());
                }
            }
        });
        selectConnectorLess.setBounds(325, 50, 80, 27);
        selectConnectorLess.setText(Messages.EditOnePriorityDialog_4);

        Button selectConnectorGreat = new Button(container, SWT.NONE);
        selectConnectorGreat.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                ConnectorSelectionDialog dialog = new ConnectorSelectionDialog(getShell(), ctm);
                if (Window.OK == dialog.open()) {
                    greatP.setText(dialog.getData().getName());
                    pm.setRightPort(dialog.getData());
                }
            }
        });
        selectConnectorGreat.setBounds(325, 102, 80, 27);
        selectConnectorGreat.setText(Messages.EditOnePriorityDialog_5);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(10, 16, 61, 17);
        lblNewLabel.setText(Messages.EditOnePriorityDialog_6);

        name = new Text(container, SWT.BORDER);
        name.setBounds(76, 10, 243, 23);


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
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        Button button =
                        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                                        true);
        button.setText(Messages.EditOnePriorityDialog_7);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.EditOnePriorityDialog_8);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(421, 257);
    }

    @Override
    protected void initValues() {
        if (pm == null) {
            pm = new PriorityModel<CompoundTypeModel>();
            pm.setName(""); //$NON-NLS-1$
        } else
            name.setEnabled(false);

        name.setText(pm.getName());
        lessP.setText(getSaveString(pm.getLeftPort()));
        greatP.setText(getSaveString(pm.getRightPort()));

    }


    @Override
    protected void updateValues() {
        pm.setName(name.getText());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {
        if (name.getText().equals("")) { //$NON-NLS-1$
            handleError(Messages.EditOnePriorityDialog_11);
            return false;
        }

        if (name.getEnabled()) {
            HashSet<String> names = new HashSet<String>();
            for (IInstance model : ctm.getChildren()) {
                names.add(model.getName());
            }
            if (names.contains(name.getText())) {
                handleError(Messages.EditOnePriorityDialog_12);
                return false;
            }
        }
        
        if (pm.getLeftPort() == null || pm.getRightPort() == null) {
            handleError(Messages.EditOnePriorityDialog_13);
            return false;
        }

        if (pm.getLeftPort().equals(pm.getRightPort())) {
            handleError(Messages.EditOnePriorityDialog_14);
            return false;
        }

        return true;
    }

    public PriorityModel<CompoundTypeModel> getData() {
        return pm;
    }

    @Override
    protected Label getErrorLabel() {
        errorLabel.setForeground(ColorConstants.red);
        return errorLabel;
    }

    private String getSaveString(IModel m) {
        if (m == null)
            return ""; //$NON-NLS-1$
        else
            return m.getName();

    }
}
