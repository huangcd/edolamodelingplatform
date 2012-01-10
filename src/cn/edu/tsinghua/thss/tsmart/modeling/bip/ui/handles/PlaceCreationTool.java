package cn.edu.tsinghua.thss.tsmart.modeling.bip.ui.handles;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

public class PlaceCreationTool extends CreationTool {

    public PlaceCreationTool() {
        super();
        setUnloadWhenFinished(false);
    }

    public PlaceCreationTool(CreationFactory aFactory) {
        this();
        setFactory(aFactory);
    }
}
