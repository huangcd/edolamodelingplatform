package cn.edu.tsinghua.thss.tsmart.modeling.codegen;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="linking")
public class Linking {
	@ElementList(inline=true, entry="link", required=false)
	public List<LinkBind> links = new ArrayList<LinkBind>();

	public void addLink(LinkBind binding) {
		links.add(binding);
	}
	
	public List<LinkBind> getLinks() {
		return links;
	}

	public LinkBind searchLink(String varname) {
		for (LinkBind b : links) {
			if (b.var.equals(varname))
				return b;
		}
		return null;
	}
}
