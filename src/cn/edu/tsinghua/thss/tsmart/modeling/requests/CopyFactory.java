package cn.edu.tsinghua.thss.tsmart.modeling.requests;

import org.eclipse.gef.requests.CreationFactory;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

@SuppressWarnings("rawtypes")
public class CopyFactory implements CreationFactory {
    IModel model;

    public CopyFactory(IModel model) {
        this.model = model;
    }

    @Override
    public Object getNewObject() {
        return model.copy();
    }

    @Override
    public Object getObjectType() {
        return model.getClass();
    }

}
