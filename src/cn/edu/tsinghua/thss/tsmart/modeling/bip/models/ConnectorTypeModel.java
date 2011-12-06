package cn.edu.tsinghua.thss.tsmart.modeling.bip.models;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectorTypeModel extends BaseModel {
    private String                   name;
    private String                   defineExpression;
    private List<DataModel>          datas             = new ArrayList<DataModel>();
    private List<DataModel>          associateDatas    = new ArrayList<DataModel>();
    private List<InteractionModel>   interactions      = new ArrayList<InteractionModel>();
    private PortModel                exportPort        = new PortModel();
    private BulletModel              shape;
    private boolean                  isExport;
    private CompoundTypeModel        parent;
    public final static String       SOURCE_CONNECTION = "ConnectorSourceConnection";
    public final static String       ACTION            = "ConnectorAction";
    public final static String       EXPORT            = "ConnectorExport";

    private List<ConnectorPortModel> sourceConnections = new ArrayList<ConnectorPortModel>();
    private final static Pattern     portNamePattern   = Pattern.compile("^p(\\d+)");

    public String toString() {
        return "export port " + name;
    }

    public String getDefaultConnectionPortName() {
        String prefix = "p";
        int maxValue = 0;
        for (ConnectorPortModel connection : sourceConnections) {
            String name = connection.getName();
            Matcher mat = portNamePattern.matcher(name);
            if (mat.matches()) {
                maxValue = Math.max(maxValue, Integer.parseInt(mat.group(1)) + 1);
            }
        }
        return prefix + maxValue;
    }

    public List<ConnectorPortModel> getSourceConnections() {
        return sourceConnections;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        exportPort.setName(name);
        firePropertyChange(REFRESH, null, null);
        notifyRegisterObjects();
    }

    public boolean isExport() {
        return isExport;
    }

    public void setExport(boolean isExport) {
        if (isExport != this.isExport) {
            if (isExport) {
                if (shape == null) {
                    shape = new BulletModel(parent, this);
                    int diameter = 2 * BaseModel.BULLET_RADIUS;
                    shape.setPositionConstraint(new Rectangle(0, 40, diameter, diameter));
                    this.register(shape);
                }
                parent.addChild(shape);
            } else {
                parent.removeChild(shape);
            }
            this.isExport = isExport;
            firePropertyChange(EXPORT, null, isExport);
            notifyRegisterObjects();
        }
    }

    public BulletModel getShape() {
        return shape;
    }

    public void setShape(BulletModel shape) {
        this.shape = shape;
    }

    public void addSourceConnection(ConnectorPortModel source) {
        sourceConnections.add(source);
        firePropertyChange(SOURCE_CONNECTION, null, null);
    }

    public void removeSourceConnection(ConnectorPortModel source) {
        sourceConnections.remove(source);
        firePropertyChange(SOURCE_CONNECTION, null, null);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id))
            return name;
        else if (EXPORT.equals(id)) {
            if (isExport)
                return 1;
            else
                return 0;
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || ACTION.equals(id) || EXPORT.equals(id);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (EXPORT.equals(id)) {
            int index = (Integer) value;
            if (index == 0) {
                setExport(false);
            } else if (index == 1) {
                setExport(true);
            } else {
                System.err.printf("Unknown index %d in ConnectorTypeModel\n", index);
            }
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {
                        new TextPropertyDescriptor(NAME, "name"),
                        new TextPropertyDescriptor(ACTION, "actions"),
                        new ComboBoxPropertyDescriptor(EXPORT, "is export port", new String[] {
                                        "false", "true"}),};
    }

    public CompoundTypeModel getParent() {
        return parent;
    }

    public void setParent(CompoundTypeModel parent) {
        this.parent = parent;
    }

    public String getDefineExpression() {
        return defineExpression;
    }

    public void setDefineExpression(String defineExpression) {
        this.defineExpression = defineExpression;
    }

    @Override
    public Element toXML() {
        Element element = DocumentHelper.createElement("connectorType");
        element.addAttribute("name", getName());
        element.addAttribute("id", getId());
        Element portArgs = element.addElement("portArgs");
        for (ConnectorPortModel portArgModel : getSourceConnections()) {
            portArgs.add(portArgModel.toXML());
        }
        element.addElement("defineConnectorExpression").setText(defineExpression);
        Element datas = element.addElement("datas");
        for (DataModel data : getDatas()) {
            datas.add(data.toXML());
        }
        Element interactions = element.addElement("interactions");
        for (InteractionModel interaction : getInteractions()) {
            interactions.add(interaction.toXML());
        }
        if (isExport()) {
            element.add(exportPort.toXML());
        }
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

    public List<DataModel> getDatas() {
        return datas;
        // TODO PropertyChange and updateNotify
    }

    public void addData(DataModel data) {
        datas.add(data);
        // TODO PropertyChange and updateNotify
    }

    public void clearDatas() {
        datas.clear();
        // TODO PropertyChange and updateNotify
    }

    public List<DataModel> getAssociateDatas() {
        return associateDatas;
        // TODO PropertyChange and updateNotify
    }

    public void clearAssociateDatas() {
        associateDatas.clear();
        // TODO PropertyChange and updateNotify
    }

    public void setAssociateDatas(List<DataModel> associateDatas) {
        this.associateDatas = associateDatas;
        exportPort.setDatas(associateDatas);
        // TODO PropertyChange and updateNotify
    }

    public void clearInteractions() {
        interactions.clear();
    }

    public void addInteraction(InteractionModel interaction) {
        interactions.add(interaction);
    }

    public List<InteractionModel> getInteractions() {
        return interactions;
        // TODO PropertyChange and updateNotify
    }

    public void setInteractions(List<InteractionModel> interactions) {
        this.interactions = interactions;
        // TODO PropertyChange and updateNotify
    }

    public PortModel getExportPort() {
        return exportPort;
    }

    public void setExportPort(PortModel exportPort) {
        this.exportPort = exportPort;
    }
}
