package cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.atomic;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.editors.BipContextMenuProvider;

public class AtomicEditorContextMenuProvider extends BipContextMenuProvider {
    public AtomicEditorContextMenuProvider(EditPartViewer viewer, ActionRegistry registry) {
        super(viewer, registry);
    }
}
