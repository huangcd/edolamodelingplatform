package cn.edu.tsinghua.thss.tsmart.platform.language;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class SwitchChineseAction extends Action implements ISelectionListener, IWorkbenchAction {

    public static final String ID = SwitchChineseAction.class.getCanonicalName();

    public SwitchChineseAction() {
        setId(ID);
        setText(Messages.SwitchChineseAction_0);
        setToolTipText(Messages.SwitchChineseAction_1);
    }

    @Override
    public void dispose() {}

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {}

    @Override
    public void run() {
        LanguageSwitcher.changeLocale("zh_CN"); //$NON-NLS-1$
    }

}
