package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.UnboundedException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel.ArgumentEntry;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class PortModel<P extends IContainer> extends BaseInstanceModel<PortModel, PortTypeModel, P> {
    private static final long serialVersionUID = -1192079654784887960L;
    @Element
    private boolean           export;
    private BulletModel       bullet;

    protected PortModel() {
        bullet = new BulletModel(this);
    }

    public BulletModel getBullet() {
        return bullet;
    }

    public boolean isExport() {
        return export;
    }

    public PortModel setExport(boolean export) {
        if (this.export == export) {
            return this;
        }
        // 如果portModel是export的，需要把AtomicModel添加到portModel的属性变化通知队列中去
        if (export) {
            addPropertyChangeListener(((IType) getParent()).getInstance());
            addPropertyChangeListener(((IType) getParent()).getInstance()).getParent();
            addPropertyChangeListener(bullet);
        }
        this.export = export;
        firePropertyChange(EXPORT_PORT, !export, export);
        if (!export) {
            removePropertyChangeListener(((BaseTypeModel) getParent()).getInstance());
            removePropertyChangeListener(bullet);
        }
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    public String getParentName() {
        if (getParent() instanceof AtomicTypeModel) {
            return ((IType) getParent()).getInstance().getName();
        } else if (getParent() instanceof ConnectorTypeModel) {
            return ((IType) getParent()).getInstance().getParent().getName();
        } else {
            return "$UNKNOWN_COMPONENT$";
        }
    }

    /**
     * 绑定一个参数。如果argument为null，表示取消绑定
     * 
     * @param index 参数位置
     * @param argument 参数
     * @return 模型自身
     */
    public PortModel bound(int index, DataModel argument) {
        DataModel oldArgument = getType().getData(index);
        if (oldArgument != null) {
            oldArgument.removePropertyChangeListener(this);
        }
        if (argument != null) {
            argument.addPropertyChangeListener(this);
            getType().bound(index, (DataTypeModel) argument.getType());
        } else {
            getType().unbound(index);
        }
        return this;
    }

    public String getBoundedArguments() {
        StringBuilder buffer = new StringBuilder();

        if (!getType().getArgumentEntries().isEmpty()) {
            for (ArgumentEntry entry : getType().getArgumentEntries()) {
                if (!entry.isBounded())
                    throw new UnboundedException(getType().getName(), getName(), entry.getIndex());
                DataModel data = (DataModel) entry.getModel().getInstance();
                buffer.append(data.getName()).append(", ");
            }
            buffer.setLength(buffer.length() - 2);
        }
        return buffer.toString();
    }

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        if (isExport()) {
            buffer.append("export ");
        }
        buffer.append("port ").append(getName()).append('(');
        buffer.append(getBoundedArguments());
        buffer.append(")");
        return buffer.toString();
    }

    @SuppressWarnings("unchecked")
    public List<DataModel<IDataContainer>> getPortArguments() {
        ArrayList<DataModel<IDataContainer>> arguments = new ArrayList<DataModel<IDataContainer>>();
        for (DataTypeModel dataTypeModel : getType().getChildren()) {
            arguments.add((DataModel<IDataContainer>) dataTypeModel.getInstance());
        }
        return arguments;
    }

    private transient Map<String, List<DataModel>> map;

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor pName = new TextPropertyDescriptor(NAME, "端口名");
        pName.setDescription("01");
        list.add(pName);
        ComboBoxPropertyDescriptor isExport =
                        new ComboBoxPropertyDescriptor(EXPORT_PORT, "是否导出", TRUE_FALSE_ARRAY);
        isExport.setDescription("02");
        list.add(isExport);
        map = ((AtomicTypeModel) getParent()).getDatasGroupByType();
        int i = 1;
        ComboBoxPropertyDescriptor arg;
        for (ArgumentEntry entry : getType().getArgumentEntries()) {
            String typeName = entry.getTypeName();
            List<DataModel> datas = map.get(typeName);
            if (datas == null) datas = Collections.EMPTY_LIST;
            String[] values = new String[datas.size() + 1];
            int index = 0;
            for (DataModel data : datas) {
                values[index] = data.getName();
                index++;
            }
            values[index] = "$UNBOUNDED$";
            String name = "参数" + (i++) + "_" + entry.getName();
            arg = new ComboBoxPropertyDescriptor(entry, name, values);
            arg.setDescription("0" + (i + 2));
            list.add(arg);
        }
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "标签");
        tag.setDescription("0" + (i + 3));
        list.add(tag);
        return list.toArray(new IPropertyDescriptor[list.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return getName();
        }
        if (EXPORT_PORT.equals(id)) {
            return Boolean.toString(export).equals(TRUE_FALSE_ARRAY[0]) ? 0 : 1;
        }
        if (id instanceof ArgumentEntry) {
            ArgumentEntry entry = (ArgumentEntry) id;
            String name = entry.getTypeName();
            List<DataModel> datas = map.get(name);
            if (datas == null) datas = Collections.EMPTY_LIST;
            if (!entry.isBounded()) {
                return (Integer) datas.size();
            } else {
                return datas.indexOf(entry.getModel().getInstance());
            }
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || EXPORT_PORT.equals(id) || ENTITY.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (EXPORT_PORT.equals(id)) {
            setExport(Boolean.parseBoolean(TRUE_FALSE_ARRAY[(Integer) value]));
        } else if (id instanceof ArgumentEntry) {
            ArgumentEntry entry = (ArgumentEntry) id;
            String name = entry.getTypeName();
            List<DataModel> datas = map.get(name);
            if (datas == null) datas = Collections.EMPTY_LIST;
            int index = (Integer) value;
            if (index == datas.size()) {
                bound(entry.getIndex(), null);
            } else if (index >= 0 && index < datas.size()) {
                bound(entry.getIndex(), datas.get(index));
            }
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>)value);
        }
    }

    public String getFriendlyString() {
        StringBuilder buffer = new StringBuilder();
        if (isExport()) buffer.append("export ");
        buffer.append(getType().getName()).append(' ').append(getName()).append('(');
        buffer.append(getType().getFriendlyArguments()).append(')');
        return buffer.toString();
    }
}
