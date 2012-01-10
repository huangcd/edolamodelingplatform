package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.ArrayList;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Entity;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class EntitySelectionFromLibDialog extends AbstractEditDialog {
    private ArrayList<Entity> entityNames = new ArrayList<Entity>();
    private Label             labelErr;
    private List              list;
    private Text              text;
    private String            entity = "";

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EntitySelectionFromLibDialog(Shell parentShell) {
        super(parentShell, "选择参数");
        setTitle("\u9009\u62E9\u5B9E\u4F53");
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

        list = new List(container, SWT.BORDER);
        list.setBounds(10, 62, 337, 234);

        labelErr = new Label(container, SWT.NONE);
        labelErr.setBounds(10, 302, 337, 17);

        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setBounds(10, 10, 132, 17);
        lblNewLabel.setText("\u8F93\u5165\u5173\u952E\u5B57\uFF0C\u4EE5\u8FC7\u6EE4\u5B9E\u4F53");

        text = new Text(container, SWT.BORDER);
        text.setBounds(10, 33, 337, 23);

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
        createButton(parent, IDialogConstants.OK_ID, "\u786E\u5B9A", true);
        createButton(parent, IDialogConstants.CANCEL_ID, "\u53D6\u6D88", false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(363, 415);
    }

    @Override
    protected void updateValues() {
        String[] selections = list.getSelection();

        entity = selections[0];
    }

    @Override
    protected void initValues() {
        for (Entity e : GlobalProperties.getInstance().getEntities()) {
            entityNames.add(e);
            list.add(e.getName());
        }
    }

    @Override
    protected Label getErrorLabel() {
        labelErr.setForeground(ColorConstants.red);
        return labelErr;
    }

    public String getSelectEntity() {
        return entity;
    }

    @Override
    protected boolean validateUserInput() {
        String[] selections = list.getSelection();
        if (selections == null || selections.length == 0) {
            handleError("请选择实体。");
            return false;
        }

        return true;
    }

}
