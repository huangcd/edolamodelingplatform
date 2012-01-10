package cn.edu.tsinghua.thss.virtualenv.plc.devices.devs;

import cn.edu.tsinghua.thss.tsmart.modeling.codegen.Device;

/**
 * 
 * @author aleck
 *
 */
public class Light extends Device {
	public boolean on;
	
	public Light() {}
	
	public Light(String name) {
		super(name);
	}

}
