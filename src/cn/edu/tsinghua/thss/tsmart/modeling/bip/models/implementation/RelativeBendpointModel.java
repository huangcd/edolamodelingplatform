package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.geometry.Dimension;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root
public class RelativeBendpointModel {
    @Attribute
    private int w1;
    @Attribute
    private int h1;
    @Attribute
    private int w2;
    @Attribute
    private int h2;

    public Dimension getFirstRelativeDimension() {
        return new Dimension(w1, h1);
    }

    public RelativeBendpointModel setFirstRelativeDimension(Dimension dimension) {
        this.w1 = dimension.width;
        this.h1 = dimension.height;
        return this;
    }

    public Dimension getSecondRelativeDimension() {
        return new Dimension(w2, h2);
    }

    public RelativeBendpointModel setSecondRelativeDimension(Dimension dimension) {
        this.w2 = dimension.width;
        this.h2 = dimension.height;
        return this;
    }

    public RelativeBendpointModel setRelativeDimensions(Dimension dimension1, Dimension dimension2) {
        this.w1 = dimension1.width;
        this.h1 = dimension1.height;
        this.w2 = dimension2.width;
        this.h2 = dimension2.height;
        return this;
    }
}
