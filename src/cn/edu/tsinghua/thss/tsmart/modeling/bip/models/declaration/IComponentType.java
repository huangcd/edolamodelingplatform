package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration;

import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * Created by Huangcd<br/>
 * Date: 11-11-17<br/>
 * Time: 上午8:50<br/>
 */
@SuppressWarnings("rawtypes")
public interface IComponentType<Model extends IComponentType, Instance extends IComponentInstance, Parent extends IContainer, Child extends IModel>
                extends
                    IContainer<Model, Parent, Child>,
                    IType<Model, Instance, Parent> {
    public final static Dimension componentSize = new Dimension(200, 124);

    /** @return 返回export port的集合（组件对外可见的端口集） */
    List<IPort> getExportPorts();
}
