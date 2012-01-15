package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.EdolaModelingException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.dialogs.MessageUtil;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Rule;
import cn.edu.tsinghua.thss.tsmart.modeling.validation.Validator;
import cn.edu.tsinghua.thss.tsmart.platform.properties.GlobalProperties;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:49<br/>
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Root
public abstract class BaseTypeModel<Model extends BaseTypeModel, Instance extends IInstance, Parent extends IContainer>
                implements
                    IType<Model, Instance, Parent>,
                    IPropertySource {
    private static final long                   serialVersionUID = 4399354235987755192L;
    protected static final transient Serializer serializer;
    static {
        serializer = new Persister(new CycleStrategy());
    }
    private PropertyChangeSupport               listeners        = new PropertyChangeSupport(this);
    @Element(required = false)
    protected Rectangle                         positionConstraint;
    private Parent                              parent;
    @Attribute(required = false)
    private String                              name;
    @Attribute(required = false)
    private String                              comment;
    @Element
    protected Instance                          instance;
    @Attribute
    private boolean                             editable         = true;
    protected transient GlobalProperties        properties;

    /**
     * 从文件读取Atomic模型
     * 
     * @param file
     * @return
     */
    public static AtomicTypeModel loadAtomicType(File file) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
            AtomicTypeModel model = (AtomicTypeModel) in.readObject();
            return model;
        } catch (Exception e) {
            System.err.println("error when load file:" + file);
            e.printStackTrace();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {}
            return null;
        }
    }

    /**
     * 从文件读取Compound模型
     * 
     * @param file
     * @return
     */
    public static CompoundTypeModel loadCompoundType(File file) {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            CompoundTypeModel model = (CompoundTypeModel) in.readObject();
            return model;
        } catch (Exception e) {
            System.err.println("Error while loading file:" + file);
            e.printStackTrace();
            return null;
        }
    }

    public BaseTypeModel() {}

    public Instance getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public GlobalProperties getProperties() {
        if (properties == null) {
            properties = GlobalProperties.getInstance();
        }
        return properties;
    }

    @Override
    public ArrayList<String> getEntityNames() {
        return getInstance().getEntityNames();
    }

    public Model setEntityNames(ArrayList<String> entityNames) {
        getInstance().setEntityNames(entityNames);
        firePropertyChange(ENTITY);
        validateOnTheFly();
        return (Model) this;
    }

    @Override
    public Model deleteAllEntityNames() {
        getInstance().deleteAllEntityNames();
        firePropertyChange(ENTITY);
        validateOnTheFly();
        return (Model) this;
    }

    public boolean hasName() {
        return name != null;
    }

    @Override
    public Model setName(String newName) {
        this.name = newName;
        firePropertyChange(NAME);
        return (Model) this;
    }

    @Override
    public String getName() {
        if (name == null) {
            return UNNAMED_NAME;
        }
        return name;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public Model setComment(String comment) {
        this.comment = comment;
        return (Model) this;
    }

    @Override
    public Parent getParent() {
        return parent;
    }

    @Override
    public Model setParent(Parent parent) {
        this.parent = parent;
        return (Model) this;
    }

    public Model addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
        return (Model) this;
    }

    public Model firePropertyChange(String propertyName) {
        listeners.firePropertyChange(propertyName, null, null);
        return (Model) this;
    }

    public Model firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        listeners.firePropertyChange(propertyName, oldValue, newValue);
        return (Model) this;
    }

    public Model removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
        return (Model) this;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
    }

    public Model setPositionConstraint(Rectangle positionConstraint) {
        this.positionConstraint = positionConstraint;
        firePropertyChange(CONSTRAINT);
        return (Model) this;
    }

    @Override
    public String exportToString() {
        try {
            byte[] bytes = exportToBytes();
            return new String(Base64.encodeBase64(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public Object getEditableValue() {
        return this;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[0];
    }

    @Override
    public void resetPropertyValue(Object id) {}

    public Rectangle getPositionConstraint() {
        return positionConstraint;
    }

    /**
     * 从字符串中导入
     * 
     * @param string 需要导入的字符串
     * 
     * @return 新的模型
     */
    public static <M> M importFromString(String string) throws IOException, ClassNotFoundException {
        return importFromBytes(Base64.decodeBase64(string.getBytes()));
    }

    /**
     * 从byte数组中导入对象
     * 
     * @param bytes 用ObjectOutStream导出的byte数组
     * 
     * @return 一个IType的实例
     */
    public static <M> M importFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return (M) in.readObject();
    }

    public Model copy() {
        try {
            byte[] bytes = exportToBytes();
            Model model = importFromBytes(bytes);
            model.getInstance();
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] exportToBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(this);
        out.close();
        return bytes.toByteArray();
    }

    @Override
    public boolean editable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public boolean validateOnTheFly() {
        try {
            MessageUtil.clearProblemMessage();
            getProperties();
            for (Rule rule : properties.getRules()) {
                for (Validator validator : getValidators()) {
                    validator.validateOnTheFly(this, rule);
                }
            }
            return true;
        } catch (EdolaModelingException e) {
            MessageUtil.ShowErrorDialog(e.getMessage(), "Error");
            return false;
        }
    }

    @Override
    public boolean validate() {
        boolean result = true;
        try {
            getProperties();
            for (Rule rule : properties.getRules()) {
                for (Validator validator : getValidators()) {
                    result = validator.validate(this, rule) && result;
                }
            }
            return result;
        } catch (EdolaModelingException e) {
            MessageUtil.ShowErrorDialog(e.getMessage(), "Error");
            return false;
        }
    }

    @Override
    public boolean validateFull() {
        boolean result = true;

        if (this instanceof IContainer) {
            for (Object child : ((IContainer) this).getChildren()) {
                if (child instanceof IModel) {
                    result = ((IModel) child).validateFull() && result;
                }
            }
        }

        return validate() && result;
    }

    @Override
    public List<Validator> getValidators() {
        return getProperties().getValidators();
    }

    /*
     * 代码生成相关
     */
    public boolean isMarkedHareware() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return false;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        return this.getEntityNames().contains(projectModel.getHardwareEntity());
    }

    public boolean isMarkedSoftware() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return false;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        return this.getEntityNames().contains(projectModel.getSoftwareEntity());
    }

    public boolean isMarkedIO() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return false;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        return this.getEntityNames().contains(projectModel.getIoEntity());
    }

    public boolean isMarkedTick() {
        TopLevelModel topModel = GlobalProperties.getInstance().getTopModel();
        // 构件库模式下不做检测
        if (topModel instanceof LibraryModel) {
            return false;
        }
        if (!(topModel instanceof CodeGenProjectModel)) {
            try {
                throw new Exception("something seems wrong");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return false;
        }
        CodeGenProjectModel projectModel = (CodeGenProjectModel) topModel;

        return this.getEntityNames().contains(projectModel.getTickEntity());
    }
}
