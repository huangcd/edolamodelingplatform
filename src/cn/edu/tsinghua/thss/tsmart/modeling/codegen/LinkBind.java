package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import org.simpleframework.xml.Attribute;

public class LinkBind {
	public static final byte TYPE_INPUT_ACTION 	= 1;
	public static final byte TYPE_OUTPUT_ACTION 	= 2;
	
	@Attribute(name="type")
	public String type;
	@Attribute(name="variable")
	public String var;
	@Attribute(name="addr")
	public int addr;
	@Attribute(name="length", required=false)
	public int length;
	@Attribute(name="bit", required=false)
	public int bit;
}
