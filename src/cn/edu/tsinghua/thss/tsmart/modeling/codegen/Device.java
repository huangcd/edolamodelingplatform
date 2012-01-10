package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;


abstract public class Device {
	public Device() { }
	
	public Device(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void initValues() {
	    
	}
	
	@Attribute
	public String name = "";
	@Attribute(name="desc")
	public String description = "";
	@Element
	public Linking linking = new Linking();
	@Element
	public PropertyList initializer = new PropertyList();
}
