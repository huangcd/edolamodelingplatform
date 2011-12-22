package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.ArrayList;

import org.eclipse.draw2d.Bendpoint;

/**
 * Created by Huangcd<br/>
 * Date: 11-9-25<br/>
 * Time: ÏÂÎç9:40<br/>
 * Connection Ä£ÐÍ
 */
@SuppressWarnings("rawtypes")
public interface IConnection<M extends IConnection, P extends IContainer, S extends IInstance, T extends IInstance>
                extends
                    IModel<M, P> {
    S getSource();

    T getTarget();
    
    void setSource(S source);
    
    void setTarget(T target);

    S attachSource();

    T attachTarget();

    S detachSource();

    T detachTarget();

    ArrayList<Bendpoint> getBendpoints();

    Bendpoint getBendpoint(int index);

    M setBendpoint(int index, Bendpoint bendpoint);

    M addBendpoint(int index, Bendpoint bendpoint);

    M removeBendpoint(int index);
}
