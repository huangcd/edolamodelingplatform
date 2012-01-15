package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IProgressMonitor;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.ITopModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

@SuppressWarnings({"rawtypes", "unchecked"})
@Root
public abstract class TopLevelModel<Model extends TopLevelModel>
                extends BaseTypeModel<Model, IInstance, ITopModel> implements ITopModel<Model> {

    private static final String        OPEN_FILE        = ".open";
    public static final FilenameFilter atomicFilenameFilter;
    public static final FilenameFilter compoundFilenameFilter;
    private static final Pattern       NAME_PREFIX      = Pattern.compile("^(.*)_(\\d+)$");
    private static final long          serialVersionUID = -5707343197446452791L;

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
    @ElementList
    private List<AtomicTypeModel>      atomicTypes;
    @Attribute
    private String                     baseline;
    @ElementList
    private List<CompoundTypeModel>    compoundTypes;
    @ElementList
    private List<ConnectorTypeModel>   connectorTypes;
    @ElementList
    private List<DataTypeModel>        dataTypes;
    @ElementList
    private List<ComponentTypeModel>   openModels;
    @ElementList
    private List<PortTypeModel>        portTypes;
    private transient File             directory;
    private transient String           location;

    public TopLevelModel() {
        dataTypes = new ArrayList<DataTypeModel>();
        portTypes = new ArrayList<PortTypeModel>();
        connectorTypes = new ArrayList<ConnectorTypeModel>();
        atomicTypes = new ArrayList<AtomicTypeModel>();
        compoundTypes = new ArrayList<CompoundTypeModel>();
        openModels = new ArrayList<ComponentTypeModel>();
    }

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
        firePropertyChange(CHILDREN);
        return (Model) this;
    }

    public void addDataType(String type) {
        addChild(DataTypeModel.addType(type));
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

    @Override
    public IInstance createInstance() {
        return null;
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

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    public List<AtomicTypeModel> getAtomicTypes() {
        return atomicTypes;
    }

    public String getBaseline() {
        return baseline;
    }

    @Override
    public List<IType> getChildren() {
        List<IType> types = new ArrayList<IType>();
        types.addAll(getDataTypeModels());
        types.addAll(getPortTypes());
        types.addAll(getConnectorTypes());
        types.addAll(getAtomicTypes());
        types.addAll(getCompoundTypes());
        for (IType type : types) {
            if (type instanceof PortTypeModel || type instanceof DataTypeModel
                            || type instanceof ConnectorTypeModel) {
                type.setParent(null);
            } else if (type.getParent() == null || !type.getParent().equals(this)) {
                type.setParent(this);
            }
        }
        return types;
    }

    public List<CompoundTypeModel> getCompoundTypes() {
        return compoundTypes;
    }

    public List<ConnectorTypeModel> getConnectorTypes() {
        return connectorTypes;
    }

    public List<DataTypeModel> getDataTypeModels() {
        return dataTypes;
    }

    public File getDirectory() {
        return directory;
    }

    public String getLocation() {
        return location;
    }

    public List<ComponentTypeModel> getOpenModels() {
        return openModels;
    }

    public List<PortTypeModel> getPortTypes() {
        return portTypes;
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
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

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    public void loadTypes(IProgressMonitor monitor) {
        File[] atomicFiles = getDirectory().listFiles(atomicFilenameFilter);
        File[] compoundFiles = getDirectory().listFiles(compoundFilenameFilter);
        int total = 3 + atomicFiles.length + compoundFiles.length;
        int count = 0;
        monitor.beginTask("读取构件", total);
        DataTypeModel.loadTypes(getDirectory());
        monitor.worked(++count);
        PortTypeModel.loadTypes(getDirectory());
        monitor.worked(++count);
        ConnectorTypeModel.loadTypes(getDirectory());
        monitor.worked(++count);
        HashMap<String, ComponentTypeModel> map = new HashMap<String, ComponentTypeModel>();
        // 读取atomic
        for (File file : atomicFiles) {
            AtomicTypeModel model = loadAtomicType(file);
            if (model != null) {
                addChild(model);
                map.put(model.getName(), model);
            }
            monitor.worked(++count);
        }
        // 读取compound
        for (File file : compoundFiles) {
            CompoundTypeModel model = loadCompoundType(file);
            if (model != null) {
                addChild(model);
                map.put(model.getName(), model);
            }
            monitor.worked(++count);
        }
        if (this instanceof ProjectModel) {
            openModels(map);
        } else if (this instanceof LibraryModel) {
            for (ComponentTypeModel model : map.values()) {
                BIPEditor.openBIPEditor(model);
            }
        }
        monitor.done();
    }

    public abstract void reloadComponent();

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

    /**
     * 将关闭的页面对应的模型从openModels里面删除
     * 
     * @param model
     */
    public void removeOpenModel(ComponentTypeModel model) {
        this.openModels.remove(model);
    }

    /**
     * TODO 好好设计一下保存文件的格式(@huangcd)
     */
    public abstract void save() throws IOException;

    public void setAtomicTypes(List<AtomicTypeModel> atomicTypes) {
        this.atomicTypes = atomicTypes;
    }

    public void setBaseline(String baseline) {
        this.baseline = baseline;
    }

    public void setCompoundTypes(List<CompoundTypeModel> compoundTypes) {
        this.compoundTypes = compoundTypes;
    }

    public void setConnectorTypes(List<ConnectorTypeModel> connectorTypes) {
        this.connectorTypes = connectorTypes;
    }

    public void setDataTypeModels(List<DataTypeModel> dataTypeModels) {
        this.dataTypes = dataTypeModels;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setOpenModels(List<ComponentTypeModel> openModels) {
        this.openModels = openModels;
    }

    public void setPortTypes(List<PortTypeModel> portTypes) {
        this.portTypes = portTypes;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    protected void saveTypes(IProgressMonitor monitor, int count) {
        for (IType type : getChildren()) {
            type.setParent(null);
        }
        DataTypeModel.saveTypes(getDirectory());
        count += dataTypes.size();
        monitor.worked(count);
        PortTypeModel.saveTypes(getDirectory());
        count += portTypes.size();
        monitor.worked(count);
        ConnectorTypeModel.saveTypes(getDirectory());
        count += connectorTypes.size();
        monitor.worked(count);
        saveAtomics();
        count += atomicTypes.size();
        monitor.worked(count);
        saveCompounds();
        count += compoundTypes.size();
        monitor.worked(count);
        saveOpenModels();
        getChildren();
    }

    private void saveAtomics() {
        // 删除目录下的edolaa文件
        for (String file : getDirectory().list(atomicFilenameFilter)) {
            new File(file).delete();
        }
        for (AtomicTypeModel model : getAtomicTypes()) {
            // 确保ITopModel不会被保存
            ITopModel topModel = model.getParent();
            model.setParent(null);
            HashMap<IModel, IContainer> map = new HashMap<IModel, IContainer>();
            for (IInstance child : model.getChildren()) {
                if (child.getParent() != null && child.getParent() instanceof ITopModel) {
                    map.put(child, child.getParent());
                    child.setParent(null);
                }
                IType type = child.getType();
                if (type != null && type.getParent() != null
                                && type.getParent() instanceof ITopModel) {
                    map.put(type, type.getParent());
                    type.setParent(null);
                }
            }
            String fileName = model.getName() + "." + GlobalProperties.ATOMIC_EXTENSION;
            writeObject(model, fileName);
            model.setParent(topModel);
            for (Map.Entry<IModel, IContainer> entry : map.entrySet()) {
                entry.getKey().setParent(entry.getValue());
            }
        }
    }

    private void saveCompounds() {
        // 删除目录下的edolam文件
        for (String file : getDirectory().list(compoundFilenameFilter)) {
            new File(file).delete();
        }
        for (CompoundTypeModel model : getCompoundTypes()) {
            // 确保ITopModel不会被保存
            ITopModel topModel = model.getParent();
            model.setParent(null);
            ArrayList<AtomicTypeModel> atomics = new ArrayList<AtomicTypeModel>();
            ArrayList<CompoundTypeModel> compounds = new ArrayList<CompoundTypeModel>();
            model.getAllComponentWithoutChecking(compounds, atomics);
            HashMap<IType, ITopModel> map = new HashMap<IType, ITopModel>();
            for (AtomicTypeModel atomic : atomics) {
                if (atomic.getParent() != null) {
                    map.put(atomic, atomic.getParent());
                    atomic.setParent(null);
                }
            }
            for (CompoundTypeModel compound : compounds) {
                if (compound.getParent() != null) {
                    map.put(compound, compound.getParent());
                    compound.setParent(null);
                }
            }
            String fileName = model.getName() + "." + GlobalProperties.COMPOUND_EXTENSION;
            writeObject(model, fileName);
            model.setParent(topModel);
            for (Map.Entry<IType, ITopModel> entry : map.entrySet()) {
                entry.getKey().setParent(entry.getValue());
            }
        }
    }

    private void openModels(HashMap<String, ComponentTypeModel> map) {
        try {
            BufferedReader in =
                            new BufferedReader(new FileReader(new File(getDirectory(), OPEN_FILE)));
            String line;
            while ((line = in.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (map.containsKey(line)) {
                    BIPEditor.openBIPEditor(map.get(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOpenModels() {
        try {
            PrintStream out = new PrintStream(new File(getDirectory(), OPEN_FILE));
            for (ComponentTypeModel model : getOpenModels()) {
                out.println(model.getName());
                out.flush();
            }
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
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
}
