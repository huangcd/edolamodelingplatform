package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.edu.tsinghua.thss.tsmart.modeling.validation.Rule;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class BaselineOptionDialog extends AbstractEditDialog {
    private Tree                 tree;
    private BaselineOptionDialog self; // 指向自身，方便关闭窗口

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public BaselineOptionDialog(Shell parentShell) {
        super(parentShell, Messages.BaselineOptionDialog_0);
        setTitle(Messages.BaselineOptionDialog_1);
        self = this;
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

        Group group = new Group(container, SWT.NONE);
        group.setText(Messages.BaselineOptionDialog_2);
        group.setBounds(10, 10, 534, 595);

        tree = new Tree(group, SWT.BORDER | SWT.CHECK | SWT.MULTI);
        tree.setBounds(10, 22, 524, 573);

        Button button = new Button(container, SWT.NONE);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem[] items = tree.getItems();

                List<Rule> rules = GlobalProperties.getInstance().getRules();
                for (int i = 0; i < rules.size(); i++) {
                    Rule rule = rules.get(i);
                    // 查看是否该rule被选中
                    String ruleDipr = rule.getDescription();
                    if (ruleDipr.isEmpty() == false) {
                        for (int j = 0; j < items.length; j++) {
                            if (items[j].getText().equals(ruleDipr)) {
                                rule.setNeedCheckOnline(items[j].getChecked());
                            }
                        }
                    }
                    // 把rule检查选项结果写会rules中
                    rules.set(i, rule);
                }
                GlobalProperties.getInstance().setRules(rules);
                self.close();
            }
        });
        button.setBounds(339, 611, 80, 27);
        button.setText(Messages.BaselineOptionDialog_3);

        Button button_1 = new Button(container, SWT.CANCEL);
        button_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                self.close();
            }
        });
        button_1.setBounds(445, 611, 80, 27);
        button_1.setText(Messages.BaselineOptionDialog_4);
        // TreeItem item = new TreeItem(tree, SWT.CHECK );
        // item.setText("test");
        // tree.getSelection();

        // Button btnCheckButton = new Button(group, SWT.CHECK);
        // btnCheckButton.setBounds(10, 21, 514, 17);
        // btnCheckButton.setText("Check Button");
        //
        // Button button = new Button(group, SWT.CHECK);
        // button.setText("Check Button");
        // button.setBounds(10, 42, 514, 17);

        List<Rule> rules = GlobalProperties.getInstance().getRules();
        for (Iterator<Rule> iterator = rules.iterator(); iterator.hasNext();) {
            Rule rule = iterator.next();
            String ruleDipr = rule.getDescription();
            if (ruleDipr.isEmpty() == false) {
                TreeItem treeItem = new TreeItem(tree, SWT.CHECK);
                treeItem.setText(ruleDipr);
                treeItem.setChecked(rule.getNeedCheckOnline());
            }
        }

        // Button btnCheckButton = new Button(scrolledComposite, SWT.CHECK);
        // btnCheckButton.setBounds(10, 20, 514, 17);
        // btnCheckButton.setText("Check Button");

        initValues();
        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {}

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(560, 700);
    }

    @Override
    protected void updateValues() {}

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
