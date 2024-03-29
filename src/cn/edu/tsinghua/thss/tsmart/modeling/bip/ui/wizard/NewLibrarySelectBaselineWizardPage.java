package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.wb.swt.ResourceManager;

import cn.edu.tsinghua.thss.tsmart.baseline.BaselineDataAccessor;
import cn.edu.tsinghua.thss.tsmart.baseline.model.refined.Baseline;

public class NewLibrarySelectBaselineWizardPage extends WizardPage {

    private CreateWizardData               data;
    private List                     listBaselines;
    private java.util.List<Baseline> baselines;

    /**
     * Create the wizard.
     */
    public NewLibrarySelectBaselineWizardPage(CreateWizardData data) {
        super(Messages.NewLibrarySelectBaselineWizardPage_0);
        setImageDescriptor(ResourceManager.getPluginImageDescriptor("EdolaModelingPlatform", //$NON-NLS-1$
                        "icons/product_wiz.gif")); //$NON-NLS-1$
        setPageComplete(false);
        setTitle(Messages.NewLibrarySelectBaselineWizardPage_3);
        setDescription(Messages.NewLibrarySelectBaselineWizardPage_4);
        this.data = data;
    }

    /**
     * Create contents of the wizard.
     * 
     * @param parent
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);

        setControl(container);
        container.setLayout(new FillLayout(SWT.HORIZONTAL));

        SashForm sashForm = new SashForm(container, SWT.NONE);

        Group groupBaseline = new Group(sashForm, SWT.NONE);
        groupBaseline.setText(Messages.NewLibrarySelectBaselineWizardPage_5);
        groupBaseline.setToolTipText(""); //$NON-NLS-1$
        groupBaseline.setLayout(new FillLayout(SWT.HORIZONTAL));

        ScrolledComposite scrolledCompositeBaseline =
                        new ScrolledComposite(groupBaseline, SWT.BORDER | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        scrolledCompositeBaseline.setExpandHorizontal(true);
        scrolledCompositeBaseline.setExpandVertical(true);

        listBaselines = new List(scrolledCompositeBaseline, SWT.BORDER);
        listBaselines.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectBaseline();
            }
        });
        scrolledCompositeBaseline.setContent(listBaselines);
        scrolledCompositeBaseline.setMinSize(listBaselines.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sashForm.setWeights(new int[] {1});
        initValues();
    }

    private void initValues() {
        BaselineDataAccessor bda = new BaselineDataAccessor();
        baselines = bda.getBaselines();
        for (Baseline baseline : baselines) {
            listBaselines.add(baseline.getName());
        }
    }

    protected void selectBaseline() {
        int index = listBaselines.getSelectionIndex();
        if (index >= 0 && index < listBaselines.getItemCount()) {
            String baseline = listBaselines.getItem(index);
            data.setBaseline(baseline);
            setPageComplete(isPageComplete());
        }
    }

    @Override
    public boolean isPageComplete() {
        if (listBaselines.getSelectionIndex() >= 0) {
            return true;
        }
        return false;
    }
}
