package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.modelchecking;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CodeGenProjectModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.TopLevelModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.AbstractEditDialog;
import cn.edu.tsinghua.thss.tsmart.modeling.modelchecking.ModelCheckingManager;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;


/**
 * 
 * @author huangcd
 */


@SuppressWarnings("rawtypes")
public class ModelCheckingDialog extends AbstractEditDialog {
    private Button btnBDDRadioButton;
    private Button btnSATRadioButton;
    private Combo comboBDDParam;
    private Button btnBdd;
    private Spinner spinnerMaxk;
    private Button btnMaxk;
    private Combo comboCnf;
    private Button btnCnfGenerateMethod;
    private Combo comboBase;
    private Button btnBaseSATDecisionMethod;
    private Combo comboStep;
    private Button btnStepSATDecisionMethod;
    private Combo comboInduction;
    private Button btnInductionFlag;
    private Combo comboLoopFree;
    private Button btnLoopFreeFlag;
    
    private String modelCheckingParamsString = ""; //$NON-NLS-1$

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ModelCheckingDialog(Shell parentShell) {
        super(parentShell, Messages.ModelCheckingDialog_1);
        setTitle(Messages.ModelCheckingDialog_2);

        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();

        if (topModel instanceof LibraryModel) {
            return;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception(Messages.ModelCheckingDialog_3);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return;
        }
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(null);

        Group group = new Group(container, SWT.NONE);
        group.setBounds(10, 0, 330, 355);
        group.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        group.setLayout(null);

        Button editMapping = new Button(group, SWT.NONE);
        editMapping.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Shell shell = Display.getCurrent().getActiveShell();
                EditModelCheckingPropertiesDialog dialog =
                                new EditModelCheckingPropertiesDialog(shell);
                dialog.setBlockOnOpen(true);
                if (Dialog.OK == dialog.open()) {

                }
            }
        });
        editMapping.setBounds(227, 10, 93, 27);
        editMapping.setText(Messages.ModelCheckingDialog_4);
        
        Label lblNewLabel = new Label(group, SWT.NONE);
        lblNewLabel.setBounds(10, 10, 28, 17);
        lblNewLabel.setText(Messages.ModelCheckingDialog_5);
        
        final CTabFolder tabFolder = new CTabFolder(group, SWT.BORDER);
        tabFolder.setBounds(10, 44, 310, 301);
        tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
        
        btnBDDRadioButton = new Button(group, SWT.RADIO);
        btnBDDRadioButton.setSelection(true);
        btnBDDRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tabFolder.setSelection(0);
                tabFolder.getTabList()[0].setEnabled(true);
                tabFolder.getTabList()[1].setEnabled(false);
            }
        });
        btnBDDRadioButton.setBounds(56, 10, 47, 17);
        btnBDDRadioButton.setText("BDD"); //$NON-NLS-1$
        
        btnSATRadioButton = new Button(group, SWT.RADIO);
        btnSATRadioButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                tabFolder.setSelection(1);
                tabFolder.getTabList()[0].setEnabled(false);
                tabFolder.getTabList()[1].setEnabled(true);
            }
        });
        btnSATRadioButton.setBounds(109, 10, 47, 17);
        btnSATRadioButton.setText("SAT"); //$NON-NLS-1$
        
        CTabItem tbtmBDD = new CTabItem(tabFolder, SWT.NONE);
        tbtmBDD.setText(Messages.ModelCheckingDialog_0);
        
        Composite composite = new Composite(tabFolder, SWT.NONE);
        tbtmBDD.setControl(composite);
        
        comboBDDParam = new Combo(composite, SWT.READ_ONLY);
        comboBDDParam.setItems(new String[] {"whole", "separate", "eqsSeparate"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        comboBDDParam.setBounds(68, 7, 88, 25);
        comboBDDParam.setEnabled(false);
        comboBDDParam.select(0);
        
        btnBdd = new Button(composite, SWT.CHECK);
        btnBdd.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnBdd.getSelection()) {
                    comboBDDParam.setEnabled(true);
                }
                else {
                    comboBDDParam.setEnabled(false);
                }
            }
        });
        btnBdd.setBounds(10, 10, 98, 17);
        btnBdd.setText("bdd :"); //$NON-NLS-1$
        
        CTabItem tbtmSAT = new CTabItem(tabFolder, SWT.NONE);
        tbtmSAT.setText(Messages.ModelCheckingDialog_13);
        
        Composite composite_1 = new Composite(tabFolder, SWT.NONE);
        tbtmSAT.setControl(composite_1);
        
        spinnerMaxk = new Spinner(composite_1, SWT.BORDER);
        spinnerMaxk.setMaximum(10000);
        spinnerMaxk.setEnabled(false);
        spinnerMaxk.setSelection(20);
        spinnerMaxk.setBounds(182, 7, 112, 23);
        
        btnMaxk = new Button(composite_1, SWT.CHECK);
        btnMaxk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnMaxk.getSelection()) {
                    spinnerMaxk.setEnabled(true);
                }
                else {
                    spinnerMaxk.setEnabled(false);
                }
            }
        });
        btnMaxk.setBounds(10, 10, 98, 17);
        btnMaxk.setText("max_k"); //$NON-NLS-1$
        
        comboCnf = new Combo(composite_1, SWT.READ_ONLY);
        comboCnf.setEnabled(false);
        comboCnf.setItems(new String[] {"EQU_BBF", "EQU_MBF", "POLARITY_MBF", "EQU_MNNF", "POLARITY_MNNF", "VARDEL_MNNF"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
        comboCnf.setBounds(182, 36, 112, 25);
        comboCnf.select(0);
        
        btnCnfGenerateMethod = new Button(composite_1, SWT.CHECK);
        btnCnfGenerateMethod.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnCnfGenerateMethod.getSelection()) {
                    comboCnf.setEnabled(true);
                }
                else {
                    comboCnf.setEnabled(false);
                }
            }
        });
        btnCnfGenerateMethod.setBounds(10, 42, 137, 17);
        btnCnfGenerateMethod.setText("cnfGenerateMethod"); //$NON-NLS-1$
        
        comboBase = new Combo(composite_1, SWT.READ_ONLY);
        comboBase.setEnabled(false);
        comboBase.setItems(new String[] {"DEFAULT", "TIMEFRAME", "TRANSVAR", "TRANSVAR_TIMEFRAME"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        comboBase.setBounds(182, 70, 112, 25);
        comboBase.select(0);
        
        btnBaseSATDecisionMethod = new Button(composite_1, SWT.CHECK);
        btnBaseSATDecisionMethod.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnBaseSATDecisionMethod.getSelection()) {
                    comboBase.setEnabled(true);
                }
                else {
                    comboBase.setEnabled(false);
                }
            }
        });
        btnBaseSATDecisionMethod.setBounds(10, 74, 164, 17);
        btnBaseSATDecisionMethod.setText("baseSATDecisionMethod"); //$NON-NLS-1$
        
        comboStep = new Combo(composite_1, SWT.READ_ONLY);
        comboStep.setEnabled(false);
        comboStep.setItems(new String[] {"DEFAULT", "TIMEFRAME", "TRANSVAR", "TRANSVAR_TIMEFRAME"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        comboStep.setBounds(182, 105, 112, 25);
        comboStep.select(0);
        
        btnStepSATDecisionMethod = new Button(composite_1, SWT.CHECK);
        btnStepSATDecisionMethod.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnStepSATDecisionMethod.getSelection()) {
                    comboStep.setEnabled(true);
                }
                else {
                    comboStep.setEnabled(false);
                }
            }
        });
        btnStepSATDecisionMethod.setText("stepSATDecisionMethod"); //$NON-NLS-1$
        btnStepSATDecisionMethod.setBounds(10, 108, 164, 17);
        
        comboInduction = new Combo(composite_1, SWT.READ_ONLY);
        comboInduction.setEnabled(false);
        comboInduction.setItems(new String[] {"false", "true"}); //$NON-NLS-1$ //$NON-NLS-2$
        comboInduction.setBounds(182, 136, 112, 25);
        comboInduction.select(0);
        
        btnInductionFlag = new Button(composite_1, SWT.CHECK);
        btnInductionFlag.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnInductionFlag.getSelection()) {
                    comboInduction.setEnabled(true);
                }
                else {
                    comboInduction.setEnabled(false);
                }
            }
        });
        btnInductionFlag.setBounds(10, 139, 98, 17);
        btnInductionFlag.setText("InductionFlag"); //$NON-NLS-1$
        
        comboLoopFree = new Combo(composite_1, SWT.READ_ONLY);
        comboLoopFree.setEnabled(false);
        comboLoopFree.setItems(new String[] {"false", "true"}); //$NON-NLS-1$ //$NON-NLS-2$
        comboLoopFree.setBounds(182, 167, 112, 25);
        comboLoopFree.select(0);
        
        btnLoopFreeFlag = new Button(composite_1, SWT.CHECK);
        btnLoopFreeFlag.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (btnLoopFreeFlag.getSelection()) {
                    comboLoopFree.setEnabled(true);
                }
                else {
                    comboLoopFree.setEnabled(false);
                }
            }
        });
        btnLoopFreeFlag.setBounds(10, 170, 98, 17);
        btnLoopFreeFlag.setText("LoopFreeFlag"); //$NON-NLS-1$
        
        tabFolder.setSelection(0);
        tabFolder.getTabList()[1].setEnabled(false);

        initValues();
        return container;
    }

    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        Button button = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                                        true);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	Shell shell = Display.getCurrent().getActiveShell();
            	final ModelCheckingRunningDialog mcrd = new ModelCheckingRunningDialog(shell);
            	mcrd.notifyStarted();

                new Thread() {
                    public void run() {
                        ModelCheckingManager mcm = new ModelCheckingManager();
                        //boolean isCheckOK = mcm.doChecking(true, "");//$NON-NLS-1$
                        mcm.doChecking(false, modelCheckingParamsString);//$NON-NLS-1$
                        mcrd.notifyFinished();
//                        if (isCheckOK) {
//                            MessageUtil.showMessageDialog(Messages.ValidateModelCheckingAction_3, Messages.ValidateModelCheckingAction_4);
//                        } else {
//                            MessageUtil.ShowErrorDialog(Messages.ValidateModelCheckingAction_5, Messages.ValidateModelCheckingAction_6);
//                        }
                    }
                }.start();

                if (!mcrd.isFinished()) {
                	mcrd.open();
            	}
            }
        });
        button.setText(Messages.ModelCheckingDialog_38);
        Button button_1 =
                        createButton(parent, IDialogConstants.CANCEL_ID,
                                        IDialogConstants.CANCEL_LABEL, false);
        button_1.setText(Messages.ModelCheckingDialog_39);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(352, 442);
    }

    @Override
    protected void initValues() {

    }

    @Override
    protected void updateValues() {
        if (btnBDDRadioButton.getSelection()) {
            modelCheckingParamsString += " -bdd"; //$NON-NLS-1$
            if (btnBdd.getSelection()) {
                modelCheckingParamsString += " -" + comboBDDParam.getText(); //$NON-NLS-1$
            }
        }
        else {
            modelCheckingParamsString += " -bmc"; //$NON-NLS-1$
            if (btnMaxk.getSelection()) {
                modelCheckingParamsString += " " + spinnerMaxk.getText(); //$NON-NLS-1$
            }
            if (btnCnfGenerateMethod.getSelection()) {
                modelCheckingParamsString += " -cnfMethod " + comboCnf.getText();  //$NON-NLS-1$
            }
            if (btnBaseSATDecisionMethod.getSelection()) {
                modelCheckingParamsString += " -baseSATDecisionMethod " + comboBase.getText(); //$NON-NLS-1$
            }
            if (btnStepSATDecisionMethod.getSelection()) {
                modelCheckingParamsString += " -stepSATDecisionMethod " + comboStep.getText(); //$NON-NLS-1$
            }
            if (btnInductionFlag.getSelection()) {
                modelCheckingParamsString += " -inductionFlag " + comboInduction.getText();  //$NON-NLS-1$
            }
            if (btnLoopFreeFlag.getSelection()) {
                modelCheckingParamsString += " -loopfreeFlag " + comboLoopFree.getText(); //$NON-NLS-1$
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean validateUserInput() {

        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return null;
    }
}
