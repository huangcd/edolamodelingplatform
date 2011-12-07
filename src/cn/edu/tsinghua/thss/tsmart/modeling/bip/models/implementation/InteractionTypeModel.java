package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.CycleStrategy;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: ÏÂÎç3:01<br/>
 */
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
            return serializer
                .read(InteractionTypeModel.class, new ByteArrayInputStream(out.toByteArray()));
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
    public Object getEditableValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getPropertyValue(Object id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPropertyValue(Object id, Object value) {
        // TODO Auto-generated method stub
        
    }
}
