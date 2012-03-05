package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import java.util.ArrayList;

import org.eclipse.draw2d.Bendpoint;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IConnection;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-26<br/>
 * Time: 下午3:31<br/>
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes"})
@Root
public class ConnectionModel
                extends BaseInstanceModel<ConnectionModel, BaseTypeModel, CompoundTypeModel>
                implements
                    IConnection<ConnectionModel, CompoundTypeModel, BaseInstanceModel, BaseInstanceModel> {

    private static final long    serialVersionUID = 3033446044586077863L;
    @Element
    private BaseInstanceModel    source;
    @Element
    private BaseInstanceModel    target;
    @ElementList
    private ArrayList<Bendpoint> bendpoints;
    @Element
    private int                  argumentIndex;

    public ConnectionModel() {
        bendpoints = new ArrayList<Bendpoint>();
    }

    protected ConnectionModel(int index) {
        bendpoints = new ArrayList<Bendpoint>();
        argumentIndex = index;
    }

    public BaseInstanceModel getSource() {
        return source;
    }

    public BaseInstanceModel getTarget() {
        return target;
    }

    public void setSource(BaseInstanceModel source) {
        this.source = source;
    }

    public void setTarget(BaseInstanceModel target) {
        this.target = target;
    }

    public int getArgumentIndex() {
        return argumentIndex;
    }

    /**
     * 友好的信息显示方式
     * 
     * @return
     */
    public String getFriendlyString() {
        // source不是Connector时返回""
        if (source == null || !(source instanceof ConnectorModel)) return "";
        ConnectorTypeModel connectorType = (ConnectorTypeModel) source.getType();
        return connectorType.getArgumentAsString(argumentIndex).replaceAll("\\(.*\\)", "");
    }

    public BaseInstanceModel attachSource() {
        if (source instanceof ConnectorModel) {
            ConnectorModel connector = (ConnectorModel) source;
            if (!connector.getSourceConnections().contains(this)) {
                connector.addSourceConnection(this);
            }
        } else if (source instanceof DiamondModel) {
            DiamondModel diamond = (DiamondModel) source;
            diamond.setSourceConnection(this);
        }
        return source;
    }

    public String getTypeName() {
        BaseInstanceModel source = this.source;
        // DiamondModel: 顺序遍历到ConnectorModel
        while (source instanceof DiamondModel) {
            source = ((DiamondModel) source).getTargetConnection().getSource();
        }
        if (source instanceof ConnectorModel) {
            return ((ConnectorTypeModel) source.getType()).getArgumentType(argumentIndex);
        }
        return "";
    }

    public BaseInstanceModel attachTarget() {
        if (target instanceof BulletModel) {
            BulletModel bullet = (BulletModel) target;
            if (!bullet.getTargetConnections().contains(this)) {
                bullet.addTargetConnection(this);
                // 绑定参数
                if (!(bullet instanceof InvisibleBulletModel)) {
                    if (source instanceof ConnectorModel) {
                        ((ConnectorModel) source).bound(argumentIndex, bullet.getPort());
                    } else {
                        BaseInstanceModel model = source;
                        while (model instanceof DiamondModel) {
                            ConnectionModel connection =
                                            ((DiamondModel) model).getTargetConnection();
                            if (connection == null) {
                                System.err.println("some thing is wrong here");
                                break;
                            }
                            model = connection.getSource();
                        }
                        if (model instanceof ConnectorModel) {
                            ((ConnectorModel) model).bound(argumentIndex, bullet.getPort());
                        }
                    }
                }
            }
        } else if (target instanceof DiamondModel) {
            DiamondModel diamond = (DiamondModel) target;
            ((DiamondModel) target).setTargetConnection(this);
        }
        return target;
    }

    public BaseInstanceModel detachSource() {
        if (source instanceof ConnectorModel)
            ((ConnectorModel) source).removeSourceConnection(this);
        else if (source instanceof DiamondModel) {
            ((DiamondModel) source).removeSourceConnection(this);
        }
        return source;
    }

    public BaseInstanceModel detachTarget() {
        if (target instanceof BulletModel) {
            ((BulletModel) target).removeTargetConnection(this);
            if (target instanceof InvisibleBulletModel) {
                if (source instanceof ConnectorModel) {
                    ((CompoundTypeModel) source.getParent())
                                    .removeBullet((InvisibleBulletModel) target);
                }
            }
        }
        return target;
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return new IPropertyDescriptor[0];
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

    @Override
    public ArrayList<Bendpoint> getBendpoints() {
        return bendpoints;
    }

    public ConnectionModel setBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().set(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    public ConnectionModel addBendpoint(int index, Bendpoint bendpoint) {
        getBendpoints().add(index, bendpoint);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public ConnectionModel removeBendpoint(int index) {
        getBendpoints().remove(index);
        firePropertyChange(BEND_POINTS);
        return this;
    }

    @Override
    public Bendpoint getBendpoint(int index) {
        return getBendpoints().get(index);
    }
}
