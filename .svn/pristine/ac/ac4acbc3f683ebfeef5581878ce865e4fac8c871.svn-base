package cn.edu.tsinghua.thss.tsmart.platform;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class GlobalProperties implements Properties {
    @Attribute(name = "modelchecking")
    public boolean enableModelChecking  = false;
    @Attribute(name = "codegeneration")
    public boolean enableCodeGeneration = false;
    public boolean isMultipleDataTypeAvailble() {
        return !(enableModelChecking || enableCodeGeneration);
    }

    @Override
    public boolean isAtomicPriorityAllow() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isPriorityAllow() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isBroadcastAllow() {
        // TODO Auto-generated method stub
        return false;
    }
}
