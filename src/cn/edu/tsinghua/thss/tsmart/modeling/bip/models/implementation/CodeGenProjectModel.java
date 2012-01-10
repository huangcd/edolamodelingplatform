package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class CodeGenProjectModel extends ProjectModel {

    private static final long serialVersionUID = -74792855277971601L;
    private String            hardwareEntity   = "Ó²¼þ";
    private String            softwareEntity   = "Èí¼þ";
    private String            tickEntity       = "Ê±ÖÓ";
    private String            ioEntity         = "IO";

    public CodeGenProjectModel(String name, String baseline, String location) {
        super(name, baseline, location);
    }

    protected CodeGenProjectModel() {}

    public static CodeGenProjectModel load(File directory) throws InvalidPropertiesFormatException,
                    FileNotFoundException, IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(new File(directory, FILE_NAME)));
        CodeGenProjectModel model = new CodeGenProjectModel();
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
        setHardwareEntity(checkIsNull(properties.getProperty("hardwareEntity")));
        setSoftwareEntity(checkIsNull(properties.getProperty("softwareEntity")));
        setTickEntity(checkIsNull(properties.getProperty("tickEntity")));
        setIoEntity(checkIsNull(properties.getProperty("ioEntity")));
    }

    protected void saveProperties(Properties properties) {
        properties.put("name", getName());
        properties.put("baseline", getBaseline());
        properties.put("location", getLocation());
        properties.put("startup", getStartupModel().getName());
        properties.put("hardwareEntity", assertNotNull(getHardwareEntity()));
        properties.put("softwareEntity", assertNotNull(getSoftwareEntity()));
        properties.put("tickEntity", assertNotNull(getTickEntity()));
        properties.put("ioEntity", assertNotNull(getIoEntity()));
    }

    public String checkIsNull(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        return str;
    }

    private String assertNotNull(String str) {
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
}
