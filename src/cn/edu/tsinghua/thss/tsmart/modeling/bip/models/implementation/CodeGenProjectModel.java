package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class CodeGenProjectModel extends ProjectModel {

    private static final long   serialVersionUID            = -74792855277971601L;
    private static final String DEVICE_DEFINITION_FILE_NAME = "device_definition.xml";
    private static final String MAPPING_FILE_NAME           = "mapping.xml";
    private static final String MC_PROPERTIES_FILE_NAME     = "mc_properties.xml";

    @Element(required = false)
    private String              hardwareEntity              = "";
    @Element(required = false)
    private String              softwareEntity              = "";
    @Element(required = false)
    private String              tickEntity                  = "";
    @Element(required = false)
    private String              ioEntity                    = "";

    public CodeGenProjectModel(String name, String baseline, String location) {
        super(name, baseline, location);
    }

    protected CodeGenProjectModel() {}

    public static CodeGenProjectModel load(File directory, IProgressMonitor monitor) throws InvalidPropertiesFormatException,
                    FileNotFoundException, IOException, ClassNotFoundException {
        monitor.beginTask("读取配置文件", 1);
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(new File(directory, FILE_NAME)));
        CodeGenProjectModel model = new CodeGenProjectModel();
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
        setHardwareEntity(ensureNotNull(properties.getProperty("hardwareEntity")));
        setSoftwareEntity(ensureNotNull(properties.getProperty("softwareEntity")));
        setTickEntity(ensureNotNull(properties.getProperty("tickEntity")));
        setIoEntity(ensureNotNull(properties.getProperty("ioEntity")));
    }

    protected void saveProperties(Properties properties) {
        properties.put("name", getName());
        properties.put("baseline", getBaseline());
        properties.put("location", getLocation());
        if (getStartupModel() != null) {
            properties.put("startup", getStartupModel().getName());
        } else {
            properties.put("startup", "startupModel");
        }
        properties.put("hardwareEntity", ensureNotNull(getHardwareEntity()));
        properties.put("softwareEntity", ensureNotNull(getSoftwareEntity()));
        properties.put("tickEntity", ensureNotNull(getTickEntity()));
        properties.put("ioEntity", ensureNotNull(getIoEntity()));
    }

    public String ensureNotNull(String str) {
        if (str == null) {
            return "";
        }
        return str;
    }

    public String getHardwareEntity() {
        return hardwareEntity;
    }

    public void setHardwareEntity(String hardwareEntity) {
        this.hardwareEntity = hardwareEntity;
    }

    public String getSoftwareEntity() {
        return softwareEntity;
    }

    public void setSoftwareEntity(String softwareEntity) {
        this.softwareEntity = softwareEntity;
    }

    public String getTickEntity() {
        return tickEntity;
    }

    public void setTickEntity(String tickEntity) {
        this.tickEntity = tickEntity;
    }

    public String getIoEntity() {
        return ioEntity;
    }

    public void setIoEntity(String ioEntity) {
        this.ioEntity = ioEntity;
    }

    public String getDeviceDefinitionFilePath() {
        String loc = this.getLocation();
        loc = loc + "/" + DEVICE_DEFINITION_FILE_NAME;
        return loc;
    }

    public String getMappingFilePath() {
        String loc = this.getLocation();
        loc = loc + "/" + MAPPING_FILE_NAME;
        return loc;
    }

    public String getMCPropertiesFilePath() {
        String loc = this.getLocation();
        loc = loc + "/" + MC_PROPERTIES_FILE_NAME;
        return loc;
    }

    /**
     * 当某个value为null的时候，返回之，否则返回某个默认值。
     * 
     * @param value
     * @param defaultValue
     * @return
     */
    private static <T> T ensureValue(T value, T defaultValue) {
        return (value == null) ? defaultValue : value;
    }
}
