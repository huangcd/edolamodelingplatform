package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import org.simpleframework.xml.Attribute;

public class Binding {
	public static final byte TYPE_INPUT_ACTION 	= 1;
	public static final byte TYPE_OUTPUT_ACTION 	= 2;
	
	@Attribute(name="type")
	public String type = "";
	@Attribute(name="variable")
	public String var = "";
	@Attribute(name="addr")
	public int addr = 0;
	@Attribute(name="length", required=false)
	public int length = 0;
	@Attribute(name="bit", required=false)
	public int bit = 0;
	
	public byte getIoType() {
		this.type = type.toLowerCase();
		if (type.equals("input")) {
			return TYPE_INPUT_ACTION;
		} else if (type.equals("output")) {
			return TYPE_OUTPUT_ACTION;
		} else {
			throw new RuntimeException("Invalid mapping type!");
		}
	}
	
	@Override
	public String toString() {
		return var + " -- " + type + "(" + addr + "." + bit + ")";
	}
}

