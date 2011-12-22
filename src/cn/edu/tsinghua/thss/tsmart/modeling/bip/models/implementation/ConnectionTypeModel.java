package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.simpleframework.xml.Element;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-7<br/>
 * Time: ÏÂÎç7:23<br/>
 */
@SuppressWarnings("rawtypes")
@Element
public class ConnectionTypeModel
                extends BaseTypeModel<ConnectionTypeModel, ConnectionModel, IContainer> {

    @Override
    public ConnectionModel createInstance() {
        if (instance == null) {
            instance = new ConnectionModel().setType(this);
        }
        return instance;
    }

    @Override
    public ConnectionTypeModel copy() {
        return new ConnectionTypeModel();
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}
}
