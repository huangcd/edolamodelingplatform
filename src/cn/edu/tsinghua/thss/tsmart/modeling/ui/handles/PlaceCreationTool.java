package cn.edu.tsinghua.thss.tsmart.modeling.ui.handles;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

public class PlaceCreationTool extends CreationTool {

    /**
     * Default constructor. Sets the default and disabled cursors.
     */
    public PlaceCreationTool() {
        super();
        setUnloadWhenFinished(false);// 唯一的修改
    }

    /**
     * Constructs a new CreationTool with the given factory.
     * 
     * @param aFactory the creation factory
     */
    public PlaceCreationTool(CreationFactory aFactory) {
        this(); // TO lynn: 调用这个的时候已经设置了setUnloadWhenFinished(false)，所以后面不用再设置了。
        setFactory(aFactory);
    }
}
