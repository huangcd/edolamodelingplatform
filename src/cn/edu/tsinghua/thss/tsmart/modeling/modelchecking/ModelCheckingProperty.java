package cn.edu.tsinghua.thss.tsmart.modeling.modelchecking;
import org.simpleframework.xml.Attribute;

public class ModelCheckingProperty {
   
    @Attribute(name="desc")
    public String description = "";
    @Attribute(name="prop")
    public String property = "";
}
