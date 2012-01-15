package cn.edu.tsinghua.thss.tsmart.platform.language;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class SwitchEnglishAction extends Action implements ISelectionListener, IWorkbenchAction{

    public static final String     ID = SwitchEnglishAction.class.getCanonicalName();

    public SwitchEnglishAction() {
        setId(ID);
        setText(Messages.SwitchEnglishAction_0);
        setToolTipText(Messages.SwitchEnglishAction_1);
    }
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void run() {
		LanguageSwitcher.changeLocale("en_US"); //$NON-NLS-1$
	}

}
