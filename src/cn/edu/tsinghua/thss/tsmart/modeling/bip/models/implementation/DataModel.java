package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IDataContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:19<br/>
 * DataType 会在 Atomic Type 和 Connector Type 里面出现
 */
public class DataModel<Parent extends IDataContainer>
    extends BaseInstanceModel<DataModel, DataTypeModel, Parent> {

    @Element(required = false)
    private String value;

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
                return false;
            }
        }
        if (typeString.equals("boolean")) {
            value = value.toLowerCase();
            if (!value.equals("true") && !value.equals("false")) {
                return false;
            }
        }
        this.value = value;
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
}
