package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class DeviceDefinition {
    public void clearAllDevices() {
        devices.clear();
    }
    
    @ElementList
    public List<Device> devices = new ArrayList<Device>();
}
