package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.exceptions.DataValueNotMatchException;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.descriptors.EntitySelectionPropertyDescriptor;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: ����3:19<br/>
 * DataType ���� Atomic Type �� Connector Type �������
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DataModel<Parent extends IDataContainer>
                extends BaseInstanceModel<DataModel, DataTypeModel, Parent> {

    private static final long serialVersionUID = 5148651288712084524L;
    @Element(required = false)
    private String            value;

    protected DataModel() {
        this.value = "";
    }

    public String getValue() {
        return value;
    }

    public boolean setValue(String value) {
        String oldValue = getValue();
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
        // �����ⲻͨ������ԭԭ����ֵ��������false
        if (!validateOnTheFly()) {
            this.value = oldValue;
            return false;
        }
        firePropertyChange(DATA_VALUE);
        return true;
    }

    @Override
    public boolean exportable() {
        return true;
    }

    @Override
    public String exportToBip() {
        return "data " + getFriendlyString();
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
        if (!getProperties().isMultipleDataTypeAvailble()) {
            // �����bug����ComboBox�ĳ�Text�ģ�������getPropertyValue��ʵ�֡�
            valueDescriptor = new ComboBoxPropertyDescriptor(DATA_VALUE, "ֵ", TRUE_FALSE_ARRAY);
        } else {
            valueDescriptor = new TextPropertyDescriptor(DATA_VALUE, "ֵ");

        }
        ArrayList<IPropertyDescriptor> properties = new ArrayList<IPropertyDescriptor>();
        TextPropertyDescriptor name = new TextPropertyDescriptor(NAME, "������");
        name.setDescription("01");
        properties.add(name);
        ((PropertyDescriptor) valueDescriptor).setDescription("02");
        properties.add(valueDescriptor);
        EntitySelectionPropertyDescriptor tag = new EntitySelectionPropertyDescriptor(ENTITY, "��ǩ");
        tag.setDescription("03");
        properties.add(tag);
        return properties.toArray(new IPropertyDescriptor[properties.size()]);
    }

    @Override
    public Object getPropertyValue(Object id) {
        if (DATA_VALUE.equals(id)) {
            if (!getProperties().isMultipleDataTypeAvailble()) {
                return getValue().equals(TRUE_FALSE_ARRAY[0]) ? 0 : 1;
            }
            return getValue();
        }
        if (NAME.equals(id)) {
            return hasName() ? getName() : "";
        }
        if (ENTITY.equals(id)) {
            return getEntityNames();
        }
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return DATA_VALUE.equals(id) || NAME.equals(id) || ENTITY.equals(id);
    }

    @Override
    public void setPropertyValue(Object id, Object value) {

        if (DATA_VALUE.equals(id)) {
            if (value instanceof Integer) {
                setValue(TRUE_FALSE_ARRAY[(Integer) value]);
            } else {
                setValue((String) value);
            }
        } else if (NAME.equals(id)) {
            setName((String) value);
        } else if (ENTITY.equals(id)) {
            setEntityNames((ArrayList<String>)value);
        }
    }
}
