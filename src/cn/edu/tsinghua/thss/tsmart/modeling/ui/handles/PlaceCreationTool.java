package cn.edu.tsinghua.thss.tsmart.modeling.ui.handles;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.tools.CreationTool;

public class PlaceCreationTool extends CreationTool {

    /**
     * Default constructor. Sets the default and disabled cursors.
     */
    public PlaceCreationTool() {
        super();
        setUnloadWhenFinished(false);// Ψһ���޸�
    }

    /**
     * Constructs a new CreationTool with the given factory.
     * 
     * @param aFactory the creation factory
     */
    public PlaceCreationTool(CreationFactory aFactory) {
        this(); // TO lynn: ���������ʱ���Ѿ�������setUnloadWhenFinished(false)�����Ժ��治���������ˡ�
        setFactory(aFactory);
    }
}
