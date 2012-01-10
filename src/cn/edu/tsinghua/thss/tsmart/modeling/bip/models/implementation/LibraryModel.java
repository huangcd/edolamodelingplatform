package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings({"rawtypes", "unchecked"})
/**
 * XXX LibraryModel的目录位置是限定在项目内部的，不能随便定义，因此对外应该提供树状结构，用户只能在指定的路径里面选择或者增加目录，但不能把库保存在外部(@huangcd)
 * @author Huangcd
 *
 */
public class LibraryModel extends TopLevelModel<LibraryModel> {

    private static final long  serialVersionUID = -4592528530988131833L;
    public static final String FILE_NAME        = ".lib";

    public LibraryModel(String name, String baseline, String location) {
        this();
        setName(name);
        setLocation(location);
        setBaseline(baseline);
        setDirectory(new File(location));
        if (!getDirectory().exists()) {
            getDirectory().mkdirs();
        }
        save();
    }

    private LibraryModel() {
        // 清空全局缓存
        DataTypeModel.clearTypes();
        getDataTypeModels().addAll(DataTypeModel.getAllRegisterTypes());
        PortTypeModel.clearTypes();
        getPortTypes().addAll(PortTypeModel.getAllRegisterTypes());
        ConnectorTypeModel.clearTypes();
        getConnectorTypes().addAll(ConnectorTypeModel.getAllRegisterTypes());
        AtomicTypeModel.clearTypes();
        getAtomicTypes().addAll(AtomicTypeModel.getAllRegisterTypes());
        CompoundTypeModel.clearTypes();
        getCompoundTypes().addAll(CompoundTypeModel.getAllRegisterTypes());
    }

    public static LibraryModel load(File directory) throws InvalidPropertiesFormatException,
                    FileNotFoundException, IOException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.loadFromXML(new FileInputStream(new File(directory, FILE_NAME)));
        LibraryModel model = new LibraryModel();
        model.setLocation(directory.getAbsolutePath());
        model.setBaseline(properties.getProperty("baseline"));
        model.setName(properties.getProperty("name"));
        model.setDirectory(directory);
        return model;
    }

    /**
     * 重新将当前类库的组件对象载入到静态存储区里面去
     */
    public void reloadComponent() {
        AtomicTypeModel.clearTypes();
        CompoundTypeModel.clearTypes();
        for (AtomicTypeModel model : getAtomicTypes()) {
            AtomicTypeModel.addType(model);
        }
        for (CompoundTypeModel model : getCompoundTypes()) {
            CompoundTypeModel.addType(model);
        }
    }

    public void save() {
        saveBaseInformation();
        saveTypes();
    }

    private void saveBaseInformation() {
        try {
            Properties properties = new Properties();
            properties.put("name", getName());
            properties.put("baseline", getBaseline());
            properties.put("location", getLocation());
            properties.storeToXML(new FileOutputStream(new File(getDirectory(), FILE_NAME)),
                            "library setting file");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public LibraryModel addChild(IType child) {
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
        if (child instanceof AtomicTypeModel) {
            AtomicTypeModel.addType((AtomicTypeModel) child);
        }
        if (child instanceof CompoundTypeModel) {
            CompoundTypeModel.addType((CompoundTypeModel) child);
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
        if (child instanceof AtomicTypeModel) {
            AtomicTypeModel.removeType(child.getName());
        }
        if (child instanceof CompoundTypeModel) {
            CompoundTypeModel.removeType(child.getName());
        }
        return super.removeChild(child);
    }
}
