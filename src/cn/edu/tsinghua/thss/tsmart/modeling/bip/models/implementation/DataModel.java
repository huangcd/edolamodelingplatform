package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.DataValueNotMatchException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;


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
        String typeString = getType().getTypeName();
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
            return String.format("data %s %s", getType().getTypeName(), getName());
        } else {
            return String.format("data %s %s = %s", getType().getTypeName(), getName(), getValue());
        }
    }

    public String getFriendlyString() {
        if (value == null || value.isEmpty()) {
            return String.format("%s %s", getType().getTypeName(), getName());
        } else {
            return String.format("%s %s = %s", getType().getTypeName(), getName(), getValue());
        }
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        if (!getProrerties().isMultipleDataTypeAvailble()) {
            // TODO 如果有bug，把ComboBox改成Text的，并更改getPropertyValue的实现。
            return new IPropertyDescriptor[] {new TextPropertyDescriptor(IModel.NAME, "name"),
                            new ComboBoxPropertyDescriptor(DATA_VALUE, "value", trueFalseArray)};
        } else {
            return new IPropertyDescriptor[] {new TextPropertyDescriptor(DATA_TYPE, "type"),
                            new TextPropertyDescriptor(IModel.NAME, "name"),
                            new TextPropertyDescriptor(DATA_VALUE, "value")};
        }
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (DATA_TYPE.equals(id)) {
            return getType().getTypeName();
        }
        if (DATA_VALUE.equals(id)) {
            if (!getProrerties().isMultipleDataTypeAvailble()) {
                return getValue().equals(trueFalseArray[0]) ? 0 : 1;
            }
            return getValue();
        }
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return DATA_TYPE.equals(id) || DATA_VALUE.equals(id) || NAME.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        if (DATA_TYPE.equals(id)) {
            getType().setTypeName((String) value);
        }
        if (DATA_VALUE.equals(id)) {
            if (value instanceof Integer) {
                setValue(trueFalseArray[(Integer) value]);
            } else {
                setValue((String) value);
            }
        }
        if (NAME.equals(id)) {
            setName((String) value);
        }
    }
}
