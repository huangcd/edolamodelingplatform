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
    @Element
    public String                   defaultEditorFontName   = "Consolas";
    @Element
    public int                      defaultEditorFontHeight = 10;
    @Element
    public int                      defaultEditorFontStyle  = SWT.BOLD;
    @Attribute(name = "atomicpriority")
    public boolean                  allowAtomicPriority     = false;
    @Attribute(name = "priority")
    public boolean                  allowPriority           = false;
    @Attribute(name = "broadcast")
    public boolean                  allowBroadcast          = false;

    public Color                    placeLabelColor         = ColorConstants.blue;
    public Color                    actionLabelColor        = new Color(null, 204, 102, 255);
    public Color                    guardLabelColor         = new Color(null, 100, 180, 100);
    public Color                    connectorColor          = new Color(null, 99, 37, 35);
    public Color                    connectorOutlineColor   = new Color(null, 113, 137, 63);
    public Color                    connectorFillColor      = new Color(null, 155, 187, 89);
    public Color                    connectionLabelColor    = new Color(null, 0, 51, 0);
    public Color                    atomicLabelColor        = new Color(null, 60, 60, 60);
    public Color                    compoundLabelColor      = new Color(null, 13, 13, 13);
    public Color                    portLabelColor          = ColorConstants.lightBlue;
    public Color                    dataLabelColor          = ColorConstants.darkGray;
    public Font                     defaultEditorFont;
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
        return allowAtomicPriority;
    }

    @Override
    public boolean isPriorityAllow() {
        return allowPriority;
    }

    @Override
    public boolean isBroadcastAllow() {
        return allowBroadcast;
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
    public Font getDefaultEditorBoldFont() {
        return new Font(Display.getCurrent(), new FontData(defaultEditorFontName,
                        defaultEditorFontHeight, SWT.BOLD));
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
    public Color getConnectorColor() {
        return connectorColor;
    }

    @Override
    public Color getPortLabelColor() {
        return portLabelColor;
    }

    public Color getDataLabelColor() {
        return dataLabelColor;
    }

    public Color getConnectorOutlineColor() {
        return connectorOutlineColor;
    }

    public Color getConnectorFillColor() {
        return connectorFillColor;
    }

    public Color getConnectionLabelColor() {
        return connectionLabelColor;
    }

    public Color getAtomicLabelColor() {
        return atomicLabelColor;
    }

    public Color getCompoundLabelColor() {
        return compoundLabelColor;
    }

}
