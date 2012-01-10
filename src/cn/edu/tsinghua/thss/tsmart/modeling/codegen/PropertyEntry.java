package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import org.simpleframework.xml.Attribute;

public class PropertyEntry {
	public PropertyEntry() { }
	
	public PropertyEntry(String property, String value) {
		this.property = property;
		this.value = value;
	}
	
	@Attribute
	public String property;
	@Attribute
	public String value;
}
