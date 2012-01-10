package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class TopLevelModel<Model extends TopLevelModel>
                extends BaseTypeModel<Model, IInstance, ITopModel> implements ITopModel<Model> {

    private static final long          serialVersionUID = -5707343197446452791L;
    public static final FilenameFilter atomicFilenameFilter;
    public static final FilenameFilter compoundFilenameFilter;

    static {
        atomicFilenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("." + GlobalProperties.ATOMIC_EXTENSION)) {
                    return true;
                }
                return false;
            }
        };
        compoundFilenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith("." + GlobalProperties.COMPOUND_EXTENSION)) {
                    return true;
                }
                return false;
            }
        };
    }

    private List<DataTypeModel>        dataTypes;
    private List<PortTypeModel>        portTypes;
    private List<ConnectorTypeModel>   connectorTypes;
    private List<AtomicTypeModel>      atomicTypes;
    private List<CompoundTypeModel>    compoundTypes;
    /** 保存当前打开在编辑的模型 */
    private List<ComponentTypeModel>   openModels;
    private String                     baseline;
    private String                     location;
    private transient File             directory;

    public TopLevelModel() {
        dataTypes = new ArrayList<DataTypeModel>();
        portTypes = new ArrayList<PortTypeModel>();
        connectorTypes = new ArrayList<ConnectorTypeModel>();
        atomicTypes = new ArrayList<AtomicTypeModel>();
        compoundTypes = new ArrayList<CompoundTypeModel>();
        openModels = new ArrayList<ComponentTypeModel>();
    }

    // TODO 好好设计一下保存文件的格式(@huangcd)
    public abstract void save() throws IOException;

    public abstract void reloadComponent();

    public void loadTypes() {
        DataTypeModel.loadTypes(getDirectory());
        PortTypeModel.loadTypes(getDirectory());
        ConnectorTypeModel.loadTypes(getDirectory());
        HashMap<String, ComponentTypeModel> map = new HashMap<String, ComponentTypeModel>();
        // 读取atomic
        File[] atomicFiles = getDirectory().listFiles(atomicFilenameFilter);
        for (File file : atomicFiles) {
            AtomicTypeModel model = loadAtomicType(file);
            if (model != null) {
                addChild(model);
                map.put(model.getName(), model);
            }
        }
        // 读取compound
        File[] compoundFiles = getDirectory().listFiles(compoundFilenameFilter);
        for (File file : compoundFiles) {
            CompoundTypeModel model = loadCompoundType(file);
            if (model != null) {
                addChild(model);
                map.put(model.getName(), model);
            }
        }
        // TODO 还原打开的窗口
        // try {
        // BufferedReader in = new BufferedReader(new FileReader(new File(directory, ".open")));
        // String line;
        // while ((line = in.readLine()) != null) {
        // if (map.containsKey(line.trim())) {
        // ComponentTypeModel model = map.get(line.trim());
        // addOpenModel(model);
        // BIPEditor.openBIPEditor(model);
        // }
        // }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // 暂时先打开所有窗口
        for (ComponentTypeModel model : map.values()) {
            BIPEditor.openBIPEditor(model);
        }
    }

    public List<DataTypeModel> getDataTypeModels() {
        return dataTypes;
    }

    public void setDataTypeModels(List<DataTypeModel> dataTypeModels) {
        this.dataTypes = dataTypeModels;
    }

    public List<PortTypeModel> getPortTypes() {
        return portTypes;
    }

    public void setPortTypes(List<PortTypeModel> portTypes) {
        this.portTypes = portTypes;
    }

    public List<ConnectorTypeModel> getConnectorTypes() {
        return connectorTypes;
    }

    public void setConnectorTypes(List<ConnectorTypeModel> connectorTypes) {
        this.connectorTypes = connectorTypes;
    }

    public List<AtomicTypeModel> getAtomicTypes() {
        return atomicTypes;
    }

    public void setAtomicTypes(List<AtomicTypeModel> atomicTypes) {
        this.atomicTypes = atomicTypes;
    }

    public List<CompoundTypeModel> getCompoundTypes() {
        return compoundTypes;
    }

    public void setCompoundTypes(List<CompoundTypeModel> compoundTypes) {
        this.compoundTypes = compoundTypes;
    }

    @Override
    public List<IType> getChildren() {
        List<IType> types = new ArrayList<IType>();
        types.addAll(getDataTypeModels());
        types.addAll(getPortTypes());
        types.addAll(getConnectorTypes());
        types.addAll(getAtomicTypes());
        types.addAll(getCompoundTypes());
        return types;
    }

    public void addDataType(String type) {
        addChild(DataTypeModel.addType(type));
    }

    private final static Pattern NAME_PREFIX = Pattern.compile("^(.*)_(\\d+)$");

    @Override
    public Model addChild(IType child) {
        ensureUniqueName(child);
        child.setParent(this);
        if (child instanceof DataTypeModel) {
            dataTypes.add((DataTypeModel) child);
        }
        if (child instanceof PortTypeModel) {
            portTypes.add((PortTypeModel) child);
        }
        if (child instanceof ConnectorTypeModel) {
            connectorTypes.add((ConnectorTypeModel) child);
        }
        if (child instanceof AtomicTypeModel) {
            atomicTypes.add((AtomicTypeModel) child);
        }
        if (child instanceof CompoundTypeModel) {
            compoundTypes.add((CompoundTypeModel) child);
        }
        return (Model) this;
    }

    public boolean removeChildByName(String name) {
        for (IType child : getChildren()) {
            if (child.getName().equals(name)) {
                if (child instanceof ComponentTypeModel) {
                    BIPEditor.closeEditor((ComponentTypeModel) child);
                }
                removeChild(child);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeChild(IType child) {
        if (child instanceof DataTypeModel) {
            return dataTypes.remove((DataTypeModel) child);
        }
        if (child instanceof PortTypeModel) {
            return portTypes.remove((PortTypeModel) child);
        }
        if (child instanceof ConnectorTypeModel) {
            return connectorTypes.remove((ConnectorTypeModel) child);
        }
        if (child instanceof AtomicTypeModel) {
            openModels.remove(child);
            return atomicTypes.remove((AtomicTypeModel) child);
        }
        if (child instanceof CompoundTypeModel) {
            openModels.remove(child);
            return compoundTypes.remove((CompoundTypeModel) child);
        }
        return false;
    }

    @Override
    public IInstance createInstance() {
        return null;
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    public String getBaseline() {
        return baseline;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<ComponentTypeModel> getOpenModels() {
        return openModels;
    }

    public void setOpenModels(List<ComponentTypeModel> openModels) {
        this.openModels = openModels;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    /**
     * 将打开的页面对应的模型添加到openModels里面，方便再次打开项目/构件库的时候恢复现场
     * 
     * @param model
     */
    public void addOpenModel(ComponentTypeModel model) {
        if ((atomicTypes.contains(model) || compoundTypes.contains(model))
                        && !openModels.contains(model)) {
            this.openModels.add(model);
        }
    }

    /**
     * 将关闭的页面对应的模型从openModels里面删除
     * 
     * @param model
     */
    public void removeOpenModel(ComponentTypeModel model) {
        this.openModels.remove(model);
    }

    protected void saveTypes() {
        DataTypeModel.saveTypes(getDirectory());
        PortTypeModel.saveTypes(getDirectory());
        ConnectorTypeModel.saveTypes(getDirectory());
        saveAtomics();
        saveCompounds();
        saveOpenModels();
    }

    private void saveOpenModels() {
        try {
            PrintStream out = new PrintStream(new File(getDirectory(), ".open"));
            for (ComponentTypeModel model : getOpenModels()) {
                out.println(model.getName());
                out.flush();
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void saveCompounds() {
        for (CompoundTypeModel model : getCompoundTypes()) {
            String fileName = model.getName() + "." + GlobalProperties.COMPOUND_EXTENSION;
            writeObject(model, fileName);
        }
    }

    private void saveAtomics() {
        for (AtomicTypeModel model : getAtomicTypes()) {
            String fileName = model.getName() + "." + GlobalProperties.ATOMIC_EXTENSION;
            writeObject(model, fileName);
        }
    }

    private void writeObject(ComponentTypeModel model, String fileName) {
        try {
            File file = new File(getDirectory(), fileName);
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(model);
            out.flush();
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean isNewNameAlreadyExistsInParent(IType child, String newName) {
        if (child == null) {
            return false;
        }
        for (IType c : getChildren()) {
            if (!child.equals(c) && child.getName().equals(c.getName())) {
                return true;
            }
        }
        return false;
    }

    public void ensureUniqueName(IType child) {
        HashSet<String> names = new HashSet<String>();
        String name = child.getName();
        for (IType model : getChildren()) {
            names.add(model.getName());
        }
        while (names.contains(name)) {
            Matcher mat = NAME_PREFIX.matcher(name);
            if (mat.matches()) {
                String baseName = mat.group(1);
                String number = mat.group(2);
                name = baseName + "_" + (Integer.parseInt(number) + 1);
            } else {
                name = name + "_1";
            }
            child.setName(name);
        }
    }
}
