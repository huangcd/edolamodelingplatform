package cn.edu.tsinghua.thss.tsmart.modeling.bip.actions;

import java.io.File;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;


/**
 * Created by Huangcd Date: 11-9-14 Time: ÏÂÎç1:14
 */
public class ExportAction extends SelectionAction {
    public final static String ID = "Export to XML";

    public ExportAction(IWorkbenchPart part) {
        super(part);
        this.setId(ID);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

    @Override
    public void run() {
        Shell shell = Display.getDefault().getActiveShell();
        FileDialog dialog = new FileDialog(shell, SWT.SAVE);
        dialog.setOverwrite(true);
        dialog.setFilterExtensions(new String[] {"*.xml"});
        String path = dialog.open();
        if (path == null) {
            return;
        }
    }
}
