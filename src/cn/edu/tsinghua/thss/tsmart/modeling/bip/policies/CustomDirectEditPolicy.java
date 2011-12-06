package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DirectEditCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;


public class CustomDirectEditPolicy extends DirectEditPolicy {

    @Override
    // 当选中 cell editor，修改文本，cell editor 失去焦点之前执行 getDirectEditCommand方法
    protected Command getDirectEditCommand(DirectEditRequest request) {
        DirectEditCommand command = new DirectEditCommand();
        // 判断是place还是transition....
        if (getHost().getModel() instanceof PlaceModel)
            command.setPlaceModel(getHost().getModel());
        else if (getHost().getModel() instanceof TransitionModel)
            command.setTransitionModel(getHost().getModel());
        else if (getHost().getModel() instanceof AtomicTypeModel)
            command.setAtomicTypeModel(getHost().getModel());
        else if (getHost().getModel() instanceof DataModel)
            command.setDataModel(getHost().getModel());
        else if (getHost().getModel() instanceof PortModel)
            command.setPortModel(getHost().getModel());
        else if (getHost().getModel() instanceof PriorityModel)
            command.setPriorityModel(getHost().getModel());

        // 从 cell editor 中得到 newText 来给 Figure 设置文本
        command.setText((String) request.getCellEditor().getValue());
        return command;
    }

    @Override
    // showCurrentEditValue 方法用于显示 Figure 中的当前直接编辑值。
    protected void showCurrentEditValue(DirectEditRequest arg0) {

    }



}
