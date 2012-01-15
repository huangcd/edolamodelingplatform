package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.actions.RetargetAction;

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

/**
 * Created by Huangcd
 * 
 * Date: 11-9-14
 * 
 * Time: ÏÂÎç1:10
 */
public class ExportRetargetAction extends RetargetAction {
    public ExportRetargetAction() {
        super(ExportAction.ID, Messages.ExportRetargetAction_0);
        setToolTipText(Messages.ExportRetargetAction_1);
        ImageDescriptor descriptor = Activator.getImageDescriptor("icons/xml.ico"); //$NON-NLS-1$
        setImageDescriptor(descriptor);
        setDisabledImageDescriptor(descriptor);
    }
}
