package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.CompoundTypeModel;

/**
 * Created by Huangcd<br/>
 * Date: 11-12-9<br/>
 * Time: ÏÂÎç12:38<br/>
 */
@SuppressWarnings("rawtypes")
public class BIPModelEditorInput implements IEditorInput {

    private ComponentTypeModel model;

    public BIPModelEditorInput(ComponentTypeModel model) {
        this.model = model;
    }

    public ComponentTypeModel getModel() {
        return model;
    }

    public void setModel(ComponentTypeModel model) {
        this.model = model;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        if (model == null) {
            return null;
        }
        if (model instanceof AtomicTypeModel) {
            return BIPEditor.getImage("icons/atomic_16.png"); //$NON-NLS-1$
        } else if (model instanceof CompoundTypeModel) {
            return BIPEditor.getImage("icons/compound_16.png"); //$NON-NLS-1$
        }
        return null;
    }

    @Override
    public String getName() {
        if (model == null || model.getName().equals(IModel.UNNAMED_NAME)) {
            return Messages.BIPModelEditorInput_1;
        } else if (getModel().getParent() != null) {
            return getModel().getParent().getName() + "." + model.getName(); //$NON-NLS-1$
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
