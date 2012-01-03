package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;

public class CompoundTypeManageDialog extends AbstractEditDialog {
    private Label      labelError;
    private List       listTypes;
    private StyledText styledTextPreview;

    public CompoundTypeManageDialog(Shell parentShell) {
        super(parentShell, "\u590D\u5408\u6784\u4EF6\u5E93\u7BA1\u7406");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 395, 717, 17);

        SashForm sashForm = new SashForm(container, SWT.NONE);
        sashForm.setBounds(10, 10, 717, 379);

        Group groupView = new Group(sashForm, SWT.NONE);
        groupView.setText("\u9009\u62E9");
        groupView.setLayout(new FormLayout());

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(groupView, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        FormData fd_scrolledComposite = new FormData();
        fd_scrolledComposite.top = new FormAttachment(0, 10);
        fd_scrolledComposite.left = new FormAttachment(0, 10);
        fd_scrolledComposite.right = new FormAttachment(100, -10);
        scrolledComposite.setLayoutData(fd_scrolledComposite);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listTypes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selections = listTypes.getSelection();
                if (selections == null || selections.length == 0) {
                    return;
                }
                styledTextPreview.setText(CompoundTypeModel.getModelByName(selections[0])
                                .exportToBip());
            }
        });
        scrolledComposite.setContent(listTypes);
        scrolledComposite.setMinSize(listTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        Button buttonDelete = new Button(groupView, SWT.NONE);
        fd_scrolledComposite.bottom = new FormAttachment(100, -40);
        FormData fd_buttonDelete = new FormData();
        fd_buttonDelete.top = new FormAttachment(scrolledComposite, 6);
        fd_buttonDelete.left = new FormAttachment(scrolledComposite, 0, SWT.LEFT);
        fd_buttonDelete.right = new FormAttachment(100, -10);
        buttonDelete.setLayoutData(fd_buttonDelete);
        buttonDelete.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {}
        });
        buttonDelete.setText("\u5220\u9664\u7EC4\u4EF6");

        Group group = new Group(sashForm, SWT.NONE);
        group.setText("\u9884\u89C8");
        group.setLayout(new FillLayout(SWT.HORIZONTAL));

        styledTextPreview =
                        new StyledText(group, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        sashForm.setWeights(new int[] {287, 427});

        initValues();
        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(743, 511);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {
        for (String name : CompoundTypeModel.getTypes()) {
            listTypes.add(name);
        }
    }

    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return labelError;
    }
}
