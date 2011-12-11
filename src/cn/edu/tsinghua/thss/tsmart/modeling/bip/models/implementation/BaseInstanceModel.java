package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

// import org.eclipse.draw2d.geometry.Rectangle;

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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.util.UpdateNotifier;
import cn.edu.tsinghua.thss.tsmart.modeling.util.UpdateReceiver;
import cn.edu.tsinghua.thss.tsmart.platform.GlobalProperties;



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
                    IPropertySource,
                    UpdateNotifier,
                    UpdateReceiver {

    private PropertyChangeSupport listeners       = new PropertyChangeSupport(this);
    private List<UpdateReceiver>  registerObjects = new ArrayList<UpdateReceiver>();
    protected Rectangle           positionConstraint;
    @Element(required = false)
    protected Parent              parent;
    @Attribute(required = false)
    protected String              name;
    protected final UUID          uuid            = UUID.randomUUID();
    @Element(required = false, name = "type")
    protected Type                type;
    protected static Serializer   serializer      = new Persister(new CycleStrategy());
    protected GlobalProperties    prorerties      = GlobalProperties.getInstance();

    public Type getType() {
        return type;
    }

    public Model setType(Type type) {
        this.type = type;
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

    @Override
    public String getStringID() {
        return uuid.toString().replaceAll("-", "");
    }

    @Override
    public UUID getID() {
        return uuid;
    }

    @Override
    public Model setName(String newName) {
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
    public Parent getParent() {
        return parent;
    }

    @Override
    public Model setParent(Parent parent) {
        this.parent = parent;
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
        return (Model) getType().copy().getInstance().setName(getName());
    }

    @Override
    public void updated() {
        firePropertyChange(REFRESH);
    }

    @Override
    public List<UpdateReceiver> getRegisterObjects() {
        return registerObjects;
    }

    @Override
    public void register(UpdateReceiver obj) {
        if (obj != null) registerObjects.add(obj);
    }

    @Override
    public void unRegister(UpdateReceiver obj) {
        if (obj != null) registerObjects.remove(obj);
    }

    @Override
    public void notifyRegisterObjects() {
        for (UpdateReceiver receiver : getRegisterObjects()) {
            receiver.updated();
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[0];
    }
}
