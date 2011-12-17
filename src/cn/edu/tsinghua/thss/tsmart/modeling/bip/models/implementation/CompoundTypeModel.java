package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentInstance;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IInstance;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ÏÂÎç9:15<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class CompoundTypeModel extends BaseTypeModel<CompoundTypeModel, CompoundModel, IContainer>
                implements
                    IContainer<CompoundTypeModel, IContainer, IInstance>,
                    IComponentType<CompoundTypeModel, CompoundModel, IContainer, IInstance> {

    @ElementList
    private List<IComponentInstance>                               components;
    @ElementList
    private List<ConnectorModel>                                   connectors;
    @ElementList
    private List<PriorityModel<CompoundTypeModel, ConnectorModel>> priorities;

    public CompoundTypeModel() {
        components = new ArrayList<IComponentInstance>();
        connectors = new ArrayList<ConnectorModel>();
        priorities = new ArrayList<PriorityModel<CompoundTypeModel, ConnectorModel>>();
    }

    @Override
    public List<IInstance> getChildren() {
        ArrayList<IInstance> children = new ArrayList<IInstance>();
        children.addAll(components);
        children.addAll(connectors);
        children.addAll(priorities);
        return children;
    }

    public void addComponent(IComponentInstance component) {
        components.add(component);
        firePropertyChange(CHILDREN);
    }

    public void addConnector(ConnectorModel connector) {
        connectors.add(connector);
        // TODO validate
        firePropertyChange(CHILDREN);
    }

    public void addPriority(PriorityModel<CompoundTypeModel, ConnectorModel> priorityModel) {
        priorities.add(priorityModel);
        // TODO validate
        firePropertyChange(CHILDREN);
    }

    public List<IComponentInstance> getComponents() {
        return components;
    }

    @Override
    public CompoundTypeModel addChild(IInstance child) {
        if (child instanceof IComponentInstance) {
            addComponent((IComponentInstance) child);
        } else if (child instanceof ConnectorModel) {
            addConnector((ConnectorModel) child);
        } else if (child instanceof PriorityModel) {
            addPriority((PriorityModel) child);
        } else {
            System.err.println("Invalidated child type to add:\n" + child);
        }
        return this;
    }

    @Override
    public boolean removeChild(IInstance iInstance) {
        if (iInstance instanceof IComponentInstance) {
            removeComponent((IComponentInstance) iInstance);
        } else if (iInstance instanceof ConnectorModel) {
            removeConnector((ConnectorModel) iInstance);
        } else if (iInstance instanceof PriorityModel) {
            removePriority((PriorityModel) iInstance);
        }
        return false;
    }

    public boolean removePriority(PriorityModel child) {
        if (child == null) {
            return false;
        }
        int index = priorities.indexOf(child);
        if (index < 0) {
            return false;
        }
        priorities.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removeConnector(ConnectorModel child) {
        if (child == null) {
            return false;
        }
        int index = connectors.indexOf(child);
        if (index < 0) {
            return false;
        }
        connectors.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    public boolean removeComponent(IComponentInstance child) {
        if (child == null) {
            return false;
        }
        int index = components.indexOf(child);
        if (index < 0) {
            return false;
        }
        components.remove(index);
        firePropertyChange(CHILDREN);
        return true;
    }

    @Override
    public CompoundModel createInstance() {
        if (instance == null) {
            instance = new CompoundModel().setType(this);
        }
        return instance;
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("compound type ").append(getName()).append('\n');
        for (IComponentInstance component : components) {
            buffer.append('\t').append(component.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (ConnectorModel connector : connectors) {
            buffer.append('\t').append(connector.exportToBip()).append('\n');
        }
        buffer.append('\n');
        for (ConnectorModel connector : connectors) {
            buffer.append('\t').append(connector.exportPort()).append('\n');
        }
        buffer.append('\n');
        for (PriorityModel<CompoundTypeModel, ConnectorModel> priority : priorities) {
            buffer.append('\t').append(priority.exportToBip()).append('\n');
        }
        buffer.append("end\n");
        return buffer.toString();
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public List<PortModel> getExportPorts() {
        List<PortModel> exportPorts = new ArrayList<PortModel>();
        for (ConnectorModel connector : connectors) {
            if (connector.isExport()) {
                exportPorts.add(connector.getExportPort());
            }
        }
        return exportPorts;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[] {new TextPropertyDescriptor(NAME, "compound name")};
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        }
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
