package cn.edu.tsinghua.thss.virtualenv.plc.devices.devs;

import cn.edu.tsinghua.thss.tsmart.modeling.codegen.LinkBind;


/**
 * FixedSpeedMotor
 * 1. target speed is a constant which is initialized once, it should be positive
 * 2. 
 * @author aleck
 *
 */
public class FixedSpeedMotor extends Motor {

    
    
    public void initValues() {
        initializer.addProperty("ticktime", "0");
        initializer.addProperty("fixedSpeed", "0");
        initializer.addProperty("position", "0");
        initializer.addProperty("hardDownLimit", "0");
        initializer.addProperty("softDownLimit", "0");
        initializer.addProperty("softUpLimit", "0");
        initializer.addProperty("hardUpLimit", "0");

        LinkBind lb;
        
        lb = new LinkBind();
        lb.var = "position";
        lb.type = "input";
        lb.addr = 0;
        lb.length = 0;
        lb.bit = 0;
        linking.links.add(lb);

        lb = new LinkBind();
        lb.var = "limitFlag";
        lb.type = "input";
        lb.addr = 0;
        lb.length = 0;
        lb.bit = 0;
        linking.links.add(lb);

        lb = new LinkBind();
        lb.var = "direction";
        lb.type = "output";
        lb.addr = 0;
        lb.length = 0;
        lb.bit = 0;
        linking.links.add(lb);
    }

}
