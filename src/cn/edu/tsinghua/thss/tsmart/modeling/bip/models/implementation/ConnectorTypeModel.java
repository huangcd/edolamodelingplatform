package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.gef.palette.CreationToolEntry;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.compound.CompoundEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IOrderContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.requests.CopyFactory;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ����4:43<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root(name = "connectorType")
public class ConnectorTypeModel
                extends BaseTypeModel<ConnectorTypeModel, ConnectorModel, IDataContainer>
                implements
                    IDataContainer<ConnectorTypeModel, IDataContainer, IInstance>,
                    IOrderContainer<PortTypeModel> {
    private final static HashMap<String, ConnectorTypeModel>                         typeSources;
    private final static HashMap<String, HashMap<CompoundEditor, CreationToolEntry>> toolMap;

    static {
        typeSources = new HashMap<String, ConnectorTypeModel>();
        toolMap = new HashMap<String, HashMap<CompoundEditor, CreationToolEntry>>();
        ConnectorTypeModel singleton = new ConnectorTypeModel();
        singleton.addArgument(PortTypeModel.getPortTypeModel("ePort"), "p1");
        ConnectorTypeModel rendezvous = new ConnectorTypeModel();
        rendezvous.addArgument(PortTypeModel.getPortTypeModel("ePort"), "p1").addArgument(
                        PortTypeModel.getPortTypeModel("ePort"), "p2");
    }

    public static void addType(String type, String[][] arrays) {
        ConnectorTypeModel connector = new ConnectorTypeModel().setName(type);
        for (int i = 0, size = arrays.length; i < size; i++) {
            String portTypeName = arrays[i][0];
            String argumentName = arrays[i][1];
            connector.addArgument(PortTypeModel.getPortTypeModel(portTypeName), argumentName);
        }
        addTypeSources(type, connector);
        HashMap<CompoundEditor, CreationToolEntry> map =
                        new HashMap<CompoundEditor, CreationToolEntry>();
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            CreationToolEntry entry =
                            new CreationToolEntry(type, "�½�һ��" + type + "������", new CopyFactory(
                                            connector),
                                            BIPEditor.getImage("icons/connector_16.png"),
                                            BIPEditor.getImage("icons/connector_32.png"));
            editor.addConnectorCreationToolEntry(entry);
            map.put(editor, entry);
        }
        toolMap.put(type, map);
    }

    public static boolean addTypeSources(String type, ConnectorTypeModel connector) {
        if (typeSources.containsKey(type)) return false;
        typeSources.put(type, connector);
        return true;
    }

    public static void addToolEntry(String type, CompoundEditor editor, CreationToolEntry entry) {
        if (!toolMap.containsKey(type)) {
            toolMap.put(type, new HashMap<CompoundEditor, CreationToolEntry>());
        }
        toolMap.get(type).put(editor, entry);
    }

    public static void removeType(String type) {
        HashMap<CompoundEditor, CreationToolEntry> map = toolMap.get(type);
        for (CompoundEditor editor : BIPEditor.getCompoundEditors()) {
            editor.removeConnectorCreationToolEntry(map.get(editor));
        }
        toolMap.remove(type);
        removeTypeSources(type);
        // TODO ɾ��һ�����͵�ʱ����������Ƿ�ʹ��
    }

    public static boolean removeTypeSources(String type) {
        if (type.equals("singleton") || type.equals("rendezvous") || !typeSources.containsKey(type))
            return false;
        typeSources.remove(type);
        return true;
    }

    public static Set<String> getTypes() {
        return typeSources.keySet();
    }

    public static String[] getTypeNamesAsArray() {
        ArrayList<String> array = new ArrayList<String>(typeSources.keySet());
        Collections.sort(array);
        return array.toArray(new String[array.size()]);
    }

    public static Set<Map.Entry<String, ConnectorTypeModel>> getTypeEntries() {
        return typeSources.entrySet();
    }

    public static ConnectorTypeModel getConnectorTypeModel(String type) {
        return typeSources.get(type).copy();
    }

    static class ArgumentEntry implements Serializable {
        private int       index;
        private boolean   bounded = false;
        private String    name;
        private PortTypeModel model;

        protected ArgumentEntry(String name, PortTypeModel model, int index) {
            super();
            this.name = name;
            this.model = model;
            this.index = index;
        }

        public String getName() {
            return name;
        }

        protected void setName(String name) {
            this.name = name;
        }

        public PortTypeModel getModel() {
            return model;
        }

        protected void setModel(PortTypeModel model) {
            this.model = model;
        }

        public String getTypeName() {
            return model.getName();
        }

        public boolean isBounded() {
            return bounded;
        }

        protected void bound(PortTypeModel model) {
            this.model = model;
            this.bounded = true;
        }

        public void unbound() {
            this.bounded = false;
        }

        public int getIndex() {
            return index;
        }

        public String toString() {
            return model.getName() + " " + name;
        }

    }

    @ElementList
    private List<DataModel<ConnectorTypeModel>> datas;
    @ElementList
    private List<DataModel<ConnectorTypeModel>> exportDatas;
    @ElementList
    private List<InteractionModel>              interactions;
    @ElementList
    private List<ArgumentEntry>                 arguments;
    @Element
    private PortTypeModel                       port;
    @Element(required = false)
    private Interactor                          interactor;

    public ConnectorTypeModel() {
        datas = new ArrayList<DataModel<ConnectorTypeModel>>();
        exportDatas = new ArrayList<DataModel<ConnectorTypeModel>>();
        interactions = new ArrayList<InteractionModel>();
        arguments = new ArrayList<ArgumentEntry>();
    }

    protected List<DataModel<ConnectorTypeModel>> getExportDatas() {
        return exportDatas;
    }

    public ConnectorTypeModel addArgument(PortTypeModel child, String name) {
        arguments.add(new ArgumentEntry(name, child, arguments.size()));
        firePropertyChange(CHILDREN);
        return this;
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
        model.arguments.addAll(arguments);
        return model;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("connector type ").append(getName()).append('(');
        if (arguments != null && !arguments.isEmpty()) {
            PortTypeModel portType = arguments.get(0).getModel();
            buffer.append(portType.getName()).append(' ').append(portType.getInstance().getName());
            for (int i = 1, size = arguments.size(); i < size; i++) {
                portType = arguments.get(0).getModel();
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
     * ���connector�Ķ˿ڲ�����ʵ���˿ڵ������Ƿ�һ��
     * 
     * @param port ʵ���˿�
     * @param index ʵ���˿�λ��
     * 
     * @return ���һ�£�����true�����򷵻�false
     */
    public boolean validateArguments(PortModel port, int index) {
        // TODO ʹ�ø��򵥵ķ�����ֱ�Ӽ�����port�������Ƿ�һ����
        int size = arguments.size();
        if (index < 0 || index >= size) {
            return false;
        }
        PortTypeModel portType = arguments.get(index).getModel();
        if (port == null || portType == null) {
            return false;
        }
        List<DataTypeModel> portTypeArguments = portType.getArguments();
        List<DataModel> portArguments = port.getPortArguments();
        if (portTypeArguments.size() != portArguments.size()) {
            return false;
        }
        for (int i = 0, max = portTypeArguments.size(); i < max; i++) {
            if (!portTypeArguments.get(i).getName()
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
     * ���� define [p1' p2]' p3 ��ʽ�����
     * 
     * @param string [p1' p2]' p3 ��ʽ�����
     * 
     * @return ģ�ͱ������ڴ��ӵ���
     */
    public ConnectorTypeModel parseInteractor(String string) {
        string = String.format("[%s]", string);
        Stack<Object> stack = new Stack<Object>();
        List<Interactor> list = new LinkedList<Interactor>();
        HashMap<String, PortTypeModel> portTypeHashMap = new HashMap<String, PortTypeModel>();
        for (ArgumentEntry entry : this.arguments) {
            portTypeHashMap.put(entry.getModel().getInstance().getName(), entry.getModel());
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
                PortTypeModel portType = portTypeHashMap.get(identifier);
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

    public ConnectorTypeModel addParameter(PortTypeModel child) {
        return setParameter(child, arguments.size());
    }

    public ConnectorTypeModel setParameter(PortTypeModel child, int index) {
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
    public void setOrderModelChild(PortTypeModel child, int index) {
        arguments.get(index).setModel(child);
        firePropertyChange(CHILDREN);
    }

    @Override
    public boolean allSets() {
        for (ArgumentEntry child : arguments) {
            if (!child.isBounded()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public PortTypeModel removeOrderModelChild(int index) {
        firePropertyChange(CHILDREN);
        return arguments.remove(index).getModel();
    }

    @Override
    public List<PortTypeModel> getOrderList() {
        ArrayList<PortTypeModel> list = new ArrayList<PortTypeModel>();
        for (ArgumentEntry entry : arguments) {
            list.add(entry.getModel());
        }
        return list;
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

    @Override
    public boolean isNewNameAlreadyExistsInParent(IInstance child, String newName) {
        for (IInstance instance : getChildren()) {
            if (!instance.equals(child) && instance.getName().equals(newName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Map<String, List<DataModel>> getDatasGroupByType() {
        HashMap<String, List<DataModel>> map = new HashMap<String, List<DataModel>>();
        for (DataModel<ConnectorTypeModel> data : datas) {
            String typeName = data.getType().getName();
            if (!map.containsKey(typeName)) {
                map.put(typeName, new ArrayList<DataModel>());
            }
            map.get(typeName).add(data);
        }
        return map;
    }
}


@SuppressWarnings({"unused", "rawtypes"})
@Root
class Interactor {

    @Element(required = false, name = "content")
    private PortTypeModel    content;
    @Element(required = false, name = "trigger")
    private Interactor   completePort;
    @ElementArray(required = false, name = "ports")
    private Interactor[] incompletePorts;

    public Interactor(@Element(name = "content") PortTypeModel content) {
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
