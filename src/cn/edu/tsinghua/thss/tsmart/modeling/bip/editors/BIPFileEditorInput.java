package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IPersistableElement;

@SuppressWarnings({"rawtypes"})
public class BIPFileEditorInput implements IPathEditorInput {

    private IPath path;

    public BIPFileEditorInput(IPath path) {
        this.path = path;
    }

    @Override
    public boolean exists() {
        return path.toFile().exists();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    @Override
    public String getName() {
        return path.toString();
    }

    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    @Override
    public String getToolTipText() {
        return path.toString();
    }

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }

    @Override
    public IPath getPath() {
        return path;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }
}