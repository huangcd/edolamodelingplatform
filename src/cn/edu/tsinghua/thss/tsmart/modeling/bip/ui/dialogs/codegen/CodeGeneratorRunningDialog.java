package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.codegen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

public class CodeGeneratorRunningDialog extends Dialog {

	private boolean finished = false;
	
	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 */
	public CodeGeneratorRunningDialog(Shell parent) {
		super(parent, SWT.None);
		setText(Messages.CodeGeneratorRunningDialog_0);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed() && !finished) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		shell.dispose();
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 80);
		shell.setText(Messages.CodeGeneratorRunningDialog_1);
		// center this dialog
	    // Display display = Display.getDefault();
	    // Monitor primary = display.getPrimaryMonitor();
	    // Rectangle bounds = primary.getBounds();
	    Rectangle bounds = getParent().getBounds();
	    Rectangle rect = shell.getBounds();
	    int x = bounds.x + (bounds.width - rect.width) / 2;
	    int y = bounds.y + (bounds.height - rect.height) / 2;
	    shell.setLocation(x, y);
	    
		ProgressBar progressBar = new ProgressBar(shell, SWT.SMOOTH | SWT.INDETERMINATE);
		progressBar.setSelection(100);
		progressBar.setBounds(52, 46, 341, 17);
		
		Label label = new Label(shell, SWT.NONE);
		label.setBounds(52, 20, 137, 17);
		label.setText(Messages.CodeGeneratorRunningDialog_2);

	}

	public void notifyFinished() {
		finished = true;
	}
	
	public void notifyStarted() {
		finished = false;
	}
	
	public boolean isFinished() {
		return finished;
	}

}
