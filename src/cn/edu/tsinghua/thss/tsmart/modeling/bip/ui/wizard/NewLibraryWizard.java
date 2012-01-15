package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import org.eclipse.jface.wizard.Wizard;

public class NewLibraryWizard extends Wizard {
    private CreateWizardData data;

    public NewLibraryWizard(CreateWizardData data) {
        setWindowTitle(Messages.NewLibraryWizard_0);
        this.data = data;
    }

    @Override
    public void addPages() {
        addPage(new NewLibraryNameWizardPage(data));
        addPage(new NewLibrarySelectBaselineWizardPage(data));
    }

    @Override
    public boolean performFinish() {
        return true;
    }
}
