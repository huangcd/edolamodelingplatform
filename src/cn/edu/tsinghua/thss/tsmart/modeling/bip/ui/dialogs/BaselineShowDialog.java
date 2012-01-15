package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import cn.edu.tsinghua.thss.tsmart.baseline.model.FileBaseline;
import cn.edu.tsinghua.thss.tsmart.baseline.model.GroupContent;
import cn.edu.tsinghua.thss.tsmart.baseline.model.NodeContent;
import cn.edu.tsinghua.thss.tsmart.baseline.model.NodeRequirement;
import cn.edu.tsinghua.thss.tsmart.baseline.model.NodeRule;

public class BaselineShowDialog extends AbstractEditDialog {

    private String   filepath;

    private Tree     tree;
    private Table    ruletable;
    private Table    requirementtable;

    public BaselineShowDialog(Shell parentShell, String path) {
        super(parentShell, Messages.BaselineShowDialog_0);
        setShellStyle(SWT.MAX | SWT.RESIZE);
        filepath = path;
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

        final SashForm hsashForm = new SashForm(container, SWT.HORIZONTAL);
        hsashForm.SASH_WIDTH = 1;

        hsashForm.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_GREEN));

        // Create the buttons and their event handlers
        tree = new Tree(hsashForm, SWT.V_SCROLL);
        tree.setLinesVisible(true);
        tree.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                AddDetailedInfo2Tables();
            }
        });
        tree.setLayout(new FillLayout());

        final Composite tabledock = new Composite(hsashForm, SWT.PUSH);
        tabledock.setLayout(new FillLayout(SWT.VERTICAL));

        final SashForm vsashForm = new SashForm(tabledock, SWT.VERTICAL);
        vsashForm.SASH_WIDTH = 1;
        vsashForm.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_GREEN));

        ruletable = new Table(vsashForm, SWT.BORDER | SWT.FULL_SELECTION);
        ruletable.setHeaderVisible(true);
        ruletable.setLinesVisible(true);

        TableColumn col_entitysubject = new TableColumn(ruletable, SWT.CENTER);
        col_entitysubject.setWidth(87);
        col_entitysubject.setText(Messages.BaselineShowDialog_1);

        TableColumn col_relation = new TableColumn(ruletable, SWT.CENTER);
        col_relation.setWidth(88);
        col_relation.setText(Messages.BaselineShowDialog_2);

        TableColumn col_quantifier = new TableColumn(ruletable, SWT.CENTER);
        col_quantifier.setWidth(79);
        col_quantifier.setText(Messages.BaselineShowDialog_3);

        TableColumn col_number = new TableColumn(ruletable, SWT.CENTER);
        col_number.setWidth(80);
        col_number.setText(Messages.BaselineShowDialog_4);

        TableColumn col_entityguest = new TableColumn(ruletable, SWT.CENTER);
        col_entityguest.setWidth(88);
        col_entityguest.setText(Messages.BaselineShowDialog_5);

        requirementtable = new Table(vsashForm, SWT.BORDER | SWT.FULL_SELECTION);
        requirementtable.setHeaderVisible(true);
        requirementtable.setLinesVisible(true);

        TableColumn tblclmnFormaldesc = new TableColumn(requirementtable, SWT.CENTER);
        tblclmnFormaldesc.setWidth(234);
        tblclmnFormaldesc.setText(Messages.BaselineShowDialog_6);

        TableColumn tblclmnRelatedentity = new TableColumn(requirementtable, SWT.CENTER);
        tblclmnRelatedentity.setWidth(195);
        tblclmnRelatedentity.setText(Messages.BaselineShowDialog_7);


        hsashForm.setWeights(new int[] {2, 2});

        if (AddBaseline2Tree(tree, filepath)) {
            if (tree.getItemCount() > 0) {
                tree.getItem(0).setExpanded(true);
            }

            tree.pack();
        }

        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.CANCEL_ID, Messages.BaselineShowDialog_8, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(930, 614);
    }

    private boolean AddBaseline2Tree(Tree tree, String filepath) {
        Serializer serializer = new Persister();
        File source = new File(filepath);
        FileBaseline baseline = null;
        try {
            baseline = serializer.read(FileBaseline.class, source);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (baseline == null) return false;

        TreeItem item = new TreeItem(tree, SWT.NONE);
        item.setText(baseline.getName());
        item.setData(baseline);

        // Show the whole tree;
        List<GroupContent> gplist = baseline.getGroupStructure().FindSonsContent("NULL");
        if (gplist.size() > 0) {
            AddNodes(item, gplist, baseline, true);
        }


        if (item.getItemCount() > 0) {
            item.setExpanded(true);
        } else {
            item.dispose();
        }

        return true;
    }

    @SuppressWarnings("rawtypes")
    private void AddNodes(TreeItem item, List list, FileBaseline bsline, boolean bGroup) {

        TreeItem subItem;
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (bGroup) {
                GroupContent gp_ctnt = (GroupContent) it.next();
                subItem = new TreeItem(item, SWT.NONE);
                subItem.setText(gp_ctnt.GetContentDescription().replace("\r", "").replace("\n", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                subItem.setData(gp_ctnt);
                List<GroupContent> gplist =
                                bsline.getGroupStructure().FindSonsContent(gp_ctnt.GetContentID());
                if (gplist.size() > 0) {
                    AddNodes(subItem, gplist, bsline, true);
                } else {
                    List<NodeContent> ndlist =
                                    bsline.getLeafNodes().FindSonsContent(gp_ctnt.GetContentID());
                    if (ndlist.size() > 0) AddNodes(subItem, ndlist, bsline, false);
                }
                if (subItem.getItemCount() > 0) {
                    subItem.setExpanded(true);
                } else {
                    subItem.dispose();
                }
            } else {
                NodeContent node_ctnt = (NodeContent) it.next();
                subItem = new TreeItem(item, SWT.NONE);
                subItem.setText(node_ctnt.GetNodeNatDesc().replace("\r", "").replace("\n", "")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                subItem.setData(node_ctnt);

            }
        }
    }

    @SuppressWarnings("rawtypes")
    private void AddDetailedInfo2Tables() {
        TreeItem[] items = tree.getSelection();
        if (items.length > 0) {
            ruletable.removeAll();
            requirementtable.removeAll();
            String clsname = items[0].getData().getClass().getName();
            if (!clsname.equals("cn.edu.tsinghua.thss.tsmart.baseline.model.NodeContent")) return; //$NON-NLS-1$

            NodeContent nd_ctnt = (NodeContent) items[0].getData();
            Iterator it = nd_ctnt.GetNodeRueList().iterator();
            while (it.hasNext()) {
                String[] colums = new String[5];
                NodeRule rule = (NodeRule) it.next();
                colums[0] = rule.GetEntitySubject();
                colums[1] = rule.GetEntityRelation();
                colums[2] = rule.GetEntityQuantifier();
                colums[3] = rule.GetEntityCardinality();
                colums[4] = rule.GetEntityGuest();

                TableItem item1 = new TableItem(ruletable, SWT.NONE);
                item1.setText(colums);
            }



            it = nd_ctnt.GetNodeRequirementList().iterator();
            while (it.hasNext()) {
                NodeRequirement requirement = (NodeRequirement) it.next();

                String[] colums = new String[5];
                colums[0] = requirement.GetFormalDescription();
                colums[1] = ""; //$NON-NLS-1$
                for (int i = 0; i < requirement.GetNodeEntityList().size(); i++) {
                    colums[1] += requirement.GetNodeEntityList().get(i) + " "; //$NON-NLS-1$
                }

                TableItem item2 = new TableItem(requirementtable, SWT.NONE);
                item2.setText(colums);
            }

        }
    }

    @Override
    protected void updateValues() {

    }

    @Override
    protected void initValues() {

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
