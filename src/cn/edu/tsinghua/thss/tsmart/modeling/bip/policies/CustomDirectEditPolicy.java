package cn.edu.tsinghua.thss.tsmart.modeling.bip.policies;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.commands.DirectEditCommand;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.*;


public class CustomDirectEditPolicy extends DirectEditPolicy {

    @Override
    // ��ѡ�� cell editor���޸��ı���cell editor ʧȥ����֮ǰִ�� getDirectEditCommand����
    protected Command getDirectEditCommand(DirectEditRequest request) {
        DirectEditCommand command = new DirectEditCommand();
        // �ж���place����transition....
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

        // �� cell editor �еõ� newText ���� Figure �����ı�
        command.setText((String) request.getCellEditor().getValue());
        return command;
    }

    @Override
    // showCurrentEditValue ����������ʾ Figure �еĵ�ǰֱ�ӱ༭ֵ��
    protected void showCurrentEditValue(DirectEditRequest arg0) {

    }



}
