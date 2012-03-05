package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings("all")
@Root
public class DiamondModel extends BaseInstanceModel<DiamondModel, IType, IContainer> {

    private static final long serialVersionUID = -981697852340971872L;

    @Element
    private ConnectionModel   targetConnection;
    @Element
    private ConnectionModel   sourceConnection;

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}

    public List<ConnectionModel> getTargetConnections() {
        if (targetConnection == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(targetConnection);
    }

    public List<ConnectionModel> getSourceConnections() {
        if (sourceConnection == null) {
            return Collections.EMPTY_LIST;
        }
        return Arrays.asList(sourceConnection);
    }
    /**
     * @return 以此DiamondModel为target的ConnectionModel
     */
    public ConnectionModel getTargetConnection() {
        return targetConnection;
    }

    public void setTargetConnection(ConnectionModel targetConnection) {
        this.targetConnection = targetConnection;
        firePropertyChange(TARGET);
    }

    /**
     * @return 以此DiamondModel为source的ConnectionModel
     */
    public ConnectionModel getSourceConnection() {
        return sourceConnection;
    }

    public void setSourceConnection(ConnectionModel sourceConnection) {
        this.sourceConnection = sourceConnection;
        firePropertyChange(SOURCE);
    }

    public void removeSourceConnection(ConnectionModel connectionModel) {
        this.sourceConnection = null;
    }
}
