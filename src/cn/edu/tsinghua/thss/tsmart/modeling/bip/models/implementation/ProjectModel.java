package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
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

    public static ProjectModel load(File directory) throws InvalidPropertiesFormatException,
                    FileNotFoundException, IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(new File(directory, FILE_NAME)));
        ProjectModel model = new ProjectModel();
        model.setLocation(directory.getAbsolutePath());
        model.loadProperties(properties);
        model.setDirectory(directory);
        model.loadLibraryLocations();
        model.loadTypesFromLibraries();
        model.setStartupModelName(properties.getProperty("startup"));
        return model;
    }

    protected void loadProperties(Properties properties) {
        setBaseline(properties.getProperty("baseline"));
        setName(properties.getProperty("name"));
    }

    public void loadTypes() {
        super.loadTypes();
        for (CompoundTypeModel model : getCompoundTypes()) {
            if (model.getName().equals(startupModelName)) {
                startupModel = model;
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
        loadTypesFromLibraries();
    }

    /**
     * 读取构件库的所有类型
     */
    protected void loadTypesFromLibraries() {
        for (File dir : libraryLocations) {
            DataTypeModel.loadTypes(dir);
            PortTypeModel.loadTypes(dir);
            ConnectorTypeModel.loadTypes(dir);
            File[] atomicFiles = dir.listFiles(atomicFilenameFilter);
            for (File file : atomicFiles) {
                AtomicTypeModel model = loadAtomicType(file);
                if (model != null) {
                    AtomicTypeModel.addType(model);
                }
            }
            File[] compoundFiles = dir.listFiles(compoundFilenameFilter);
            for (File file : compoundFiles) {
                CompoundTypeModel model = loadCompoundType(file);
                if (model != null) {
                    CompoundTypeModel.addType(model);
                }
            }
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
        saveBaseInformation();
        saveLibraries();
        saveTypes();
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
        properties.put("location", getLocation());
        properties.put("startup", startupModel.getName());
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
