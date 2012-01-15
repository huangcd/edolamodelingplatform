package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.baseline.EntitySelectDialogInterface;
import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Entity;

@SuppressWarnings("rawtypes")
public class EntitySelectionDialog extends AbstractEditDialog
                implements
                    EntitySelectDialogInterface {
    private ArrayList<String>     entityNames;
    private Label                 labelErr;
    private List                  list;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public EntitySelectionDialog(Shell parentShell, ArrayList<String> entityNames) {
        super(parentShell, Messages.EntitySelectionDialog_0);
        setTitle(Messages.EntitySelectionDialog_1);
        this.entityNames = new ArrayList<String>(entityNames);
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FormLayout());

        list = new List(container, SWT.BORDER);
        FormData fd_list = new FormData();
        fd_list.left = new FormAttachment(0, 10);
        fd_list.top = new FormAttachment(0, 10);
        list.setLayoutData(fd_list);

        labelErr = new Label(container, SWT.NONE);
        fd_list.bottom = new FormAttachment(labelErr, -6);
        FormData fd_labelErr = new FormData();
        fd_labelErr.top = new FormAttachment(0, 302);
        fd_labelErr.right = new FormAttachment(0, 553);
        fd_labelErr.left = new FormAttachment(0, 10);
        labelErr.setLayoutData(fd_labelErr);

        Button newEntity = new Button(container, SWT.NONE);
        newEntity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = new Shell();
                EntitySelectionFromLibDialog dialog =
                                new EntitySelectionFromLibDialog(shell);
                dialog.open();

                if (Arrays.asList(list.getItems()).indexOf(dialog.getSelectEntity()) == -1)
                    list.add(dialog.getSelectEntity());

            }
        });
        fd_list.right = new FormAttachment(newEntity, -13);
        FormData fd_newEntity = new FormData();
        fd_newEntity.left = new FormAttachment(0, 469);
        fd_newEntity.right = new FormAttachment(100, -10);
        fd_newEntity.top = new FormAttachment(list, -1, SWT.TOP);
        newEntity.setLayoutData(fd_newEntity);
        newEntity.setText(Messages.EntitySelectionDialog_2);

        Button deleteEntity = new Button(container, SWT.NONE);
        deleteEntity.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String[] selections = list.getSelection();
                if (selections == null || selections.length == 0) {
                    handleError(Messages.EntitySelectionDialog_3);
                    return;
                }
                for (String selection : selections) {
                    list.remove(selection);
                }
            }
        });
        FormData fd_deleteEntity = new FormData();
        fd_deleteEntity.right = new FormAttachment(labelErr, 0, SWT.RIGHT);
        deleteEntity.setLayoutData(fd_deleteEntity);
        deleteEntity.setText(Messages.EntitySelectionDialog_4);

        Button deleteAll = new Button(container, SWT.NONE);
        deleteAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                list.removeAll();
            }
        });
        fd_deleteEntity.bottom = new FormAttachment(deleteAll, -6);
        fd_deleteEntity.left = new FormAttachment(deleteAll, 0, SWT.LEFT);
        FormData fd_deleteAll = new FormData();
        fd_deleteAll.top = new FormAttachment(0, 76);
        fd_deleteAll.right = new FormAttachment(labelErr, 0, SWT.RIGHT);
        deleteAll.setLayoutData(fd_deleteAll);
        deleteAll.setText(Messages.EntitySelectionDialog_5);

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
        createButton(parent, IDialogConstants.OK_ID, Messages.EntitySelectionDialog_6, true);
        createButton(parent, IDialogConstants.CANCEL_ID, Messages.EntitySelectionDialog_7, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(569, 415);
    }

    @Override
    protected void updateValues() {}

    @Override
    protected void initValues() {
        for (String entity : entityNames) {
            list.add(entity);
        }
    }

    @Override
    protected Label getErrorLabel() {
        labelErr.setForeground(ColorConstants.red);
        return labelErr;
    }

    @Override
    protected boolean validateUserInput() {
        entityNames.clear();
        for (String entity : list.getItems()) {
            entityNames.add(entity);
        }

        return true;
    }

    public ArrayList<String> getEntityNames() {
        return entityNames;
    }

    @Override
    public void updateSelected(Entity entity) {
        list.add(entity.getName());
    }
}
