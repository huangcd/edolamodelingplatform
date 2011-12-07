package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

// import org.eclipse.draw2d.geometry.Rectangle;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.draw2d.geometry.Rectangle;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;



/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ����9:18<br/>
 */
@Root
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class BaseInstanceModel<Model extends BaseInstanceModel, Type extends IType, Parent extends IContainer>
                implements
                    IInstance<Model, Type, Parent> {

    private PropertyChangeSupport listeners  = new PropertyChangeSupport(this);
    protected Rectangle           positionConstraint;
    @Element(required = false)
    protected Parent              parent;
    @Attribute(required = false)
    protected String              name;
    protected final UUID          uuid       = UUID.randomUUID();
    @Element(required = false, name = "type")
    protected Type                type;
    protected static Serializer   serializer = new Persister(new CycleStrategy());

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
     * ���ַ����е���
     * 
     * @param string ��Ҫ������ַ���
     * 
     * @return �µ�ģ��
     */
    public static <M> M importFromString(String string) throws IOException, ClassNotFoundException {
        return importFromBytes(Base64.decodeBase64(string));
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
        firePropertyChange(PARENT);
        return (Model) this;
    }

    public Rectangle getPositionConstraint() {
        return positionConstraint;
    }

    public void setPositionConstraint(Rectangle positionConstraint) {
        this.positionConstraint = positionConstraint;
        firePropertyChange(POSITION);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName) {
        listeners.firePropertyChange(propertyName, null, null);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    /**
     * ��byte�����е������
     * 
     * @param bytes ��ObjectOutStream������byte����
     * 
     * @return һ��IType��ʵ��
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
}
