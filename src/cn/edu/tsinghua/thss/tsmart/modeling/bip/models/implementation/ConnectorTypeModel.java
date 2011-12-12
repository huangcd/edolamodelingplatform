package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPortType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午4:43<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root(name = "connectorType")
public class ConnectorTypeModel
                extends BaseTypeModel<ConnectorTypeModel, ConnectorModel, IContainer>
                implements
                    IDataContainer<ConnectorTypeModel, IContainer, IInstance>,
                    IPortType<ConnectorTypeModel, ConnectorModel, IContainer>,
                    IOrderContainer<IPortType> {

    @ElementList
    private List<DataModel<ConnectorTypeModel>> datas         =
                                                                              new ArrayList<DataModel<ConnectorTypeModel>>();
    @ElementList
    private List<DataModel<ConnectorTypeModel>> exportDatas   =
                                                                              new ArrayList<DataModel<ConnectorTypeModel>>();
    @ElementList
    private List<InteractionModel>              interactions  = new ArrayList<InteractionModel>();
    @ElementList
    private List<IPortType>                     portArguments = new ArrayList<IPortType>();
    @Element(required = false)
    private Interactor                          interactor;

    public ConnectorTypeModel() {}

    protected List<DataModel<ConnectorTypeModel>> getExportDatas() {
        return exportDatas;
    }

    @Override
    public ConnectorModel createInstance() {
        if (instance == null) {
            instance = new ConnectorModel().setType(this);
        }
        return instance;
    }

    @Override
    public ConnectorTypeModel copy() {
        ConnectorTypeModel model = new ConnectorTypeModel();
        model.datas.addAll(datas);
        model.exportDatas.addAll(exportDatas);
        model.interactions.addAll(interactions);
        model.portArguments.addAll(portArguments);
        return model;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("connector type ").append(getName()).append('(');
        if (portArguments != null && !portArguments.isEmpty()) {
            IPortType portType = portArguments.get(0);
            buffer.append(portType.getName()).append(' ').append(portType.getInstance().getName());
            for (int i = 1, size = portArguments.size(); i < size; i++) {
                portType = portArguments.get(0);
                buffer.append(", ").append(portType.getName()).append(' ')
                                .append(portType.getInstance().getName());
            }
        }
        buffer.append(")\n");
        buffer.append("   define ").append(interactor).append('\n');
        for (DataModel<ConnectorTypeModel> data : datas) {
            buffer.append("    ").append(data).append('\n');
        }
        for (InteractionModel interaction : interactions) {
            buffer.append("    ").append(interaction).append('\n');
        }
        buffer.append("end");
        return buffer.toString();
    }

    /**
     * 检查connector的端口参数和实例端口的类型是否一致
     * 
     * @param port 实例端口
     * @param index 实例端口位置
     * 
     * @return 如果一致，返回true；否则返回false
     */
    public boolean validateArguments(IPort port, int index) {
        int size = portArguments.size();
        if (index < 0 || index >= size) {
            return false;
        }
        IPortType portType = portArguments.get(index);
        if (port == null || portType == null) {
            return false;
        }
        List<DataTypeModel> portTypeArguments = portType.getPortTypeArguments();
        List<DataModel> portArguments = port.getPortArguments();
        if (portTypeArguments.size() != portArguments.size()) {
            return false;
        }
        for (int i = 0, max = portTypeArguments.size(); i < max; i++) {
            if (!portTypeArguments.get(i).getTypeName()
                            .equals(portArguments.get(i).getType().getName())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<DataModel<ConnectorTypeModel>> getDatas() {
        return datas;
    }

    @Override
    public List<IInstance> getChildren() {
        List<IInstance> list = new ArrayList<IInstance>(interactions);
        list.addAll(datas);
        return list;
    }

    @Override
    public ConnectorTypeModel addChild(IInstance child) {
        if (child instanceof DataModel) {
            addData((DataModel<ConnectorTypeModel>) child);
        }
        if (child instanceof InteractionModel) {
            addInteraction((InteractionModel) child);
        }
        return this;
    }

    public void addInteraction(InteractionModel child) {
        interactions.add(child);
        firePropertyChange(CHILDREN);
    }

    public void addData(DataModel<ConnectorTypeModel> child) {
        datas.add(child);
        firePropertyChange(CHILDREN);
    }

    public boolean removeInteraction(InteractionModel child) {
        boolean result = interactions.remove(child);
        firePropertyChange(CHILDREN);
        return result;
    }

    public boolean removeData(DataModel<ConnectorTypeModel> child) {
        boolean result = datas.remove(child);
        firePropertyChange(CHILDREN);
        return result;
    }


    /**
     * 解析 define [p1' p2]' p3 样式的语句
     * 
     * @param string [p1' p2]' p3 样式的语句
     * 
     * @return 模型本身，用于串接调用
     */
    public ConnectorTypeModel parseInteractor(String string) {
        string = String.format("[%s]", string);
        Stack<Object> stack = new Stack<Object>();
        List<Interactor> list = new LinkedList<Interactor>();
        HashMap<String, IPortType> portTypeHashMap = new HashMap<String, IPortType>();
        for (IPortType portType : this.portArguments) {
            portTypeHashMap.put(portType.getInstance().getName(), portType);
        }
        int i = 0;
        int size = string.length();
        while (i < size) {
            char c = string.charAt(i);
            if (c == '[') {
                stack.push("[");
            } else if (c == ']') {
                Interactor completePort = null;
                while (true) {
                    Object obj = stack.pop();
                    if (obj.equals("[")) {
                        break;
                    }
                    if (obj.equals("'")) {
                        if (stack.peek() instanceof Interactor) {
                            completePort = (Interactor) stack.pop();
                            if (!stack.peek().equals("[")) {
                                throw new IllegalArgumentException(string);
                            }
                        } else {
                            throw new IllegalArgumentException(string);
                        }
                    } else if (obj instanceof Interactor) {
                        list.add(0, (Interactor) obj);
                    }
                }
                Interactor interactor = new Interactor(completePort, list);
                stack.push(interactor);
            } else if (c == '\t' || c == ' ') {
                i++;
                continue;
            } else if (c == '\'') {
                stack.push("'");
            } else if (isIdentifierStart(c)) {
                int j = i + 1;
                while (j < size && isIdentifierPart(string.charAt(j))) {
                    j++;
                }
                String identifier = string.substring(i, j);
                IPortType portType = portTypeHashMap.get(identifier);
                if (portType == null) {
                    throw new NullPointerException(identifier);
                }
                stack.push(new Interactor(portType));
                i = j - 1;
            }
            i++;
        }
        if (stack.size() != 1 || !(stack.peek() instanceof Interactor)) {
            throw new IllegalArgumentException(string);
        }
        this.interactor = (Interactor) stack.pop();
        return this;
    }

    private boolean isIdentifierStart(char c) {
        return c == '_' || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean isIdentifierPart(char c) {
        return isIdentifierStart(c) || (c >= '0' && c <= '9');
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public ConnectorTypeModel addParameter(IPortType child) {
        return setParameter(child, portArguments.size());
    }

    public ConnectorTypeModel setParameter(IPortType child, int index) {
        setOrderModelChild(child, index);
        return this;
    }

    @Override
    public boolean removeChild(IInstance iInstance) {
        if (iInstance instanceof DataModel) {
            return removeData((DataModel<ConnectorTypeModel>) iInstance);
        } else if (iInstance instanceof InteractionModel) {
            return removeInteraction((InteractionModel) iInstance);
        }
        return false;
    }

    @Override
    // TODO test me
    public void setOrderModelChild(IPortType child, int index) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException(Integer.toString(index));
        }
        if (index < portArguments.size()) {
            portArguments.set(index, child);
            return;
        }
        while (index > portArguments.size()) {
            portArguments.add(null);
        }
        portArguments.add(child);
        getInstance().enlargeArguments(portArguments.size());
        firePropertyChange(CHILDREN);
    }

    @Override
    public boolean allSets() {
        for (IPortType child : portArguments) {
            if (child == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IPortType removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        getInstance().removeArgument(index);
        return portArguments.remove(index);
    }

    @Override
    public List<IPortType> getOrderList() {
        return portArguments;
    }

    @Override
    public List<DataTypeModel> getPortTypeArguments() {
        List<DataTypeModel> dataTypes = new ArrayList<DataTypeModel>();
        for (DataModel<ConnectorTypeModel> data : exportDatas) {
            dataTypes.add(data.getType());
        }
        return dataTypes;
    }

    @Override
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isNewNameAlreadyExistsInParent(IInstance child, String newName) {
        for (IInstance instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }
}


@SuppressWarnings({"unused", "rawtypes"})
@Root
class Interactor {

    @Element(required = false, name = "content")
    private IPortType    content;
    @Element(required = false, name = "trigger")
    private Interactor   completePort;
    @ElementArray(required = false, name = "ports")
    private Interactor[] incompletePorts;

    public Interactor(@Element(name = "content") IPortType content) {
        this.content = content;
        this.completePort = null;
        this.incompletePorts = null;
    }

    public Interactor(@Element(name = "trigger") Interactor completePort,
                    @ElementArray(name = "ports") Interactor... incompletePorts) {
        this.completePort = completePort;
        this.incompletePorts = incompletePorts;
    }

    public Interactor(Interactor completePort, List<Interactor> incompletePorts) {
        this.completePort = completePort;
        this.incompletePorts = incompletePorts.toArray(new Interactor[incompletePorts.size()]);
    }

    public String toString() {
        if (content != null) {
            return content.getName();
        }
        StringBuilder buffer = new StringBuilder("[");
        if (completePort != null) {
            buffer.append(completePort).append("\' ");
        }
        if (incompletePorts != null && !(incompletePorts.length > 0)) {
            for (Interactor interactor : incompletePorts) {
                buffer.append(interactor).append(' ');
            }
        }
        return buffer.append(']').toString();
    }
}
