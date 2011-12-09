package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;

import java.beans.PropertyChangeEvent;

import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPModuleEditorInput;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;

public class AtomicEditPart extends BaseEditableEditPart {
    private Label typeLabel;
    private Label instanceLabel;
    private Panel panel;

    public AtomicModel getModel() {
        return (AtomicModel) super.getModel();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (AtomicModel.TYPE_NAME.equals(evt.getPropertyName())) {
            typeLabel.setText(getModel().getType().getName());
        } else if (AtomicModel.NAME.equals(evt.getPropertyName())) {
            instanceLabel.setText(getModel().getName());
        }
        refresh();
    }

    public void refreshVisuals() {
        Rectangle constraint = getModel().getPositionConstraint();
        ((AbstractGraphicalEditPart) getParent())
                        .setLayoutConstraint(this, getFigure(), constraint);
    }

    @Override
    protected IFigure createFigure() {
        panel = new Panel();
        panel.setLayoutManager(new GridLayout(1, true));
        typeLabel = new Label(getModel().getType().getName());
        instanceLabel = new Label(getModel().getName());
        System.out.println(getModel().getPositionConstraint());
        panel.add(typeLabel);
        panel.add(instanceLabel);
        return panel;
    }

    @Override
    protected void createEditPolicies() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void performDoubleClick() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IWorkbenchPage page = window.getActivePage();
        try {
            // 如果页面已经打开，则跳转到指定页面
            for (IEditorReference reference : page.getEditorReferences()) {
                if (reference.getEditorInput() instanceof BIPModuleEditorInput) {
                    BIPModuleEditorInput editorInput =
                                    (BIPModuleEditorInput) reference.getEditorInput();
                    if (editorInput.getModel().equals(getModel().getType())) {
                        page.openEditor(editorInput,
                                        "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor");
                        return;
                    }
                }
            }
            page.openEditor(new BIPModuleEditorInput(getModel().getType()),
                            "cn.edu.tsinghua.thss.tsmart.modeling.bip.BIPEditor");
        } catch (PartInitException e) {
            MessageBox errorBox = new MessageBox(window.getShell());
            errorBox.setMessage(e.getMessage());
            errorBox.open();
        }
    }
}
