package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts.outline;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BIPEditor;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentModel;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.ComponentTypeModel;

@SuppressWarnings("rawtypes")
public abstract class BaseTreeEditPart extends AbstractTreeEditPart
                implements
                    PropertyChangeListener {

    // override
    public void activate() {
        super.activate();
        getModel().addPropertyChangeListener(this);
        Listener listener = new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.type == SWT.MouseDoubleClick) {
                    if (getModel() instanceof ComponentTypeModel) {
                        BIPEditor.openBIPEditor((ComponentTypeModel) getModel());
                    } else if (getModel() instanceof ComponentModel) {
                        BIPEditor.openBIPEditor((ComponentTypeModel) ((ComponentModel) getModel())
                                        .getType());
                    }
                } else if (event.type == SWT.MouseDown) {
                }
            }
        };
        if (getWidget() instanceof TreeItem) {
            TreeItem item = (TreeItem) getWidget();
            item.addListener(SWT.MouseDoubleClick, listener);
            item.addListener(SWT.MouseDown, listener);
        }
    }

    public IModel getModel() {
        return (IModel) super.getModel();
    }

    // override
    public void deactivate() {
        super.deactivate();
        getModel().removePropertyChangeListener(this);
    }

    public void propertyChange(PropertyChangeEvent evt) {}

    @Override
    public void refresh() {}
}
