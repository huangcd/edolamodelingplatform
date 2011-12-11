package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.ArrayList;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.RelativeBendpointModel;

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

    S attachSource();

    T attachTarget();

    ArrayList<RelativeBendpointModel> getBendpoints();
    
    RelativeBendpointModel getBendpoint(int index);

    M setBendpoint(int index, RelativeBendpointModel bendpoint);

    M addBendpoint(int index, RelativeBendpointModel bendpoint);

    M removeBendpoint(int index);
}
