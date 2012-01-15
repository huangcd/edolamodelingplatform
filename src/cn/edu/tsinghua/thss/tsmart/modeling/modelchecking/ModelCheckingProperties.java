package cn.edu.tsinghua.thss.tsmart.modeling.modelchecking;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root
public class ModelCheckingProperties {
    public void clearAllProperties() {
        properties.clear();
    }
    
    @ElementList
    public List<ModelCheckingProperty> properties = new ArrayList<ModelCheckingProperty>();
}
