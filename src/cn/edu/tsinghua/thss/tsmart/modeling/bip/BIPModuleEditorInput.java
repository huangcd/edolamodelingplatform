package cn.edu.tsinghua.thss.tsmart.modeling.bip;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;

/**
 * Created by Huangcd<br/>
 * Date: 11-12-9<br/>
 * Time: ����12:38<br/>
 */
@SuppressWarnings("rawtypes")
public class BIPModuleEditorInput implements IEditorInput {

    private IModel model;

    public BIPModuleEditorInput(IModel model) {
        this.model = model;
    }

    public IModel getModel() {
        return model;
    }

    public void setModel(IModel model) {
        this.model = model;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        // TODO
        return null;
    }

    @Override
    public String getName() {
        if (model == null || model.getName().equals(model.getStringID())) {
            return "*untitled";
        } else {
            return model.getName();
        }
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return getName();
    }

    @Override
    public Object getAdapter(Class aClass) {
        return null;
    }

    @Override
    public int hashCode() {
        return model.hashCode();
    }    
}
