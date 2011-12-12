package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import cn.edu.tsinghua.thss.tsmart.modeling.ModelingProperties;

@Root
public class GlobalProperties implements ModelingProperties, PlatformProperties {
    @Attribute(name = "modelchecking")
    public boolean                  enableModelChecking     = false;
    @Attribute(name = "codegeneration")
    public boolean                  enableCodeGeneration    = false;
    public Font                     defaultEditorFont;
    @Element
    public String                   defaultEditorFontName   = "Consolas";
    @Element
    public int                      defaultEditorFontHeight = 10;
    @Element
    public int                      defaultEditorFontStyle  = SWT.NORMAL;

    public Color                    placeLabelColor         = ColorConstants.blue;
    public Color                    actionLabelColor        = ColorConstants.cyan;
    public Color                    guardLabelColor         = ColorConstants.green;
    public Color                    portLabelColor          = ColorConstants.gray;
    public Color                    dataLabelColor          = ColorConstants.darkGray;
    private static GlobalProperties instance;

    public final static GlobalProperties getInstance() {
        if (instance == null) {
            instance = new GlobalProperties();
        }
        return instance;
    }

    private GlobalProperties() {

    }

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

    @Override
    public Font getDefaultEditorFont() {
        if (defaultEditorFont == null) {
            Device device = Display.getCurrent();
            defaultEditorFont =
                            new Font(device, new FontData(defaultEditorFontName,
                                            defaultEditorFontHeight, defaultEditorFontStyle));
        }
        return defaultEditorFont;
    }

    @Override
    public Color getPlaceLabelColor() {
        return placeLabelColor;
    }

    @Override
    public Color getActionLabelColor() {
        return actionLabelColor;
    }

    @Override
    public Color getGuardLabelColor() {
        return guardLabelColor;
    }

    @Override
    public Color getPortLabelColor() {
        return portLabelColor;
    }

    public Color getDataLabelColor() {
        return dataLabelColor;
    }
}
