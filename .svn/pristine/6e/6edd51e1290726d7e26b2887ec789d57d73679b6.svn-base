package cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog;

import java.util.HashMap;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityAreaModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.PriorityModel;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.util.MessageBoxUtil;


public class EditPriorityDialog extends AbstractEditDialog {
    private List<PortModel>             ports;
    private HashMap<Integer, PortModel> portIdxMap;
    private Text                        conditionText;
    private Combo                       leftCombo;
    private Combo                       rightCombo;
    private PriorityAreaModel           _parent;
    private PriorityModel               owner;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     * @wbp.parser.constructor
     */
    public EditPriorityDialog(Shell parentShell, PriorityModel owner, List<PortModel> ports,
                    PriorityAreaModel parent) {
        super(parentShell, "edit priority");
        this.ports = ports;
        this._parent = parent;
        this.owner = owner;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) container.getLayout();
        gridLayout.numColumns = 5;

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setAlignment(SWT.CENTER);
        lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        lblNewLabel.setText("condition");
        new Label(container, SWT.NONE);

        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_1.setText("left port");
        new Label(container, SWT.NONE);

        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1));
        lblNewLabel_2.setText("right port");

        conditionText = new Text(container, SWT.BORDER);
        conditionText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label = new Label(container, SWT.NONE);
        label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setText("\u2192");

        leftCombo = new Combo(container, SWT.READ_ONLY);
        leftCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        Label label_1 = new Label(container, SWT.NONE);
        label_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label_1.setText("<");

        rightCombo = new Combo(container, SWT.READ_ONLY);
        rightCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

        portIdxMap = new HashMap<Integer, PortModel>();
        for (int i = 0; i < ports.size(); i++) {
            PortModel port = ports.get(i);
            portIdxMap.put(i, port);
            leftCombo.add(port.getName());
            rightCombo.add(port.getName());
        }
        leftCombo.select(0);
        rightCombo.select(ports.size() - 1);

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
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(430, 169);
    }

    protected boolean validateUserInput() {
        if (nameExistsInParent(conditionText.getText())) return false;
        if (samePortOnBothSide()) return false;
        return true;
    }

    private boolean samePortOnBothSide() {
        if (leftCombo.getSelectionIndex() == rightCombo.getSelectionIndex()) {
            MessageBoxUtil.ShowErrorMessage(getShell(), "Port error", "same port on both side");
            return true;
        }
        return false;
    }

    public boolean nameExistsInParent(String name) {
        for (PriorityModel child : _parent.getChildren()) {
            if (child.equals(owner)) {
                continue;
            }
            if (child.getName().equals(name)) {
                MessageBoxUtil.ShowErrorMessage("Name conflict error",
                                "Port name exists in this atomic");
                return true;
            }
        }
        return false;
    }

    @Override
    protected void okPressed() {
        if (_parent != null) {
            if (!validateUserInput()) {
                return;
            }
        }
        owner.setCondition(conditionText.getText());
        owner.setLeft(portIdxMap.get(leftCombo.getSelectionIndex()));
        owner.setRight(portIdxMap.get(rightCombo.getSelectionIndex()));
        owner.setName(owner.getLeft().getName() + "LT" + owner.getRight().getName());
        super.okPressed();
    }

    protected void initValues() {
        if (owner.getCondition() == null)
            conditionText.setText("true");
        else
            conditionText.setText(owner.getCondition());

        if (owner.getLeft() != null && getIndex(owner.getLeft()) >= 0)
            leftCombo.select(getIndex(owner.getLeft()));
        if (owner.getRight() != null && getIndex(owner.getRight()) >= 0)
            rightCombo.select(getIndex(owner.getRight()));
    }

    private int getIndex(PortModel portModel) {
        for (int i = 0; i < portIdxMap.size(); i++)
            if (portModel == portIdxMap.get(i)) return i;
        return -1;
    }
}
