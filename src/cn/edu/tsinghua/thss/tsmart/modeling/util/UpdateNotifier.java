package cn.edu.tsinghua.thss.tsmart.modeling.util;

import java.util.List;

public interface UpdateNotifier {
    List<UpdateReceiver> getRegisterObjects();

    void register(UpdateReceiver obj);

    void unRegister(UpdateReceiver obj);

    void notifyRegisterObjects();
}
