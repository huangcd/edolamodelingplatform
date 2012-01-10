package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

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
 * Date: 11-9-25<br/>
 * Time: 下午9:18<br/>
 */
@Root
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseInstanceModel<Model extends BaseInstanceModel, Type extends IType, Parent extends IContainer>
                implements
                    IInstance<Model, Type, Parent>,
                    IPropertySource {
    private static final long            serialVersionUID = -7202759073484290135L;
    protected transient GlobalProperties properties;

    public GlobalProperties getProperties() {
        if (properties == null) {
            properties = GlobalProperties.getInstance();
        }
        return properties;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
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

    private PropertyChangeSupport listeners   = new PropertyChangeSupport(this);
    protected Rectangle           positionConstraint;
    @Element(required = false)
    protected Parent              parent;
    @Attribute(required = false)
    protected String              name;
    @Attribute(required = false)
    private String                comment;
    protected UUID                uuid        = UUID.randomUUID();
    @Element(required = false, name = "type")
    protected Type                type;
    protected boolean             editable    = true;
    private ArrayList<String>     entityNames = new ArrayList<String>();
    private String                oldName;

    public String getOldName() {
        return oldName;
    }

    public Type getType() {
        return type;
    }

    public Model setType(Type type) {
        this.type = type;
        return (Model) this;
    }

    @Override
    public ArrayList<String> getEntityNames() {
        return entityNames;
    }

    @Override
    public Model setEntityNames(ArrayList<String> entityNames) {
        this.entityNames = new ArrayList<String>(entityNames);
        firePropertyChange(ENTITY);
        validateOnTheFly();
        return (Model) this;
    }

    @Override
    public Model deleteAllEntityNames() {
        entityNames.removeAll(null);
        firePropertyChange(ENTITY);
        validateOnTheFly();
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
    public String getStringID() {
        return uuid.toString().replaceAll("-", "");
    }

    @Override
    public UUID getID() {
        return uuid;
    }

    public Model resetID() {
        uuid = UUID.randomUUID();
        return (Model) this;
    }

    @Override
    public Model setName(String newName) {
        this.oldName = this.name;
        this.name = newName;
        firePropertyChange(NAME);
        return (Model) this;
    }

    public boolean hasName() {
        return name != null;
    }

    @Override
    public String getName() {
        if (name == null) {
            return getStringID();
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
        // firePropertyChange(PARENT);
        return (Model) this;
    }

    public Rectangle getPositionConstraint() {
        return positionConstraint;
    }

    public Model setPositionConstraint(Rectangle positionConstraint) {
        Rectangle oldPositionConstraint = this.positionConstraint;
        this.positionConstraint = positionConstraint;
        firePropertyChange(CONSTRAINT, oldPositionConstraint, this.positionConstraint);
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

    @Override
    public Object getEditableValue() {
        return this;
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public byte[] exportToBytes() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeObject(this);
        out.close();
        return bytes.toByteArray();
    }

    @Override
    public Model copy() {
        if (getType() != null) {
            return (Model) ((IType) getType().copy()).getInstance().setName(getName()).resetID();
        }
        try {
            return (Model) this.getClass().getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return (Model) this;
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[0];
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
            // 需要清空err box
            try {
                MessageUtil.clearProblemMessage();
            } catch (CoreException e) {
                e.printStackTrace();
            }

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

        if (this.getType() instanceof IContainer) {
            for (Object child : ((IContainer) this.getType()).getChildren()) {
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
