package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
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
public class ImportLibrariesDialog extends AbstractEditDialog {
    private Text                                     textLocation;
    private Button                                   buttonOpen;
    private List                                     listLibraries;
    private Label                                    labelError;
    private HashMap<String, HashMap<String, byte[]>> zipContents;
    private HashMap<String, LibraryModel>            librariesMap;

    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ImportLibrariesDialog(Shell parentShell) {
        super(parentShell, Messages.ImportLibrariesDialog_0);
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

        textLocation = new Text(container, SWT.BORDER);
        FormData fd_textLocation = new FormData();
        fd_textLocation.right = new FormAttachment(0, 408);
        fd_textLocation.top = new FormAttachment(0, 10);
        fd_textLocation.left = new FormAttachment(0, 10);
        textLocation.setLayoutData(fd_textLocation);
        textLocation.setEditable(false);

        buttonOpen = new Button(container, SWT.NONE);
        FormData fd_buttonOpen = new FormData();
        fd_buttonOpen.top = new FormAttachment(0, 8);
        fd_buttonOpen.left = new FormAttachment(0, 425);
        buttonOpen.setLayoutData(fd_buttonOpen);
        buttonOpen.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                chooseFile();
            }
        });
        buttonOpen.setText(Messages.ImportLibrariesDialog_1);

        Group group = new Group(container, SWT.NONE);
        group.setText(Messages.ImportLibrariesDialog_2);
        FormData fd_group = new FormData();
        fd_group.top = new FormAttachment(0, 44);
        fd_group.left = new FormAttachment(0, 7);
        group.setLayoutData(fd_group);

        ScrolledComposite scrolledComposite =
                        new ScrolledComposite(group, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setBounds(3, 17, 460, 295);
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);

        listLibraries = new List(scrolledComposite, SWT.BORDER | SWT.MULTI);
        scrolledComposite.setContent(listLibraries);
        scrolledComposite.setMinSize(listLibraries.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        labelError = new Label(container, SWT.NONE);
        labelError.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        FormData fd_labelError = new FormData();
        fd_labelError.right = new FormAttachment(textLocation, 466);
        fd_labelError.top = new FormAttachment(group, 2);
        fd_labelError.left = new FormAttachment(textLocation, 0, SWT.LEFT);
        labelError.setLayoutData(fd_labelError);

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
        return new Point(486, 473);
    }

    protected void chooseFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setFilterExtensions(new String[] {"*." + GlobalProperties.ZIP_EXTENSION}); //$NON-NLS-1$
        dialog.setText(Messages.ImportLibrariesDialog_4);
        dialog.setText(Messages.ImportLibrariesDialog_5);
        String path = dialog.open();
        if (path != null && !path.isEmpty()) {
            textLocation.setText(path);
            loadLibrariesFromZip();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadLibrariesFromZip() {
        zipContents = new HashMap<String, HashMap<String, byte[]>>();
        librariesMap = new HashMap<String, LibraryModel>();
        try {
            ZipFile zipFile = new ZipFile(new File(textLocation.getText()));
            Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entry = e.nextElement();
                String name = decodeName(entry.getName());
                InputStream in = zipFile.getInputStream(entry);
                if (name.endsWith("/" + LibraryModel.FILE_NAME)) { //$NON-NLS-1$
                    String path = name.substring(0, name.length() - 5);
                    File location = new File(Activator.getPreferenceDirection(), path);
                    LibraryModel model = LibraryModel.load(in, location);
                    librariesMap.put(path, model);
                    listLibraries.add(model.getName());
                    in.close();
                    in = zipFile.getInputStream(entry);
                }
                int index = name.lastIndexOf('/');
                String path = name.substring(0, index);
                String child = name.substring(index + 1);
                HashMap<String, byte[]> contents = zipContents.get(path);
                if (contents == null) {
                    contents = new HashMap<String, byte[]>();
                    zipContents.put(path, contents);
                }
                long size = entry.getSize();
                if (size == -1) {
                    contents.put(child, new byte[0]);
                }
                byte[] content = new byte[(int) size];
                contents.put(child, content);
                in.read(content);
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String decodeName(String name) {
        String[] parts = name.split("/"); //$NON-NLS-1$
        StringBuilder buffer = new StringBuilder();
        for (String part : parts) {
            buffer.append(new String(Base64.decodeBase64(part.replaceAll("_", "/")))); //$NON-NLS-1$ //$NON-NLS-2$
            buffer.append("/"); //$NON-NLS-1$
        }
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }

    @Override
    protected void updateValues() {
        String[] selections = listLibraries.getSelection();
        if (selections.length == 0) {
            return;
        }
        for (String library : selections) {
            LibraryModel model = librariesMap.get(library);
            String name = model.getName();
            File directory = model.getDirectory();
            if (!directory.exists()) {
                directory.mkdirs();
            }
            HashMap<String, byte[]> contents = zipContents.get(name);
            for (Map.Entry<String, byte[]> entry : contents.entrySet()) {
                File file = new File(directory, entry.getKey());
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    out.write(entry.getValue());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void initValues() {}

    @Override
    protected boolean validateUserInput() {
        return true;
    }

    @Override
    protected Label getErrorLabel() {
        return labelError;
    }
}
