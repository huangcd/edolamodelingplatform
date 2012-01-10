package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.BaseTypeModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;

@SuppressWarnings({"rawtypes"})
public class BIPFileEditorInput implements IEditorInput {
    private File               path;
    private ComponentTypeModel model;

    public BIPFileEditorInput(File path) {
        this.path = path;
    }

    public ComponentTypeModel getModel() {
        if (model == null) {
            try {
                FileInputStream in = new FileInputStream(path);
                byte[] bytes = new byte[in.available()];
                in.read(bytes);
                in.close();
                model = BaseTypeModel.importFromBytes(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return model;
    }

    @Override
    public int hashCode() {
        return getModel().hashCode();
    }

    @Override
    public boolean exists() {
        return path.exists();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return BIPEditor.getImage("icons/file_obj.gif");
    }

    @Override
    public String getName() {
        if (getModel().getParent() != null) {
            return getModel().getParent().getName() + "." + path.getName();
        }
        return path.getName();
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return path.getAbsolutePath();
    }

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }
}
