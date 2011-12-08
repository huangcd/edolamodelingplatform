package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.util.UpdateNotifier;
import cn.edu.tsinghua.thss.tsmart.modeling.util.UpdateReceiver;


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
    protected Instance            instance;
    protected UUID                uuid            = UUID.randomUUID();
    protected static Serializer   serializer      = new Persister(new CycleStrategy());

    public BaseTypeModel() {}

    public Instance getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    public String getStringID() {
        return uuid.toString().replaceAll("-", "");
    }

    public UUID getID() {
        return uuid;
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

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void firePropertyChange(String propertyName) {
        listeners.firePropertyChange(propertyName, null, null);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
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

    public void setPositionConstraint(Rectangle positionConstraint) {
        this.positionConstraint = positionConstraint;
        firePropertyChange(POSITION);
    }

    /**
     * 从字符串中导入
     * 
     * @param string 需要导入的字符串
     * 
     * @return 新的模型
     */
    public static <M> M importFromString(String string) throws IOException, ClassNotFoundException {
        return importFromBytes(Base64.decodeBase64(string));
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

    protected static <T extends IInstance> HashMap<T, T> copyList(List<T> list) {
        HashMap<T, T> map = new HashMap<T, T>();
        for (T t : list) {
            map.put(t, (T) t.copy());
        }
        return map;
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

    public static void main(String[] args) throws Exception {
        Serializer serializer = new Persister(new CycleStrategy());

        // empty port
        PortTypeModel ePort = new PortTypeModel();
        ePort.setName("ePort");
        ePort.getInstance().setName("p1");

        // int port
        PortTypeModel intPort = new PortTypeModel();
        intPort.setName("intPort");
        intPort.getInstance().setName("p2");

        // data
        DataTypeModel intDataType = new DataTypeModel("int");
        intDataType.getInstance().setName("a");
        intPort.setOrderModelChild(intDataType, 0);

        // connector type
        ConnectorTypeModel synchronizedInt = new ConnectorTypeModel();
        synchronizedInt.getInstance();
        synchronizedInt.setName("synchronizedInt");
        synchronizedInt.setOrderModelChild(ePort, 0);
        synchronizedInt.setOrderModelChild(intPort, 1);
        synchronizedInt.parseInteractor("p1' p2");

        // atomic
        AtomicTypeModel machineType = new AtomicTypeModel().setName("Machine");
        machineType.getInstance().setName("mac");
        machineType.setInitAction(new ActionTypeModel().getInstance().setAction(""));

        // atomic data
        DataTypeModel intInMachine = (DataTypeModel) intDataType.copy().setName("int");
        machineType.addData((DataModel<AtomicTypeModel>) intInMachine.createInstance().setName(
                        "counter"));

        // atomic port
        PortModel runPort = intPort.getInstance().copy().setName("run");
        machineType.addPort(runPort);

        // atomic place
        PlaceModel idle = new PlaceTypeModel().getInstance().setName("IDLE");
        PlaceModel busy = idle.copy().setName("BUSY");
        machineType.addPlace(idle);
        machineType.addPlace(busy);
        machineType.setInitPlace(idle);

        // atomic transition
        TransitionModel run = new TransitionTypeModel().getInstance();
        run.setAction(new ActionTypeModel().getInstance().setAction("counter += 1;"));
        run.setGuard(new GuardTypeModel().getInstance().setGuard("count >= 0"));
        run.setPort(runPort);
        run.attachSource(idle);
        run.attachTarget(busy);
        machineType.addTransition(run);

        // atomic priority

        CompoundTypeModel all = new CompoundTypeModel().setName("all");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serializer.write(machineType.getInstance(), out);
        System.out.println(new String(out.toByteArray()));
        System.out.println(serializer.read(AtomicModel.class,
                        new ByteArrayInputStream(out.toByteArray())));
    }
}
