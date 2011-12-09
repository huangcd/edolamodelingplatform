package cn.edu.tsinghua.thss.tsmart.modeling.bip.models.implementation;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;

import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IContainer;
import cn.edu.tsinghua.thss.tsmart.modeling.bip.models.declaration.IType;

@SuppressWarnings("rawtypes")
public class LabelModel extends BaseInstanceModel<LabelModel, IType, IContainer> {

    private String             labelText;
    private Color              textColor;
    public final static String LABEL = "label";
    public final static String COLOR = "color";

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        firePropertyChange(COLOR);
    }

    public Dimension getLabelSize() {
        Label lbl = new Label(labelText);
        lbl.setFont(prorerties.getDefaultEditorFont());
        Dimension size = lbl.getPreferredSize();
        size.setWidth(size.width + 5);
        return size;
    }

    public String getLabel() {
        return labelText;
    }

    public void setLabel(String label) {
        this.labelText = label;
        firePropertyChange(LABEL);
    }

    @Override
    public boolean exportable() {
        return false;
    }

    @Override
    public String exportToBip() {
        return "";
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void setPropertyValue(Object id, Object value) {}
}
