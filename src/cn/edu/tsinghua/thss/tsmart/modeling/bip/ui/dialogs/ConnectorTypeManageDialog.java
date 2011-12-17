package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel;

public class ConnectorTypeManageDialog extends AbstractEditDialog {
    private Label                   labelError;
    private List                    listPortTypes;
    private HashMap<String, String> portMap;

    public ConnectorTypeManageDialog(Shell parentShell) {
        super(parentShell, "连接子类型管理");
        portMap = new HashMap<String, String>();
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(10, 10, 379, 195);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listPortTypes = new List(scrolledComposite, SWT.BORDER | SWT.V_SCROLL);
        listPortTypes.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (listPortTypes.getSelection().length > 0) {
                    getErrorLabel().setForeground(ColorConstants.black);
                    getErrorLabel().setText(portMap.get(listPortTypes.getSelection()[0]));
                }
            }
        });
        scrolledComposite.setContent(listPortTypes);
        scrolledComposite.setMinSize(listPortTypes.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        labelError.setBounds(10, 244, 379, 17);
        
        Button buttonCreate = new Button(container, SWT.NONE);
        buttonCreate.setBounds(10, 211, 120, 27);
        buttonCreate.setText("\u589E\u52A0\u8FDE\u63A5\u5B50");
        
        Button buttonDelete = new Button(container, SWT.NONE);
        buttonDelete.setBounds(139, 211, 120, 27);
        buttonDelete.setText("\u5220\u9664\u8FDE\u63A5\u5B50");
        
        Button buttonPort = new Button(container, SWT.NONE);
        buttonPort.setBounds(269, 211, 120, 27);
        buttonPort.setText("\u7BA1\u7406\u7AEF\u53E3\u7C7B\u578B");

        initValues();
        return container;
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    @Override
    protected Point getInitialSize() {
        return new Point(405, 356);
    }

    @Override
    protected void updateValues() {
        // FIXME 删除再增加一个同名的port会有bug，应该避免
        HashSet<String> types = new HashSet<String>(Arrays.asList(listPortTypes.getItems()));
        HashSet<String> temp = new HashSet<String>();
        for (String type : PortTypeModel.getTypes()) {
            if (!types.contains(type)) {
                temp.add(type);
            }
        }
        for (String type : temp) {
            PortTypeModel.removeType(type);
        }
        for (String type : types) {
            if (!PortTypeModel.getTypes().contains(type)) {
                PortTypeModel.addType(type, portMap.get(type));
            }
        }
    }

    @Override
    protected void initValues() {
        for (String type : PortTypeModel.getTypeNamesAsArray()) {
            listPortTypes.add(type);
            portMap.put(type, PortTypeModel.getPortTypeModel(type).getArgumentAsString());
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
