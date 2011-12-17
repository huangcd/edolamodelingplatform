package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: ÏÂÎç3:01<br/>
 */
@SuppressWarnings("rawtypes")
@Root
public class InteractionTypeModel
                extends BaseTypeModel<InteractionTypeModel, InteractionModel, IContainer> {

    @Override
    public InteractionModel createInstance() {
        if (instance == null) {
            instance = new InteractionModel().setType(this);
        }
        return instance;
    }

    @Override
    public InteractionTypeModel copy() {
        try {
            getInstance();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Serializer serializer = new Persister(new CycleStrategy());
            serializer.write(this, out);
            return serializer.read(InteractionTypeModel.class,
                            new ByteArrayInputStream(out.toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
