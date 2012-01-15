package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.LibraryModel;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings("rawtypes")
public class ExportLibrariesDialog extends AbstractEditDialog {
    private static final byte[]           BYTES = new byte[1024 * 1024];
    private Text                          textLocation;
    private List                          listLibraries;
    private Button                        buttonOpen;
    private Label                         labelError;
    private HashMap<String, LibraryModel> libraryMap;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ExportLibrariesDialog(Shell parentShell) {
        super(parentShell, Messages.ExportLibrariesDialog_0);
    }

    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new FormLayout());

        Group group = new Group(container, SWT.NONE);
        FormData fd_group = new FormData();
        fd_group.bottom = new FormAttachment(0, 200);
        fd_group.right = new FormAttachment(0, 331);
        fd_group.top = new FormAttachment(0, 13);
        fd_group.left = new FormAttachment(0, 10);
        group.setLayoutData(fd_group);
        group.setText(Messages.ExportLibrariesDialog_1);
        group.setLayout(new FillLayout(SWT.HORIZONTAL));

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listLibraries = new List(scrolledComposite, SWT.BORDER | SWT.MULTI);
        scrolledComposite.setContent(listLibraries);
        scrolledComposite.setMinSize(listLibraries.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        textLocation = new Text(container, SWT.BORDER);
        textLocation.setEditable(false);
        FormData fd_textLocation = new FormData();
        fd_textLocation.left = new FormAttachment(0, 10);
        textLocation.setLayoutData(fd_textLocation);

        buttonOpen = new Button(container, SWT.NONE);
        buttonOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseFile();
            }
        });
        fd_textLocation.right = new FormAttachment(buttonOpen, -6);
        fd_textLocation.top = new FormAttachment(0, 219);
        FormData fd_buttonOpen = new FormData();
        fd_buttonOpen.top = new FormAttachment(group, 15);
        fd_buttonOpen.right = new FormAttachment(group, 0, SWT.RIGHT);
        buttonOpen.setLayoutData(fd_buttonOpen);
        buttonOpen.setText(Messages.ExportLibrariesDialog_2);

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        FormData fd_labelError = new FormData();
        fd_labelError.left = new FormAttachment(group, 0, SWT.LEFT);
        fd_labelError.bottom = new FormAttachment(100, -10);
        fd_labelError.right = new FormAttachment(group, -260, SWT.RIGHT);
        labelError.setLayoutData(fd_labelError);

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
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
        createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(347, 366);
    }

    @Override
    protected void updateValues() {
        try {
            String[] selections = listLibraries.getSelection();
            // 没有选择任何构件库
            if (selections.length == 0) {
                return;
            }
            File file = new File(textLocation.getText());
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
            for (String library : selections) {
                save(out, libraryMap.get(library).getDirectory());
            }
            out.finish();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(ZipOutputStream out, File file) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                save(out, f);
            }
        } else {
            File preferenceDirection = Activator.getPreferenceDirection();
            String relativeLocation = ""; //$NON-NLS-1$
            File tempFile = file;
            while (tempFile.compareTo(preferenceDirection) != 0) {
                if (relativeLocation.isEmpty()) {
                    relativeLocation =
                                    Base64.encodeBase64String(tempFile.getName().getBytes("GBK")) //$NON-NLS-1$
                                                    .replaceAll("/", "_"); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    relativeLocation =
                                    Base64.encodeBase64String(tempFile.getName().getBytes("GBK")) //$NON-NLS-1$
                                                    .replaceAll("/", "_") + "/" + relativeLocation; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                tempFile = tempFile.getParentFile();
            }
            ZipEntry entry = new ZipEntry(relativeLocation);
            out.putNextEntry(entry);
            FileInputStream in = new FileInputStream(file);
            int count = 0;
            synchronized (BYTES) {
                while ((count = in.read(BYTES)) != -1) {
                    out.write(BYTES, 0, count);
                }
            }
            out.flush();
            out.closeEntry();
        }
    }

    @Override
    protected void initValues() {
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
                        libraryMap.put(name, model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (String name : libraryMap.keySet()) {
            listLibraries.add(name);
        }
    }

    protected void chooseFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] {"*." + GlobalProperties.ZIP_EXTENSION}); //$NON-NLS-1$
        dialog.setText(Messages.ExportLibrariesDialog_12);
        dialog.setText(Messages.ExportLibrariesDialog_13);
        String path = dialog.open();
        if (path != null && !path.isEmpty()) {
            textLocation.setText(path);
        }
    }

    @Override
    protected boolean validateUserInput() {
        String path = textLocation.getText();
        if (path.isEmpty() && listLibraries.getSelectionCount() != 0) {
            getErrorLabel().setText(Messages.ExportLibrariesDialog_14);
            return false;
        }
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return labelError;
    }
}
