package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.wizard;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

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
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

public class NewProjectSelectBaselineWizardPage extends WizardPage {

    private CreateWizardData               data;
    private List                           listLibraries;
    private List                           listBaselines;
    private java.util.List<Baseline>       baselines;
    private Map<String, ArrayList<String>> baselineMap;
    private Map<String, LibraryModel>      libraryMap;

    /**
     * Create the wizard.
     */
    public NewProjectSelectBaselineWizardPage(CreateWizardData data) {
        super("select baseline");
        setImageDescriptor(ResourceManager.getPluginImageDescriptor("EdolaModelingPlatform",
                        "icons/product_wiz.gif"));
        setPageComplete(false);
        setTitle("\u9009\u62E9\u57FA\u51C6\u7EBF");
        setDescription("\u9009\u62E9\u6307\u5BFC\u9879\u76EE\u7684\u57FA\u51C6\u7EBF\u53CA\u6784\u4EF6\u5E93");
        this.data = data;
        loadLibraries();
    }

    private void loadLibraries() {
        baselineMap = new HashMap<String, ArrayList<String>>();
        libraryMap = new HashMap<String, LibraryModel>();
        File root = Activator.getPreferenceDirection();
        Stack<File> stack = new Stack<File>();
        stack.add(root);
        while (!stack.isEmpty()) {
            File dir = stack.pop();
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    stack.push(file);
                } else if (file.getName().equals(LibraryModel.FILE_NAME)) {
                    try {
                        LibraryModel model = LibraryModel.load(file.getParentFile());
                        String name = model.getName();
                        String baseline = model.getBaseline();
                        libraryMap.put(name, model);
                        ArrayList<String> list = baselineMap.get(baseline);
                        if (list == null) {
                            list = new ArrayList<String>();
                            baselineMap.put(baseline, list);
                        }
                        list.add(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        data.setLibraryMap(libraryMap);
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
        groupBaseline.setText("\u9009\u62E9\u57FA\u51C6\u7EBF");
        groupBaseline.setToolTipText("");
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

        Group groupLibrary = new Group(sashForm, SWT.NONE);
        groupLibrary.setText("\u9009\u62E9\u6784\u4EF6\u5E93");
        groupLibrary.setLayout(new FillLayout(SWT.HORIZONTAL));

        ScrolledComposite scrolledCompositeLibrary =
                        new ScrolledComposite(groupLibrary, SWT.BORDER | SWT.H_SCROLL
                                        | SWT.V_SCROLL);
        scrolledCompositeLibrary.setExpandHorizontal(true);
        scrolledCompositeLibrary.setExpandVertical(true);

        listLibraries = new List(scrolledCompositeLibrary, SWT.BORDER | SWT.MULTI);
        listLibraries.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectLibraries();
            }
        });
        scrolledCompositeLibrary.setContent(listLibraries);
        scrolledCompositeLibrary.setMinSize(listLibraries.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        sashForm.setWeights(new int[] {1, 1});
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
            listLibraries.removeAll();
            ArrayList<String> libraries = baselineMap.get(baseline);
            if (libraries != null) {
                for (String libraryName : libraries) {
                    listLibraries.add(libraryName);
                }
            }
            setPageComplete(isPageComplete());
        }
    }

    protected void selectLibraries() {
        data.setLibs(Arrays.asList(listLibraries.getSelection()));
    }

    @Override
    public boolean isPageComplete() {
        if (listBaselines.getSelectionIndex() >= 0/* && listLibraries.getSelectionIndex() >= 0 */) {
            return true;
        }
        return false;
    }
}
