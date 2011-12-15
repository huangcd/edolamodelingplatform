package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.DataTypeModel;


/**
 * Created by Huangcd<br/>
 * Date: 11-11-18<br/>
 * Time: обнГ7:48<br/>
 */
@SuppressWarnings("rawtypes")
public interface IPortType<T extends IPortType<T, I, P>, I extends IPort, P extends IDataContainer>
    extends IType<T, I, P> {

    List<DataTypeModel> getArguments();
}
