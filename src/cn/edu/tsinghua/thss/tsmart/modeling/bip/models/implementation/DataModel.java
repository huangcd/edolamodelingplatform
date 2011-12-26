package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.DataValueNotMatchException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:19<br/>
 * DataType 会在 Atomic Type 和 Connector Type 里面出现
 */
@SuppressWarnings("rawtypes")
public class DataModel<Parent extends IDataContainer>
                extends BaseInstanceModel<DataModel, DataTypeModel, Parent> {

    public final static String DATA_TYPE  = "dataType";
    public final static String DATA_VALUE = "dataValue";
    @Element(required = false)
    private String             value;

    protected DataModel() {
        this.value = "";
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        String typeString = getType().getName();
        if (typeString.equals("int")) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                throw new DataValueNotMatchException("int", value);
            }
        }
        if (typeString.equals("bool")) {
            value = value.toLowerCase();
            if (!value.equals("true") && !value.equals("false")) {
                throw new DataValueNotMatchException("bool", value);
            }
        }
        this.value = value;
        firePropertyChange(DATA_VALUE);
        return true;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        if (value == null || value.isEmpty()) {
            return String.format("data %s %s", getType().getName(), getName());
        } else {
            return String.format("data %s %s = %s", getType().getName(), getName(), getValue());
        }
    }

    public String getFriendlyString() {
        if (value == null || value.isEmpty()) {
            return String.format("%s %s", getType().getName(), getName());
        } else {
            return String.format("%s %s = %s", getType().getName(), getName(), getValue());
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        IPropertyDescriptor valueDescriptor = null;
        if (!getProrerties().isMultipleDataTypeAvailble()) {
            // 如果有bug，把ComboBox改成Text的，并更改getPropertyValue的实现。
            valueDescriptor = new ComboBoxPropertyDescriptor(DATA_VALUE, "值", trueFalseArray);
        } else {
            valueDescriptor = new TextPropertyDescriptor(DATA_VALUE, "值");

        }
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "变量名");
        name.setDescription("01");
        properties.add(name);
        ((PropertyDescriptor) valueDescriptor).setDescription("02");
        properties.add(valueDescriptor);
        ComboBoxPropertyDescriptor tag = new ComboBoxPropertyDescriptor(TAG, "标签", DATA_TAGS);
        tag.setDescription("03");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (DATA_VALUE.equals(id)) {
            if (!getProrerties().isMultipleDataTypeAvailble()) {
                return getValue().equals(trueFalseArray[0]) ? 0 : 1;
            }
            return getValue();
        }
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (TAG.equals(id)) {
            return getTag() == null ? 0 : Arrays.asList(DATA_TAGS).indexOf(getTag());
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return DATA_VALUE.equals(id) || NAME.equals(id) || TAG.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {

        if (DATA_VALUE.equals(id)) {
            if (value instanceof Integer) {
                setValue(trueFalseArray[(Integer) value]);
            } else {
                setValue((String) value);
            }
        } else if (NAME.equals(id)) {
            setName((String) value);
        } else if (TAG.equals(id)) {
            int index = (Integer) value;
            if (index == 0)
                setTag(null);
            else
                setTag(DATA_TAGS[index]);
        }
    }
}
