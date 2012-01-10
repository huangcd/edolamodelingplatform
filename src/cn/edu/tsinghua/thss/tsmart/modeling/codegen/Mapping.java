package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="mapping")
public class Mapping {
	@ElementList(inline=true, entry="binding", required=false)
	List<Binding> bindings = new ArrayList<Binding>();

	public List<Binding> getBindings() {
		return bindings;
	}

	public Binding searchBinding(String varname) {
		for (Binding b : bindings) {
			if (b.var.equals(varname))
				return b;
		}
		return null;
	}
	
	public void clearAllBindings() {
	    bindings.clear();
	}
}
