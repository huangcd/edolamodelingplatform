package cn.edu.tsinghua.thss.tsmart.platform;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

public interface PlatformProperties {
    String DATA_TYPE_FILE      = "datatype";
    String PORT_TYPE_FILE      = "porttype";
    String CONNECTOR_TYPE_FILE = "connectortype";

    Font getDefaultEditorFont();

    Color getPlaceLabelColor();

    Color getActionLabelColor();

    Color getGuardLabelColor();

    Color getPortLabelColor();

    Color getDataLabelColor();

    Font getDefaultEditorBoldFont();
}
