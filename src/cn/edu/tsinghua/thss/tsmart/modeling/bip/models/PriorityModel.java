package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class PriorityModel extends ListDataModel<PriorityAreaModel> {
    private PortModel                   left;
    private PortModel                   right;
    private PriorityAreaModel           parent;
    public final static String          LEFT      = "PriorityLeft";
    public final static String          RIGHT     = "PriorityRight";
    public final static String          CONDITION = "condition";
    public String                       condition = "true";
    private HashMap<Integer, PortModel> portIndexMap;

    public PriorityAreaModel getParent() {
        return parent;
    }

    public void setParent(PriorityAreaModel parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return String.format(" %s: %s → %s < %s;", getName(), condition,
                        left == null ? "" : left.getName(), right == null ? "" : right.getName());
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        portIndexMap = new HashMap<Integer, PortModel>();
        List<PortModel> ports = getParent().getParent().getPortAreaModel().getChildren();
        int length = ports.size();
        String[] portNames = new String[length + 1];
        for (int i = 0; i < length; i++) {
            portIndexMap.put(i, ports.get(i));
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
            for (Map.Entry<Integer, PortModel> entry : portIndexMap.entrySet()) {
                if (entry.getValue().equals(left)) return entry.getKey();
            }
            return portIndexMap.size();
        } else if (RIGHT.equals(id)) {
            for (Map.Entry<Integer, PortModel> entry : portIndexMap.entrySet()) {
                if (entry.getValue().equals(right)) return entry.getKey();
            }
            return portIndexMap.size();
        } else if (NAME.equals(id)) return getName();
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
            if (portIndexMap.containsKey(value))
                setLeft(portIndexMap.get(value));
            else
                setLeft(null);
        } else if (RIGHT.equals(id)) {
            if (portIndexMap.containsKey(value))
                setRight(portIndexMap.get(value));
            else
                setRight(null);
        }
    }

    public boolean nameExistsInParent(String name) {
        for (PriorityModel child : parent.getChildren()) {
            if (!child.equals(this) && child.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void setName(String name) {
        if (nameExistsInParent(name)) {
            return;
        }
        super.setName(name);
        firePropertyChange(NAME, null, name);
        getParent().childUpdated(this);
    }

    public PortModel getLeft() {
        return left;
    }

    public void setLeft(PortModel left) {
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

    public PortModel getRight() {
        return right;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        firePropertyChange(CONDITION, null, condition);
        getParent().childUpdated(this);
    }

    public void setRight(PortModel right) {
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
            portIndexMap = new HashMap<Integer, PortModel>();
            List<PortModel> ports = getParent().getParent().getPortAreaModel().getChildren();
            int length = ports.size();
            for (int i = 0; i < length; i++) {
                portIndexMap.put(i, ports.get(i));
            }
            PortModel tempLeft = null;
            PortModel tempRight = null;
            for (PortModel port : ports) {
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
}
