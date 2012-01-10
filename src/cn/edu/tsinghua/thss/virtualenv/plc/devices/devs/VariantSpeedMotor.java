package cn.edu.tsinghua.thss.virtualenv.plc.devices.devs;

import cn.edu.tsinghua.thss.tsmart.modeling.codegen.LinkBind;

/**
 * VariantSpeedMotor
 * 			
 * @author aleck
 *
 */
public class VariantSpeedMotor extends Motor {

    public void initValues() {
        initializer.addProperty("ticktime", "0");
        initializer.addProperty("accWork", "0");
        initializer.addProperty("accStop", "0");
        initializer.addProperty("targetSpeed", "0");
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
        lb.var = "speed";
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
        lb.var = "targetSpeed";
        lb.type = "output";
        lb.addr = 0;
        lb.length = 0;
        lb.bit = 0;
        linking.links.add(lb);
    }
}
