package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs.AbstractEditDialog;

public class EditInteractionDialog extends AbstractEditDialog {

    private String interaction;
    private String guard;
    private String upAction;
    private String downAction;
    private Text   interactionText;
    private Text   upActionText;
    private Text   downActionText;
    private Text   guardText;

    public EditInteractionDialog(Shell shell, String interaction, String guard, String upAction,
                    String downAction) {
        super(shell, "Edit Interaction properties");
        this.interaction = interaction;
        this.guard = guard;
        this.upAction = upAction;
        this.downAction = downAction;
    }

    public String getInteraction() {
        return interaction;
    }

    public void setInteraction(String interaction) {
        this.interaction = interaction;
    }

    public String getGuard() {
        return guard;
    }

    public void setGuard(String guard) {
        this.guard = guard;
    }

    public String getUpAction() {
        return upAction;
    }

    public void setUpAction(String upAction) {
        this.upAction = upAction;
    }

    public String getDownAction() {
        return downAction;
    }

    public void setDownAction(String downAction) {
        this.downAction = downAction;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(2, false));

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("iteraction:");

        interactionText = new Text(container, SWT.BORDER);
        interactionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblGuard = new Label(container, SWT.NONE);
        lblGuard.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblGuard.setText("guard:");

        guardText = new Text(container, SWT.BORDER);
        guardText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("up action:");

        upActionText =
                        new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
                                        | SWT.MULTI);
        GridData gd_upActionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_upActionText.heightHint = 176;
        upActionText.setLayoutData(gd_upActionText);

        Label lblDownActions = new Label(container, SWT.NONE);
        lblDownActions.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblDownActions.setText("down actions:");

        downActionText =
                        new Text(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL
                                        | SWT.MULTI);
        GridData gd_downActionText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
        gd_downActionText.heightHint = 183;
        downActionText.setLayoutData(gd_downActionText);

        initValues();
        return container;
    }

    @Override
    protected void initValues() {
        interactionText.setText(interaction);
        guardText.setText(guard);
        upActionText.setText(upAction);
        downActionText.setText(downAction);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(428, 573);
    }

    @Override
    protected void okPressed() {
        if (!validateUserInput()) {
            return;
        }
        setInteraction(interactionText.getText().trim());
        setGuard(guardText.getText().trim());
        setUpAction(upActionText.getText().trim());
        setDownAction(downActionText.getText().trim());
        super.okPressed();
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected void updateValues() {
        // TODO Auto-generated method stub
        
    }
}
