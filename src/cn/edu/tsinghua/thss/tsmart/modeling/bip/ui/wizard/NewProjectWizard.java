package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import org.eclipse.jface.wizard.Wizard;

public class NewProjectWizard extends Wizard {
    private CreateWizardData data;

    public NewProjectWizard(CreateWizardData data) {
        setWindowTitle(Messages.NewProjectWizard_0);
        this.data = data;
    }

    @Override
    public void addPages() {
        addPage(new NewProjectNameWizardPage(data));
        addPage(new NewProjectSelectBaselineWizardPage(data));
    }

    @Override
    public boolean performFinish() {
        return true;
    }
}
