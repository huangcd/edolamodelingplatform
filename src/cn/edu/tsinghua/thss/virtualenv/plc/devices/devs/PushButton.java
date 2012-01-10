package cn.edu.tsinghua.thss.virtualenv.plc.devices.devs;

import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;
import cn.edu.tsinghua.thss.tsmart.modeling.codegen.LinkBind;

/**
 * 
 * @author aleck
 * 
 */
public class PushButton extends Device {
    public PushButton() {}

    public PushButton(String name) {
        super(name);
    }
    
    public void initValues() {
        initializer.addProperty("pushed", "0");

        LinkBind lb = new LinkBind();
        lb.var = "pushedInt";
        lb.type = "input";
        lb.addr = 0;
        lb.length = 0;
        lb.bit = 0;
        linking.links.add(lb);
    }
}
