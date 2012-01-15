package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.platform.Activator;

@SuppressWarnings("rawtypes")
public class ProjectModel extends TopLevelModel<ProjectModel> {

    private static final long  serialVersionUID = 8772032556059105970L;
    public static final String FILE_NAME        = ".project";
    public static final String LIBRARY_FILE     = ".libpath";

    public static class LibraryEntry {
        File                     location;
        String                   baseline;
        String                   name;
        List<ComponentTypeModel> libs;

        public LibraryEntry(TopLevelModel<LibraryModel> model) {
            init(model);
        }

        public LibraryEntry(File directory) {
            try {
                LibraryModel model = LibraryModel.load(directory);
                init(model);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void init(TopLevelModel<LibraryModel> model) {
            setName(model.getName());
            setBaseline(model.getBaseline());
            setLocation(model.getDirectory());
            libs = new ArrayList<ComponentTypeModel>();
            for (File file : location.listFiles(atomicFilenameFilter)) {
                AtomicTypeModel m = loadAtomicType(file);
                if (m != null) {
                    libs.add(m);
                }
            }
            for (File file : location.listFiles(compoundFilenameFilter)) {
                CompoundTypeModel m = loadCompoundType(file);
                if (m != null) {
                    libs.add(m);
                }
            }
        }

        public File getLocation() {
            return location;
        }

        public void setLocation(File location) {
            this.location = location;
        }

        public String getBaseline() {
            return baseline;
        }

        public void setBaseline(String baseline) {
            this.baseline = baseline;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ComponentTypeModel> getLibs() {
            return libs;
        }
    }

    /** 顶层模型 */
    private CompoundTypeModel            startupModel;
    private transient String             startupModelName;

    /** 项目对应库保存的路径，每个File对应一个文件夹 */
    private List<File>                   libraryLocations;

    /** 使用到的类库 */
    private transient List<LibraryEntry> usedLibraryEntries;

    public ProjectModel(String name, String baseline, String location) {
        this();
        setName(name);
        setLocation(location);
        setBaseline(baseline);
        setDirectory(new File(location));
        startupModel = new CompoundTypeModel().setName("startupModel");
        addChild(startupModel);
        if (!getDirectory().exists()) {
            getDirectory().mkdirs();
        }
        save();
    }

    protected ProjectModel() {
        libraryLocations = new ArrayList<File>();
        usedLibraryEntries = new ArrayList<LibraryEntry>();
    }

    public static ProjectModel load(File directory, IProgressMonitor monitor)
                    throws InvalidPropertiesFormatException, FileNotFoundException, IOException,
                    ClassNotFoundException {
        monitor.beginTask("读取配置文件", 1);
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(new File(directory, FILE_NAME)));
        ProjectModel model = new ProjectModel();
        model.setLocation(directory.getAbsolutePath());
        model.loadProperties(properties);
        monitor.done();
        model.setDirectory(directory);
        monitor.beginTask("读取依赖库位置", 1);
        model.loadLibraryLocations();
        monitor.done();
        model.loadTypesFromLibraries(monitor);
        model.setStartupModelName(properties.getProperty("startup"));
        return model;
    }

    protected void loadProperties(Properties properties) {
        setBaseline(properties.getProperty("baseline"));
        setName(properties.getProperty("name"));
    }

    public void loadTypes(IProgressMonitor monitor) {
        super.loadTypes(monitor);
        for (CompoundTypeModel model : getCompoundTypes()) {
            if (model.getName().equals(startupModelName)) {
                setStartupModel(model);
                BIPEditor.openBIPEditor(model);
            }
        }
    }

    protected void loadLibraryLocations() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(getLibraryLocationsFile()));
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            // 相对路径
            File directory = new File(Activator.getPreferenceDirection(), line);
            // 绝对路径
            if (!directory.exists()) {
                directory = new File(line);
            }
            if (!directory.exists()) {
                MessageUtil.ShowErrorDialog("依赖库“" + directory.getName() + "”不存在", "错误");
                return;
            }
            libraryLocations.add(directory);
            usedLibraryEntries.add(new LibraryEntry(directory));
        }
    }

    public void openStartupModel() {
        BIPEditor.openBIPEditor(startupModel);
    }

    /**
     * 初始化构件库
     * 
     * @param libraries
     */
    public void initLibraries(List<LibraryModel> libraries) {
        libraryLocations = new ArrayList<File>();
        for (TopLevelModel<LibraryModel> library : libraries) {
            libraryLocations.add(library.getDirectory());
            usedLibraryEntries.add(new LibraryEntry(library));
        }

        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        try {
            progressService.busyCursorWhile(new IRunnableWithProgress() {
                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException,
                                InterruptedException {
                    loadTypesFromLibraries(monitor);
                }
            });
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 读取构件库的所有类型
     * 
     * @param monitor
     */
    protected void loadTypesFromLibraries(IProgressMonitor monitor) {
        for (File dir : libraryLocations) {
            if (!dir.exists()) {
                MessageUtil.ShowErrorDialog("依赖库“" + dir.getName() + "”不存在", "错误");
                return;
            }
            File[] atomicFiles = dir.listFiles(atomicFilenameFilter);
            File[] compoundFiles = dir.listFiles(compoundFilenameFilter);
            int total = 3 + atomicFiles.length + compoundFiles.length;
            int count = 0;
            monitor.beginTask("从构件库中读取类型信息", total);
            DataTypeModel.loadTypes(dir);
            monitor.worked(++count);
            PortTypeModel.loadTypes(dir);
            monitor.worked(++count);
            ConnectorTypeModel.loadTypes(dir);
            monitor.worked(++count);
            for (File file : atomicFiles) {
                AtomicTypeModel model = loadAtomicType(file);
                if (model != null) {
                    AtomicTypeModel.addType(model);
                }
                monitor.worked(++count);
            }
            for (File file : compoundFiles) {
                CompoundTypeModel model = loadCompoundType(file);
                if (model != null) {
                    CompoundTypeModel.addType(model);
                }
                monitor.worked(++count);
            }
            monitor.done();
        }
    }

    public CompoundTypeModel getStartupModel() {
        return startupModel;
    }

    public void setStartupModel(CompoundTypeModel startupModel) {
        this.startupModel = startupModel;
    }

    public List<LibraryEntry> getUsedLibraryEntries() {
        return Collections.unmodifiableList(usedLibraryEntries);
    }

    /*
     * 检查基准线中的实体是否有不一致
     */
    public boolean checkValidateBaseline() {
        if (getStartupModel() == null) {
            MessageUtil.ShowErrorDialog("startupModel加载失败", "错误");
            return true;
        }
        return getStartupModel().checkValidateBaseline();
    }

    /*
     * 删除模型中的无效实体（这些实体在基准线中不存在）
     */
    public void cleanEntityNames() {
        getStartupModel().cleanEntityNames();
    }

    @Override
    public ProjectModel addChild(IType child) {
        super.addChild(child);
        if (child instanceof DataTypeModel) {
            DataTypeModel.addType(child.getName());
        }
        if (child instanceof PortTypeModel) {
            PortTypeModel.addType((PortTypeModel) child);
        }
        if (child instanceof ConnectorTypeModel) {
            ConnectorTypeModel.addType((ConnectorTypeModel) child);
        }
        return this;
    }

    @Override
    public boolean removeChild(IType child) {
        if (child instanceof DataTypeModel) {
            DataTypeModel.removeType(child.getName());
        }
        if (child instanceof PortTypeModel) {
            PortTypeModel.removeType(child.getName());
        }
        if (child instanceof ConnectorTypeModel) {
            ConnectorTypeModel.removeType(child.getName());
        }
        return super.removeChild(child);
    }

    @Override
    public void save() {
        IProgressService progressService = PlatformUI.getWorkbench().getProgressService();
        try {
            progressService.runInUI(PlatformUI.getWorkbench().getProgressService(),
                            new IRunnableWithProgress() {
                                @Override
                                public void run(IProgressMonitor monitor)
                                                throws InvocationTargetException,
                                                InterruptedException {
                                    monitor.beginTask("保存项目", 2 + getChildren().size());
                                    int count = 0;
                                    saveBaseInformation();
                                    monitor.worked(++count);
                                    saveLibraries();
                                    monitor.worked(++count);
                                    saveTypes(monitor, count);
                                    monitor.done();
                                }
                            }, ResourcesPlugin.getWorkspace().getRoot());
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void reloadComponent() {
        // do nothing
    }

    private File getLibraryLocationsFile() {
        return new File(getDirectory(), LIBRARY_FILE);
    }

    protected void saveBaseInformation() {
        Properties properties = new Properties();
        saveProperties(properties);
        try {
            properties.storeToXML(new FileOutputStream(new File(getDirectory(), FILE_NAME)),
                            "project setting file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveProperties(Properties properties) {
        properties.put("name", getName());
        properties.put("baseline", getBaseline());
        if (getStartupModel() != null) {
            properties.put("startup", getStartupModel().getName());
        } else {
            properties.put("startup", "startupModel");
        }
    }

    private void saveLibraries() {
        // 保存项目依赖库信息
        try {
            PrintStream out = new PrintStream(getLibraryLocationsFile());
            for (File library : libraryLocations) {
                File preferenceDirection = Activator.getPreferenceDirection();
                String relativeLocation = "";
                while (library.compareTo(preferenceDirection) != 0) {
                    relativeLocation = library.getName() + "/" + relativeLocation;
                    library = library.getParentFile();
                }
                out.println(relativeLocation);
                out.flush();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStartupModelName() {
        return startupModelName;
    }

    public void setStartupModelName(String startupModelName) {
        this.startupModelName = startupModelName;
    }
}
