package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public class AddLibrarySettingDialog extends AbstractEditDialog {

    private final static MessageFormat titleFormat;
    static {
        String msg = "\u4E3A\u6784\u4EF6{0}\u6DFB\u52A0\u5907\u6CE8\u4FE1\u606F";
        titleFormat = new MessageFormat(msg);
    }
    private StyledText                 styledTextComment;
    private IModel                     model;
    private Text                       textName;
    private String                     name;

    /**
     * Create the dialog.
     * 
     * @param shell
     */
    public AddLibrarySettingDialog(Shell shell, IModel model) {
        super(shell, titleFormat.format(new Object[] {model.getName()}));
        this.model = model;
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));

        SashForm sashForm = new SashForm(container, SWT.VERTICAL);

        Group group_1 = new Group(sashForm, SWT.NONE);
        group_1.setText("\u8BBE\u7F6E\u6784\u4EF6\u540D");
        group_1.setLayout(new FillLayout(SWT.HORIZONTAL));

        textName = new Text(group_1, SWT.BORDER);

        Group group = new Group(sashForm, SWT.NONE);
        group.setText("\u8BBE\u7F6E\u6784\u4EF6\u5907\u6CE8\u4FE1\u606F");
        group.setLayout(new FillLayout(SWT.HORIZONTAL));

        styledTextComment = new StyledText(group, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        sashForm.setWeights(new int[] {54, 294});

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
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(475, 436);
    }

    @Override
    protected void updateValues() {
        name = textName.getText();
        model.setComment(styledTextComment.getText());
    }

    @Override
    protected void initValues() {
        String comment = model.getComment();
        if (comment != null) {
            styledTextComment.setText(comment);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }
}