package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompoundPriorityModel extends BaseModel {
    private String                               name      = "";
    private ConnectorTypeModel                   left;
    private ConnectorTypeModel                   right;
    private CompoundPriorityAreaModel            parent;
    private String                               condition = "true";
    public final static String                   LEFT      = "PriorityLeft";
    public final static String                   RIGHT     = "PriorityRight";
    public final static String                   CONDITION = "condition";
    private HashMap<Integer, ConnectorTypeModel> connectorIndexMap;

    public CompoundPriorityAreaModel getParent() {
        return parent;
    }

    public void setParent(CompoundPriorityAreaModel parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return String.format(" %s: %s → %s < %s;", getName(), condition,
                        left == null ? "" : left.getName(), right == null ? "" : right.getName());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        connectorIndexMap = new HashMap<Integer, ConnectorTypeModel>();
        List<ConnectorTypeModel> ports = getParent().getParent().getConnectors();
        int length = ports.size();
        String[] portNames = new String[length + 1];
        for (int i = 0; i < length; i++) {
            connectorIndexMap.put(i, ports.get(i));
            portNames[i] = ports.get(i).getName();
        }
        portNames[length] = "NOT SET YET";
        ComboBoxPropertyDescriptor leftDescriptor =
                        new ComboBoxPropertyDescriptor(LEFT, "left port", portNames);
        ComboBoxPropertyDescriptor rightDescriptor =
                        new ComboBoxPropertyDescriptor(RIGHT, "right port", portNames);
        IPropertyDescriptor[] descriptors =
                        new IPropertyDescriptor[] {leftDescriptor,
                                        new TextPropertyDescriptor(NAME, "name"), rightDescriptor,};
        return descriptors;
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (LEFT.equals(id)) {
            for (Map.Entry<Integer, ConnectorTypeModel> entry : connectorIndexMap.entrySet()) {
                if (entry.getValue().equals(left)) return entry.getKey();
            }
            return connectorIndexMap.size();
        } else if (RIGHT.equals(id)) {
            for (Map.Entry<Integer, ConnectorTypeModel> entry : connectorIndexMap.entrySet()) {
                if (entry.getValue().equals(right)) return entry.getKey();
            }
            return connectorIndexMap.size();
        } else if (NAME.equals(id)) return name;
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        if (LEFT.equals(id) || RIGHT.equals(id) || NAME.equals(id)) return true;
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id))
            setName((String) value);
        else if (LEFT.equals(id)) {
            if (connectorIndexMap.containsKey(value))
                setLeft(connectorIndexMap.get(value));
            else
                setLeft(null);
        } else if (RIGHT.equals(id)) {
            if (connectorIndexMap.containsKey(value))
                setRight(connectorIndexMap.get(value));
            else
                setRight(null);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        firePropertyChange(NAME, null, name);
        getParent().childUpdated(this);
    }

    public ConnectorTypeModel getLeft() {
        return left;
    }

    public void setLeft(ConnectorTypeModel left) {
        if (left != null) {
            left.register(this);
        }
        if (this.left != null) {
            this.left.unRegister(this);
        }
        this.left = left;
        firePropertyChange(LEFT, null, left);
        getParent().childUpdated(this);
    }

    public ConnectorTypeModel getRight() {
        return right;
    }

    public void setRight(ConnectorTypeModel right) {
        if (right != null) {
            right.register(this);
        }
        if (this.right != null) {
            this.right.unRegister(this);
        }
        this.right = right;
        firePropertyChange(RIGHT, null, right);
        getParent().childUpdated(this);
    }

    // 从字符串中解析出port
    public boolean setProperties(String s) {
        String pattern = "(.*):(.*)<(.*);?";
        Pattern transitionPattern = Pattern.compile(pattern);
        Matcher m = transitionPattern.matcher(s);
        if (m.matches()) {
            this.setName(m.group(1).trim());
            String leftName = m.group(2).trim();
            String rigthName = m.group(3).trim();
            connectorIndexMap = new HashMap<Integer, ConnectorTypeModel>();
            List<ConnectorTypeModel> ports = getParent().getParent().getConnectors();
            int length = ports.size();
            for (int i = 0; i < length; i++) {
                connectorIndexMap.put(i, ports.get(i));
            }
            ConnectorTypeModel tempLeft = null;
            ConnectorTypeModel tempRight = null;
            for (ConnectorTypeModel port : ports) {
                if (port.getName().equals(leftName)) tempLeft = port;
                if (port.getName().equals(rigthName)) tempRight = port;
            }
            this.setLeft(tempLeft);
            this.setRight(tempRight);
            return true;
        }
        return false;
    }

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("priority");
        element.addAttribute("name", getName());
        // TODO 考虑是使用name还是id
        element.addAttribute("left", getLeft().getName());
        element.addAttribute("right", getRight().getName());
        return element;
    }

    @Override
    public String toBIP() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BaseModel fromXML() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        firePropertyChange(CONDITION, null, condition);
        getParent().childUpdated(this);
    }
}
