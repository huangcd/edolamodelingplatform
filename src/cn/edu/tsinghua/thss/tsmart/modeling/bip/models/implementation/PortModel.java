package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IComponentType;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IPort;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.PortTypeModel.ChildEntry;

@SuppressWarnings({"unchecked", "unused", "rawtypes"})
@Root
public class PortModel<P extends IComponentType>
                extends BaseInstanceModel<PortModel, PortTypeModel, P>
                implements
                    IPort<PortModel, PortTypeModel, P, IDataContainer> {

    public final static String PORT   = "port";
    public final static String EXPORT = "export";
    @Element
    private boolean            export;
    private BulletModel        bullet;

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
            addPropertyChangeListener(getParent().getInstance());
        } else {
            removePropertyChangeListener(getParent().getInstance());
        }
        this.export = export;
        firePropertyChange(EXPORT);
        return this;
    }

    @Override
    public boolean exportable() {
        return true;
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

    @Override
    public String exportToBip() {
        StringBuilder buffer = new StringBuilder();
        if (isExport()) {
            buffer.append("export ");
        }
        buffer.append("port ").append(getName()).append('(');
        List<DataModel<IDataContainer>> datas = getPortArguments();
        if (!datas.isEmpty()) {
            buffer.append(datas.get(0).getName());
            for (int i = 1, size = datas.size(); i < size; i++) {
                buffer.append(", ").append(datas.get(i).getName());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DataModel<IDataContainer>> getPortArguments() {
        ArrayList<DataModel<IDataContainer>> arguments = new ArrayList<DataModel<IDataContainer>>();
        for (DataTypeModel dataTypeModel : getType().getChildren()) {
            arguments.add((DataModel<IDataContainer>) dataTypeModel.getInstance());
        }
        return arguments;
    }

    private Map<String, List<DataModel>> map;

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        ArrayList<IPropertyDescriptor> list = new ArrayList<IPropertyDescriptor>();
        list.add(new TextPropertyDescriptor(NAME, "name"));
        list.add(new ComboBoxPropertyDescriptor(EXPORT, "exportable", trueFalseArray));
        map = ((AtomicTypeModel) getParent()).getDatasGroupByType();
        int i = 1;
        for (ChildEntry entry : getType().getArgumentEntries()) {
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
            String name = "arg" + (i++) + "_" + entry.getName();
            list.add(new ComboBoxPropertyDescriptor(entry, name, values));
        }
        return list.toArray(new IPropertyDescriptor[list.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (NAME.equals(id)) {
            return getName();
        }
        if (EXPORT.equals(id)) {
            return Boolean.toString(export).equals(trueFalseArray[0]) ? 0 : 1;
        }
        if (id instanceof ChildEntry) {
            ChildEntry entry = (ChildEntry) id;
            String name = entry.getTypeName();
            List<DataModel> datas = map.get(name);
            if (datas == null) datas = Collections.EMPTY_LIST;
            if (!entry.isBounded()) {
                return (Integer) datas.size();
            } else {
                return datas.indexOf(entry.getModel().getInstance());
            }
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return NAME.equals(id) || EXPORT.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (NAME.equals(id)) {
            setName((String) value);
        } else if (EXPORT.equals(id)) {
            setExport(Boolean.parseBoolean(trueFalseArray[(Integer) value]));
        } else if (id instanceof ChildEntry) {
            ChildEntry entry = (ChildEntry) id;
            String name = entry.getTypeName();
            List<DataModel> datas = map.get(name);
            if (datas == null) datas = Collections.EMPTY_LIST;
            int index = (Integer) value;
            if (index == datas.size()) {
                bound(entry.getIndex(), null);
            } else if (index >= 0 && index < datas.size()) {
                bound(entry.getIndex(), datas.get(index));
            }
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
