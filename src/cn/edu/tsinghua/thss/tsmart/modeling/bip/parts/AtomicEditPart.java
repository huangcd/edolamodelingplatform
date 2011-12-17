package cn.edu.tsinghua.thss.tsmart.modeling.bip.parts;


import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation.AtomicModel;

@SuppressWarnings("rawtypes")
public class AtomicEditPart extends ComponentEditPart {

    public AtomicModel getModel() {
        return (AtomicModel) super.getModel();
    }
}
