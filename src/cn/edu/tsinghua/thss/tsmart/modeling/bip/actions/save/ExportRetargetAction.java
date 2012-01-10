package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions.save;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.actions.RetargetAction;

import cn.edu.tsinghua.thss.tsmart.platform.Activator;

/**
 * Created by Huangcd
 * 
 * Date: 11-9-14
 * 
 * Time: обнГ1:10
 */
public class ExportRetargetAction extends RetargetAction {
    public ExportRetargetAction() {
        super(ExportAction.ID, "Do Export");
        setToolTipText("Export model as Edola");
        ImageDescriptor descriptor = Activator.getImageDescriptor("icons/xml.ico");
        setImageDescriptor(descriptor);
        setDisabledImageDescriptor(descriptor);
    }
}
