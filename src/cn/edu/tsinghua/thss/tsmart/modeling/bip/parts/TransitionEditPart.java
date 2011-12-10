/**
 * 
 */
package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.gef.EditPolicy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ActionModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.GuardModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TransitionModel;

/**
 * @author: huangcd (huangcd.thu@gmail.com)
 * @time: 2011-6-26 下午01:50:09
 * @project: CereusBip
 * @package: cereusbip.parts
 * @class: TransitionEditPart.java
 * 
 */
public class TransitionEditPart extends BaseConnectionEditPart {
    protected void createEditPolicies() {
        // super.createEditPolicies();
        //installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new SelectTransitionEditPolicy());
        //installEditPolicy(EditPolicy.CONNECTION_ROLE, new DeleteTransitionEditPolicy());
    }

    /**
     * 针对TransitionModel的事件处理
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (ActionModel.ACTION.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (GuardModel.GUARD.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (PortModel.PORT.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
        } else if (TransitionModel.BEND_POINTS.equals(evt.getPropertyName())) {
            refreshVisuals();
        } else if (IModel.REFRESH.equals(evt.getPropertyName())) {
            // label.setText(getModel().toString());
            refreshVisuals();
        }
    }

    // 双击弹出对话框
    protected void performDoubleClicked() {}
}
