package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.ConnectorPortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.DeleteConnectorPortEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.policies.SelectTransitionEditPolicy;
import cn.edu.tsinghua.thss.tsmart.modeling.ui.dialog.EditConnectorPortDialog;


public class ConnectorPortEditPart extends BaseConnectionEditPart {
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
    }

    @Override
    protected void createEditPolicies() {
        super.createEditPolicies();
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new SelectTransitionEditPolicy());
        installEditPolicy(EditPolicy.CONNECTION_ROLE, new DeleteConnectorPortEditPolicy());
    }

    @Override
    protected void performDoubleClicked() {
        ConnectorPortModel connection = (ConnectorPortModel) getModel();
        connection.attachSource();
        connection.attachTarget();
        Shell shell = Display.getDefault().getActiveShell();
        if (shell != null) {
            EditConnectorPortDialog dialog = new EditConnectorPortDialog(shell, connection);
            dialog.setBlockOnOpen(true);
            dialog.open();
        }
    }

}
